package at.stefanirndorfer.practicesessions.data.source;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise;

public interface SessionsDataSource {

    /**
     * To request on all sessions Sessions (unorderd)
     * occurring Errors can be observed via {@link #getErrors()}
     * @return a live data object to obtain the requested data.
     */
    MutableLiveData<List<Session>> getSessions();

    /**
     * To request a list of SessionRelatedExercises sorted by the sessions date
     * (latest first)
     * occurring Errors can be observed via {@link #getErrors()}
     * @returns a live data object to obtain the requested data.
     */
    MutableLiveData<List<SessionRelatedExercise>> getSortedExercises();

    /**
     * to allow subscriptions on any occuring errors
     * it will act like a broadcast and must be associated on the consumer side
     * to an individual call.
     *
     * @return
     */
    public MutableLiveData<Throwable> getErrors();
}
