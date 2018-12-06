package at.stefanirndorfer.practicesessions.data.source;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise;

public interface SessionsDataSource {

    MutableLiveData<List<Session>> getSessions();

    MutableLiveData<Session> getSessionById(int id);

    MutableLiveData<List<SessionRelatedExercise>> getSortedExercises();
}
