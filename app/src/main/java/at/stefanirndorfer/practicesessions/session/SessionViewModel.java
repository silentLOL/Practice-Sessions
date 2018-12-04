package at.stefanirndorfer.practicesessions.session;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class SessionViewModel extends AndroidViewModel {

    private final SessionsRepository mSessionsRepository;

    public SessionViewModel(@NonNull Application application, SessionsRepository repository) {
        super(application);
        mSessionsRepository = repository;
    }
}
