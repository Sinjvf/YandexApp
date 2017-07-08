package ru.mobilization.sinjvf.mapsapp.retrofit;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ru.mobilization.sinjvf.mapsapp.retrofit.googlePojo.GoogleDirectionData;

/**
 * Created by asus on 08.07.2017.
 */

public class ServerContract {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";


    public static final String P_ORIGIN = "origin";
    public static final String P_DEST = "destination";
    public static final String P_MODE = "mode";
    public static final String P_KEY = "key";

    public interface MapsAPI {

        @GET("directions/json")
        Call<GoogleDirectionData> getDirection(@QueryMap Map<String, String> params);
    }

    private static class ServiceGenerator {

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        static Gson gson = new Gson();

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        //       .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        //    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(ServerContract.BASE_URL);

        private static <S> S createService(Class<S> serviceClass) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();
            Retrofit retrofit = builder.client(client).build();
            return retrofit.create(serviceClass);
        }


    }

    public static MapsAPI getService() {
        return ServiceGenerator.createService(ServerContract.MapsAPI.class);
    }
}
