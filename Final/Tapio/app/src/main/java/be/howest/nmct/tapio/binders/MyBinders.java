package be.howest.nmct.tapio.binders;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import be.howest.nmct.tapio.adapter.LandmarksAdapter;
import be.howest.nmct.tapio.model.LandmarksList;



public class MyBinders
{
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, LandmarksList landmarksList) {
        if (landmarksList != null) {
                LandmarksAdapter adapter = new LandmarksAdapter(landmarksList, recyclerView.getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
    }

}
