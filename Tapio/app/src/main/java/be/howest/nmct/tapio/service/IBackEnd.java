package be.howest.nmct.tapio.service;

import be.howest.nmct.tapio.model.LandmarksList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface IBackEnd {

    @GET("/api/landmarks")
    Call<LandmarksList> getAllLandMarks();
}
