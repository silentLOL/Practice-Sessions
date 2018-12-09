package at.stefanirndorfer.practicesessions.session;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise;
import at.stefanirndorfer.practicesessions.data.source.SessionsRepository;
import at.stefanirndorfer.practicesessions.util.NetworkUtils;
import timber.log.Timber;

public class SessionsViewModel extends AndroidViewModel {

    public final ObservableBoolean mDataLoading = new ObservableBoolean(false);
    public final ObservableBoolean mNetworkAvailable = new ObservableBoolean(false);

    private final SessionsRepository mSessionsRepository;
    private final Context mContext;
    private MutableLiveData<List<SessionRelatedExercise>> mSessionRelatedExercises = new MutableLiveData<>();

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
            loaedSessionData(true);
        } else {
            mNetworkAvailable.set(false);
        }
    }

    private void loaedSessionData(boolean showLoadingUI) {
        if (showLoadingUI) {
            mDataLoading.set(true);
        }
        mSessionsRepository.getSortedExercises().observeForever(sessionRelatedExercises -> {
            Timber.d("received " + sessionRelatedExercises.size() + " sessions from repository");
            mSessionRelatedExercises.setValue(sessionRelatedExercises);
            mDataLoading.set(false);
        });
    }

    public MutableLiveData<List<SessionRelatedExercise>> getSessionRelatedExercises() {
        return mSessionRelatedExercises;
    }


    public void onRetryButtonClicked() {
        start();
    }
}
