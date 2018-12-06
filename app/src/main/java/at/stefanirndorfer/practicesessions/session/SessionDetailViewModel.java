package at.stefanirndorfer.practicesessions.session;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public class SessionDetailViewModel extends AndroidViewModel {
    public final ObservableBoolean mDataLoading = new ObservableBoolean(false);
    public final ObservableField<String> mSessionName = new ObservableField<>();

    public SessionDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void start(String sessionName){
        mSessionName.set(sessionName);

    }
}
