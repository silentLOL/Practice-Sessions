package at.stefanirndorfer.practicesessions.session;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.Exercise;
import at.stefanirndorfer.practicesessions.data.source.SessionsRepository;

public class SessionDetailViewModel extends AndroidViewModel {
    public final ObservableBoolean mDataLoading = new ObservableBoolean(false);
    public final ObservableField<String> mSessionName = new ObservableField<>();

    private final SessionsRepository mSessionsRepository;
    private final Context mContext;
    private MutableLiveData<List<Exercise>> mExercises = new MutableLiveData<>();

    public SessionDetailViewModel(@NonNull Application application, SessionsRepository repository) {
        super(application);
        this.mContext = application.getApplicationContext();
        mSessionsRepository = repository;
    }


    public void start(String sessionName) {
        mSessionName.set(sessionName);

    }
}
