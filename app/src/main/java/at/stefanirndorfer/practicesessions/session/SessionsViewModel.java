package at.stefanirndorfer.practicesessions.session;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.data.source.SessionsRepository;
import at.stefanirndorfer.practicesessions.util.NetworkUtils;
import timber.log.Timber;

public class SessionsViewModel extends AndroidViewModel {

    public final ObservableBoolean mDataLoading = new ObservableBoolean(false);
    public final ObservableBoolean mNetworkAvailable = new ObservableBoolean(false);

    private final SessionsRepository mSessionsRepository;
    private final Context mContext;
    private MutableLiveData<List<Session>> mSessions = new MutableLiveData<>();

    public SessionsViewModel(@NonNull Application application, SessionsRepository repository) {
        super(application);
        this.mContext = application.getApplicationContext();
        mSessionsRepository = repository;
    }

    /**
     * requests Session data from the repository
     * if a network connection is given
     */
    public void start() {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            mNetworkAvailable.set(true);
            Timber.d("Requesting session data from data source.");
            loadSessions(true);
        } else {
            mNetworkAvailable.set(false);
        }
    }

    private void loadSessions(boolean showLoadingUI) {
        if (showLoadingUI) {
            mDataLoading.set(true);
        }
        mSessionsRepository.getSessions().observeForever(new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                Timber.d("received " + sessions.size() + " sessions from repository");
                mSessions.setValue(sessions);
                mDataLoading.set(false);
            }
        });
    }

    public MutableLiveData<List<Session>> getSessions() {
        return mSessions;
    }


    public void onRetryButtonClicked(){
        start();
    }
}
