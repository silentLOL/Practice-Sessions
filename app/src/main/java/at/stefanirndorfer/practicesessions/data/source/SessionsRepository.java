package at.stefanirndorfer.practicesessions.data.source;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
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

    private Map<String, Session> mCachedSessions;
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
                cacheSessions(sessions);
                mSessionLiveData.setValue(sessions);
            }
        });
    }

    private void cacheSessions(List<Session> sessions) {
        if (mCachedSessions != null) {
            mCachedSessions.clear();
        } else {
            mCachedSessions = new HashMap<>();
        }
        cacheSessionsListAsMap(sessions);
        mCacheIsDirty = false;
    }

    private void cacheSessionsListAsMap(List<Session> sessions) {
        for (Session currElement : sessions) {
            mCachedSessions.put(currElement.getName(), currElement);
        }
    }
}
