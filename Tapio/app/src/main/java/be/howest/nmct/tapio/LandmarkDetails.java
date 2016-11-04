package be.howest.nmct.tapio;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.howest.nmct.tapio.databinding.ActivityLandmarkDetailsBinding;

public class LandmarkDetails extends AppCompatActivity {
    private ActivityLandmarkDetailsBinding detailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_details);

        detailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_landmark_details);
        detailsBinding.setLandmark(MainActivity.landmarks.getLandmarks().get(0));
    }


}
