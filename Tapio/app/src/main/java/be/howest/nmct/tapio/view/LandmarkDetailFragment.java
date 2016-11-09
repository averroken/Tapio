package be.howest.nmct.tapio.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import be.howest.nmct.tapio.R;
import be.howest.nmct.tapio.databinding.FragmentLandmarkDetailBinding;
import be.howest.nmct.tapio.model.Landmarks;
import be.howest.nmct.tapio.viewmodel.LandmarksListFragmentViewModel;


public class LandmarkDetailFragment extends Fragment {
    private Landmarks landmark;
    private FragmentLandmarkDetailBinding binding;


    public LandmarkDetailFragment() {
        // Required empty public constructor
    }

    public static LandmarkDetailFragment newInstance() {
        LandmarkDetailFragment fragment = new LandmarkDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);Bundle bundle= this.getArguments();
        if(bundle!= null){
            Gson gson = new Gson();
            String landmarkSerialized = bundle.getSerializable("landmark").toString();
            landmark= gson.fromJson(landmarkSerialized, Landmarks.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Context context = getActivity().getApplicationContext();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_landmark_detail, container, false);
        binding.setLandmark(landmark);
        ImageView imageView = binding.DetailedLandmarkImage;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int height = 0;

        Picasso.with(context).load(landmark.getImageURLBig()).resize(displayWidth, height).into(imageView);
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
