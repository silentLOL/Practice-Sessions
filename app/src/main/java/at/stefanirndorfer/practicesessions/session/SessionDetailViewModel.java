package at.stefanirndorfer.practicesessions.session;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.data.source.SessionsRepository;
import timber.log.Timber;

public class SessionDetailViewModel extends AndroidViewModel {
    public final ObservableBoolean mDataLoading = new ObservableBoolean(false);
    public final ObservableField<String> mSessionName = new ObservableField<>();
    public final ObservableField<String> mPracticedOn = new ObservableField<>();

    private final SessionsRepository mSessionsRepository;
    private MutableLiveData<Session> mSession = new MutableLiveData<>();

    public SessionDetailViewModel(@NonNull Application application, SessionsRepository repository) {
        super(application);
        mSessionsRepository = repository;
    }

    /**
     * we assume now, that we don't have to check the network connection
     * at that point because the requested data should be cached already
     *
     * @param id
     */
    public void start(int id) {
        Timber.d("Loading session by id " + id);
        loadSessionById(id, true);
    }

    private void loadSessionById(int id, boolean showLoadingUI) {
        if (showLoadingUI) {
            mDataLoading.set(true);
        }
        mSessionsRepository.getSessionById(id).observeForever(session -> {
            if (session != null) {
                Timber.d("received session from repository. Session name: " + session.getName());
                mSessionName.set(session.getName());
                mPracticedOn.set(session.getPracticedOnDateAsString());
                mSession.setValue(session);
            }
        });
    }

    public MutableLiveData<Session> getSession() {
        return mSession;
    }
}
