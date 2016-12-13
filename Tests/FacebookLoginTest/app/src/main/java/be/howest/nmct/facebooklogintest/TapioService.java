package be.howest.nmct.facebooklogintest;

import be.howest.nmct.facebooklogintest.models.facebookTokenResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by brianmasschaele on 12/12/16.
 */

public interface TapioService {

    @GET("auth/facebook/token")
    Call<facebookTokenResponse> facebookTokenLogin(@Query("access_token") String access_token);
}
