package in.org.cris.icms.network;

import in.org.cris.icms.models.consistverification.Coach;
import in.org.cris.icms.models.consistverification.Consist;
import in.org.cris.icms.models.consistverification.Trains;
import in.org.cris.icms.models.login.LoginRequest;
import in.org.cris.icms.models.login.LoginResponse;
import in.org.cris.icms.models.logout.LogoutRequest;
import in.org.cris.icms.models.logout.LogoutResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by anurag on 7/7/17.
 */
public class ICMSClient {
    private static Retrofit retrofit = null;
    private static String BASE_URL = "";

    public static ICMSInterface getICMSInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(ICMSInterface.class);
    }

    public interface ICMSInterface{
        @POST("login")
        Call<LoginResponse> attemptLogin(@Body LoginRequest request);

        @POST("logout")
        Call<LogoutResponse> logOut(@Body LogoutRequest request);

        @POST("AppServices")
        Call<Trains> getTrains(@Query("service") String service, @Query("type") String type);

        @POST("AppServices")
        Call<Consist> getConsist(@Query("service") String service, @Query("type") String type, @Query("TrainNo") String trainNo, @Query("trnStDt") String startDate);
    }
}
