package at.stefanirndorfer.practicesessions.session;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.data.source.SessionsRepository;
import timber.log.Timber;

public class SessionsViewModel extends AndroidViewModel {

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final SessionsRepository mSessionsRepository;
    private MutableLiveData<List<Session>> mSessions = new MutableLiveData<>();

    public SessionsViewModel(@NonNull Application application, SessionsRepository repository) {
        super(application);
        mSessionsRepository = repository;
    }

    /**
     * requests Session data from the repository
     */
    public void start() {
        Timber.d("Requesting session data from data source.");
        loadSessions(true);
    }

    private void loadSessions(boolean showLoadingUI) {
        if (showLoadingUI){
            dataLoading.set(true);
        }
        mSessionsRepository.getSessions().observeForever(new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                Timber.d("received " + sessions.size() + " sessions from repository");
                mSessions.setValue(sessions);
                dataLoading.set(false);
            }
        });
    }

    public MutableLiveData<List<Session>> getSessions() {
        return mSessions;
    }


}
