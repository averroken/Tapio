package be.howest.nmct.tapio.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.tapio.R;
import be.howest.nmct.tapio.databinding.FragmentLandmarksListBinding;
import be.howest.nmct.tapio.viewmodel.LandmarksListFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class LandmarksListFragment extends Fragment {

    private FragmentLandmarksListBinding binding;

    private LandmarksListFragmentViewModel landmarksListFragmentViewModel;

    public LandmarksListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_landmarks_list, container, false);
        binding.landmarksRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.landmarksRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        landmarksListFragmentViewModel = new LandmarksListFragmentViewModel(binding, getContext());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        landmarksListFragmentViewModel.loadLandmarks();
    }
}
