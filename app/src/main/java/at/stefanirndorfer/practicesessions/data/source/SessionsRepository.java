package at.stefanirndorfer.practicesessions.data.source;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import at.stefanirndorfer.practicesessions.data.Session;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Concrete implementation to load sessions from the data sources into a cache.
 * This class is the main-talk-to point when data should be fetched
 */
public class SessionsRepository implements SessionsDataSource {

    private volatile static SessionsRepository INSTANCE = null;
    private final SessionsDataSource mRemoteSessionsDataSource;
    private Object lock = new Object();

    private Map<Integer, Session> mCachedSessions;
    private final MutableLiveData<List<Session>> mSessionLiveData = new MutableLiveData<>();

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     */
    private boolean mCacheIsDirty = false;


    //private constructor to prevent direct instantiation
    private SessionsRepository(@NonNull SessionsDataSource remoteSessionsDataSource) {
        mRemoteSessionsDataSource = checkNotNull(remoteSessionsDataSource); /* lets fail hard in case its null */
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteSessionsDataSource the remote data source
     * @return the {@link SessionsRepository} instance
     */
    public static SessionsRepository getInstance(SessionsDataSource remoteSessionsDataSource) {
        if (INSTANCE == null) {
            synchronized (SessionsRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionsRepository(remoteSessionsDataSource);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(SessionsDataSource)} to create a new instance
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

    /**
     * in our Application this method is only called after the
     * Network call has been done. We assume these data are cached already.
     * otherwise we fail.
     *
     * @param id unique identifier for each session object
     * @return
     */
    @Override
    public MutableLiveData<Session> getSessionById(int id) {
        MutableLiveData<Session> returningData = new MutableLiveData<>();
        if (mCachedSessions == null || mCachedSessions.isEmpty()) {
            throw new IllegalStateException("The cache is empty or null. Should not be the case!");
        } else {
            returningData.setValue(mCachedSessions.get(id));
        }
        return returningData;
    }

    //----------------------------------------------//
    // end data requests
    //----------------------------------------------//

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
                mCachedSessions = new LinkedHashMap<>();
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
