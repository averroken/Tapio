package be.howest.nmct.tapio.view;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import be.howest.nmct.tapio.R;
import be.howest.nmct.tapio.adapter.LandmarksAdapter;

import static be.howest.nmct.tapio.BR.landmarks;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new LandmarksListFragment())
                    .commit();
        }

    }

}
