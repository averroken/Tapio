package be.howest.nmct.addlandmarktest;


import be.howest.nmct.addlandmarktest.models.LandmarkBody;
import be.howest.nmct.addlandmarktest.models.LandmarkPostResponse;
import be.howest.nmct.addlandmarktest.models.LoginResponse;
import be.howest.nmct.addlandmarktest.models.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<TokenResponse> testToken(@Query("token") String token);

    @POST("api/landmarks")
    Call<LandmarkPostResponse> addLandmark(@Query("token") String token, @Body LandmarkBody body);
}
