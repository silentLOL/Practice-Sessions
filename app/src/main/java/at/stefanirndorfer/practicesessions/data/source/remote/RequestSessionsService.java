package at.stefanirndorfer.practicesessions.data.source.remote;

import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestSessionsService {

    @GET("/sessions.json")
    Call<List<Session>> getSessions();
}
