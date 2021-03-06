package at.stefanirndorfer.practicesessions.data.source.remote;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise;
import at.stefanirndorfer.practicesessions.data.source.SessionsDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SessionsNetworkDataSource implements SessionsDataSource {

    private static SessionsNetworkDataSource instance;
    private MutableLiveData<Throwable> mErrors = new MutableLiveData<>();

    private SessionsNetworkDataSource() {
        /* intentionally left empty */
    }

    public static SessionsNetworkDataSource getInstance() {
        if (instance == null) {
            instance = new SessionsNetworkDataSource();
        }
        return instance;
    }

    /**
     * actual implementation of the network call for sessions
     *
     * @returns live data in order to push the asynchronously answered call
     */
    @Override
    public MutableLiveData<List<Session>> getSessions() {
        final MutableLiveData<List<Session>> returningData = new MutableLiveData<>();

        RequestSessionsService service = RetrofitClient.getRetrofitInstance().create(RequestSessionsService.class);
        Call<List<Session>> call = service.getSessions();
        Timber.d(call.request().toString());
        call.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                Timber.d("Received response");
                if (response.body() != null) {
                    List<Session> result = response.body();
                    if (!result.isEmpty()) {
                        returningData.setValue(result);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                Timber.e("Error calling for sessions: " + t.getMessage());
                returningData.setValue(Collections.<Session>emptyList());
                mErrors.setValue(t);
            }
        });
        return returningData;
    }

    /**
     * intentionally left empty
     *
     * @return
     */
    @Override
    public MutableLiveData<List<SessionRelatedExercise>> getSortedExercises() {
        return null;
    }

    @Override
    public MutableLiveData<Throwable> getErrors() {
        return mErrors;
    }

    @Override
    public void loadImageIntoView(Context context, ImageView target, String imageUrl, com.squareup.picasso.Callback callback) {
        Picasso.with(context)
                .load(imageUrl)
                .into(target, callback);
    }
}
