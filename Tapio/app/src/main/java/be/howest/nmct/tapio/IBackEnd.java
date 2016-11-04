package be.howest.nmct.tapio;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.tapio.model.LandmarkList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by brianmasschaele on 4/11/16.
 */

public interface IBackEnd {

    @GET("/api/landmarks")
    Call<LandmarkList> getAllLandMarks();

}
