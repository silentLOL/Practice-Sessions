package at.stefanirndorfer.practicesessions.data.source;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;

public interface SessionsDataSource {

    MutableLiveData<List<Session>> getSessions();
}
