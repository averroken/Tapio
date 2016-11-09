package be.howest.nmct.tapio.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;


import be.howest.nmct.tapio.BR;
import be.howest.nmct.tapio.databinding.FragmentLandmarksListBinding;
import be.howest.nmct.tapio.model.LandmarksList;
import be.howest.nmct.tapio.service.ApiHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LandmarksListFragmentViewModel extends BaseObservable {

    private FragmentLandmarksListBinding fragmentLandmarksListBinding;
    private Context context;

    @Bindable
    private LandmarksList landmarksList = null;

    public LandmarksList getLandmarksList() {
        return landmarksList;
    }

    public void setLandmarksList(LandmarksList landmarksList) {
        this.landmarksList = landmarksList;
    }

    public LandmarksListFragmentViewModel(FragmentLandmarksListBinding fragmentLandmarksListBinding, Context context) {
        this.fragmentLandmarksListBinding = fragmentLandmarksListBinding;
        this.context = context;
    }


    public void loadLandmarks() {
        Call<LandmarksList> call = ApiHelper.getApiService().getAllLandMarks();
        call.enqueue(landmarkListCallback);
    }


    //Retrofit listeners
    private Callback<LandmarksList> landmarkListCallback = new Callback<LandmarksList>() {

        @Override
        public void onResponse(Call<LandmarksList> call, Response<LandmarksList> response) {
            response.body();
            fragmentLandmarksListBinding.setLandmarksList(response.body());
            notifyPropertyChanged(BR.landmarksList);
        }

        @Override
        public void onFailure(Call<LandmarksList> call, Throwable t) {
            Log.d(getClass().getName(), t.getMessage());
        }
    };

}
