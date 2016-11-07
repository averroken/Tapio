package be.howest.nmct.tapio.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_landmark_detail, container, false);
        binding.setLandmark(landmark);
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
