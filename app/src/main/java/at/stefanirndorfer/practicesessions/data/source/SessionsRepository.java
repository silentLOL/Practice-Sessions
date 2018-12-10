package at.stefanirndorfer.practicesessions.data.source;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.stefanirndorfer.practicesessions.data.Exercise;
import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise;
import at.stefanirndorfer.practicesessions.util.AppExecutors;
import at.stefanirndorfer.practicesessions.util.SessionsUtils;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Concrete implementation to load sessions from the data sources into a cache.
 * This class is the main-talk-to point when data should be fetched
 */
public class SessionsRepository implements SessionsDataSource {

    private volatile static SessionsRepository INSTANCE = null;
    private final SessionsDataSource mRemoteSessionsDataSource;
    private final AppExecutors mExecutors;
    private Object lock = new Object();

    private Map<Integer, Session> mCachedSessions;
    private final MutableLiveData<List<Session>> mSessionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> mErrors = new MutableLiveData<>();

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     */
    private boolean mCacheIsDirty = false;


    //private constructor to prevent direct instantiation
    private SessionsRepository(@NonNull SessionsDataSource remoteSessionsDataSource, AppExecutors executors) {
        mRemoteSessionsDataSource = checkNotNull(remoteSessionsDataSource); /* lets fail hard in case its null */
        mExecutors = executors;
        subscribeOnErrors();
    }

    private void subscribeOnErrors() {
        mRemoteSessionsDataSource.getErrors().observeForever(new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                mErrors.setValue(throwable);
            }
        });
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteSessionsDataSource the remote data source
     * @return the {@link SessionsRepository} instance
     */
    public static SessionsRepository getInstance(SessionsDataSource remoteSessionsDataSource, AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (SessionsRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionsRepository(remoteSessionsDataSource, executors);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(SessionsDataSource, AppExecutors)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    //----------------------------------------------//
    // begin data requests
    //----------------------------------------------//

    @Override
    public MutableLiveData<List<Session>> getSessions() {
        if (mCachedSessions != null && !mCacheIsDirty) {
            Timber.d("Session data are already cached.");
            mSessionLiveData.postValue(new ArrayList<>(mCachedSessions.values()));
        } else {
            // we go for the remote data source
            getSessionsFromRemoteDataSource();
        }
        return mSessionLiveData;
    }


    @Override
    public MutableLiveData<List<SessionRelatedExercise>> getSortedExercises() {
        MutableLiveData<List<SessionRelatedExercise>> returningData = new MutableLiveData<>();
        if (mCachedSessions == null || mCachedSessions.isEmpty()) {
            // we need to do our network request first
            getSessions().observeForever(sessions -> {
                if (sessions != null && !sessions.isEmpty()) {
                    generateSortedExerciseList(returningData);
                }
            });
        } else {
            generateSortedExerciseList(returningData);
        }
        return returningData;
    }

    //----------------------------------------------//
    // end data requests
    //----------------------------------------------//

    /**
     * to allow subscriptions on any occurring errors
     * it will act like a broadcast and must be associated on the consumer side
     * to an individual call.
     *
     * @return
     */
    public MutableLiveData<Throwable> getErrors() {
        return mErrors;
    }


    private void generateSortedExerciseList(MutableLiveData<List<SessionRelatedExercise>> returningData) {
        //lets do it on a background thread
        mExecutors.diskIO().execute(() -> {
            List<Session> sessions = new ArrayList<>();
            sessions.addAll(mCachedSessions.values());
            SessionsUtils.sortSessionByPracticeDate(sessions);
            // now we got the sessions in the right order to calculate the performances
            SessionsUtils.calculatePerformances(sessions);
            // now sort them in reverse order (latest session first)
            SessionsUtils.reverseSessionsList(sessions);
            List<SessionRelatedExercise> sessionRelatedExercises = convertSessionsToSessionRelatedExercises(sessions);
            returningData.postValue(sessionRelatedExercises); /* use post value since we are not on the main thread */
        });
    }

    /**
     * creates the data structure we need to populate the UI
     *
     * @param sessions
     * @return
     */
    private List<SessionRelatedExercise> convertSessionsToSessionRelatedExercises(List<Session> sessions) {
        List<SessionRelatedExercise> sessionRelatedExercises = new ArrayList<>();
        for (Session currSession : sessions) {
            sessionRelatedExercises.add(buildSessionItem(currSession));
            for (Exercise currExercise : currSession.getExercises()) {
                sessionRelatedExercises.add(buildExerciseItem(currExercise));
            }
            sessionRelatedExercises.add(buildSeperator());
        }
        return sessionRelatedExercises;
    }

    private SessionRelatedExercise buildExerciseItem(Exercise currExercise) {
        return new SessionRelatedExercise.Builder()
                .setItemType(SessionRelatedExercise.ItemType.Exercise)
                .setExercise(currExercise)
                .build();
    }

    private SessionRelatedExercise buildSeperator() {
        return new SessionRelatedExercise.Builder()
                .setItemType(SessionRelatedExercise.ItemType.Seperator)
                .build();
    }

    private SessionRelatedExercise buildSessionItem(Session currSession) {
        return new SessionRelatedExercise.Builder()
                .setSessionName(currSession.getName())
                .setItemType(SessionRelatedExercise.ItemType.Session)
                .setPracticedOnDate(currSession.getPracticedOnDateAsString())
                .build();
    }


    /**
     * uses the remote data source to request the
     * session data
     */
    private void getSessionsFromRemoteDataSource() {
        mRemoteSessionsDataSource.getSessions().observeForever(sessions -> {
            if (sessions != null && !sessions.isEmpty()) {
                sessions = addIdsToSessions(sessions);
                cacheSessions(sessions);
                mSessionLiveData.setValue(sessions);
            }
        });
    }


    /**
     * adding a client sided unique identifier to be not related on Strings as ids
     * which could be redundant
     *
     * @param sessions
     * @return
     */
    private List<Session> addIdsToSessions(List<Session> sessions) {
        int i = 0;
        for (Session currElement : sessions) {
            currElement.setId(i);
            i++;
        }
        return sessions;
    }

    /**
     * deletes already cached sessions and
     * and caches the passed in Sessions
     *
     * @param sessions
     */
    private void cacheSessions(List<Session> sessions) {
        synchronized (lock) {
            if (mCachedSessions != null) {
                mCachedSessions.clear();
            } else {
                mCachedSessions = new HashMap<>();
            }
            cacheSessionsListAsMap(sessions);
            mCacheIsDirty = false;
        }
    }

    private void cacheSessionsListAsMap(List<Session> sessions) {
        for (Session currElement : sessions) {
            mCachedSessions.put(currElement.getId(), currElement);
        }
    }
}
