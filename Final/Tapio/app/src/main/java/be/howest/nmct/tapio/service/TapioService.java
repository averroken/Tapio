package be.howest.nmct.tapio.service;
import be.howest.nmct.tapio.model.FacebookTokenResponse;
import be.howest.nmct.tapio.model.LandmarkBody;
import be.howest.nmct.tapio.model.LandmarkPostResponse;
import be.howest.nmct.tapio.model.LoginResponse;
import be.howest.nmct.tapio.model.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TapioService {

    @FormUrlEncoded
    @POST("/android/login")
    Call<LoginResponse> logIn(@Field("username") String username, @Field("password") String password);

    @GET("api/android/tokenTest")
    Call<TokenResponse> testToken(@Query("token") String token);

    @POST("api/landmarks")
    Call<LandmarkPostResponse> addLandmark(@Query("token") String token, @Body LandmarkBody body);

    @GET("auth/facebook/token")
    Call<FacebookTokenResponse> facebookTokenLogin(@Query("access_token") String access_token);
}
