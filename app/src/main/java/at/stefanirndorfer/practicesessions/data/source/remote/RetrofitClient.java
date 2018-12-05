package at.stefanirndorfer.practicesessions.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.stefanirndorfer.practicesessions.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
            .create();


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(new OkHttpClient())
                    .build();
        }
        return retrofit;
    }
}
