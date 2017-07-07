package in.org.cris.icms.network;

import in.org.cris.icms.models.consistverification.Trains;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

/**
 * Created by anurag on 7/7/17.
 */
public class ICMSClient {
    private static Retrofit retrofit = null;
    private static String BASE_URL = "http://172.16.2.118:9080/cois/";

    public static ICMSInterface getICMSInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(ICMSInterface.class);
    }

    public interface ICMSInterface{
        @POST("AppServices?service=CV")
        Call<Trains> getTrains();
    }
}
