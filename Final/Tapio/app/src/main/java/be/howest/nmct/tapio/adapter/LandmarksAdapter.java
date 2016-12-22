package be.howest.nmct.tapio.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import be.howest.nmct.tapio.R;
import be.howest.nmct.tapio.databinding.RowLandmarksBinding;
import be.howest.nmct.tapio.model.Landmarks;
import be.howest.nmct.tapio.model.LandmarksList;
import be.howest.nmct.tapio.view.LandmarkDetailActivity;
import be.howest.nmct.tapio.view.MainActivity;

public class LandmarksAdapter extends RecyclerView.Adapter<LandmarksAdapter.Viewholder> {

    private LandmarksList landmarksList = null;
    private Context context;
    private ImageView imageViewLandmark;


    public LandmarksAdapter(LandmarksList landmarksList, Context context) {
        this.landmarksList = landmarksList;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowLandmarksBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_landmarks, parent, false);
        LandmarksAdapter.Viewholder vh = new LandmarksAdapter.Viewholder(binding);
        imageViewLandmark = vh.getBinding().imageViewLandmark;

        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Landmarks landmarks= landmarksList.getLandmarks().get(position);
        holder.getBinding().setLandmarks(landmarks);
        holder.getBinding().executePendingBindings();
        Picasso.with(context).load(landmarks.getImageURLBig()).into(imageViewLandmark);
    }

    @Override
    public int getItemCount() {
        return landmarksList.getLandmarks().size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final RowLandmarksBinding binding;

        public Viewholder(RowLandmarksBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public RowLandmarksBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View v) {
            Landmarks landmarks = landmarksList.getLandmarks().get(getAdapterPosition());
            detailedLandmarks(landmarks);
        }

        public void detailedLandmarks(Landmarks  landmarks){
            /*
            Fragment newFragment = new LandmarkDetailFragment();
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            bundle.putSerializable("landmark", gson.toJson(landmarks));
            newFragment.setArguments(bundle);

            FragmentTransaction transaction = null;
            transaction = ((Activity) context).getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, newFragment); //id of ViewPager
            transaction.addToBackStack(null);
            transaction.commit();
            */

            Intent intent = new Intent(context, LandmarkDetailActivity.class);
            Gson gson = new Gson();
            intent.putExtra("landmark", gson.toJson(landmarks));
            context.startActivity(intent);
        }
    }
}
