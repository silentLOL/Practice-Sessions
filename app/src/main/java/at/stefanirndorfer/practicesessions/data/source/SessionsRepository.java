package at.stefanirndorfer.practicesessions.data.source;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import at.stefanirndorfer.practicesessions.data.Session;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Concrete implementation to load recipes from the data sources into a cache.
 * This class is the main-talk-to point when data should be fetched
 */
public class SessionsRepository implements SessionsDataSource {

    private volatile static SessionsRepository INSTANCE = null;
    private final SessionsDataSource mRemoteSessionsDataSource;
    private Object lock = new Object();

    Map<String, Session> mCachedSessions;
    final MutableLiveData<List<Session>> mSessionLiveData = new MutableLiveData<>();

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
        return null;
    }
}
