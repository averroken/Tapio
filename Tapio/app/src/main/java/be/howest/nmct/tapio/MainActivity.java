package be.howest.nmct.tapio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.tapio.model.LandmarkDetailed;
import be.howest.nmct.tapio.model.LandmarkList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static LandmarkList landmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retroFitInit();
    }

    private void retroFitInit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://back-end-tapio.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IBackEnd service = retrofit.create(IBackEnd.class);

        Call<LandmarkList> call = service.getAllLandMarks();

        call.enqueue(new Callback<LandmarkList>() {
            @Override
            public void onResponse(Call<LandmarkList> call, Response<LandmarkList> response) {
                landmarks = response.body();
                Intent details = new Intent(MainActivity.this, LandmarkDetails.class);
                startActivity(details);
            }

            @Override
            public void onFailure(Call<LandmarkList> call, Throwable t) {
                Log.d("Response", "Failed: " + t.getMessage());
            }
        });
    }
}
