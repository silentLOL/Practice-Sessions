package at.stefanirndorfer.practicesessions.session;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise;
import at.stefanirndorfer.practicesessions.data.source.SessionsRepository;
import at.stefanirndorfer.practicesessions.util.NetworkUtils;
import timber.log.Timber;

public class SessionsViewModel extends AndroidViewModel {

    public final ObservableBoolean mDataLoading = new ObservableBoolean(false);
    public final ObservableBoolean mNetworkAvailable = new ObservableBoolean(false);
    public final ObservableBoolean mIsError = new ObservableBoolean(false);
    public final ObservableField<String> mErrorMessage = new ObservableField<>("");

    private final SessionsRepository mSessionsRepository;
    @SuppressLint("StaticFieldLeak")
    private final Context mContext; /* Application Context only */
    private MutableLiveData<List<SessionRelatedExercise>> mSessionRelatedExercises = new MutableLiveData<>();

    public SessionsViewModel(@NonNull Application application, SessionsRepository repository) {
        super(application);
        this.mContext = application.getApplicationContext();
        mSessionsRepository = repository;
        subscribeonErrors();
    }

    private void subscribeonErrors() {
        mSessionsRepository.getErrors().observeForever(new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                if (throwable != null) {
                    mIsError.set(true);
                    mErrorMessage.set(throwable.getMessage());
                } else {
                    setNoError();
                }
            }
        });
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
            setNoError();
        } else {
            mNetworkAvailable.set(false);
        }
    }

    private void loaedSessionData(boolean showLoadingUI) {
        if (showLoadingUI) {
            mDataLoading.set(true);
        }
        mSessionsRepository.getSortedExercises().observeForever(sessionRelatedExercises -> {
            if (sessionRelatedExercises != null && !sessionRelatedExercises.isEmpty()) {
                Timber.d("received " + sessionRelatedExercises.size() + " sessions from repository");
                mSessionRelatedExercises.setValue(sessionRelatedExercises);
                mDataLoading.set(false);
            }
        });
    }

    public MutableLiveData<List<SessionRelatedExercise>> getSessionRelatedExercises() {
        return mSessionRelatedExercises;
    }


    public void onRetryButtonClicked() {
        start();
    }

    private void setNoError() {
        mErrorMessage.set("");
        mIsError.set(false);
    }

    public void loadExerciseImage(ImageView target, String imageUrl, Callback callback) {
        mSessionsRepository.loadImageIntoView(mContext, target, imageUrl, callback);
    }
}
