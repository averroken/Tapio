package be.howest.nmct.herokulogin;

import be.howest.nmct.herokulogin.models.LoginResponse;
import be.howest.nmct.herokulogin.models.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by brianmasschaele on 21/11/16.
 */

public interface TapioService {

    @FormUrlEncoded
    @POST("/android/login")
    Call<LoginResponse> logIn(@Field("username") String username, @Field("password") String password);

    @GET("api/android/tokenTest")
    Call<TokenResponse> getToken(@Query("token") String token);

}
