package in.org.cris.icms.network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.org.cris.icms.models.login.LoginRequest;
import in.org.cris.icms.models.login.LoginResponse;
import in.org.cris.icms.models.login.User;
import in.org.cris.icms.models.logout.LogoutRequest;
import in.org.cris.icms.models.logout.LogoutResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by anurag on 5/7/17.
 */
public class LoginClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://192.168.43.208:8080/LoginApp/";

    public static LoginInterface getLoginInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(LoginInterface.class);
    }

    public interface LoginInterface{
        @POST("login")
        Call<LoginResponse> attemptLogin(@Body LoginRequest request);

        @POST("logout")
        Call<LogoutResponse> logOut(@Body LogoutRequest request);
    }
}
