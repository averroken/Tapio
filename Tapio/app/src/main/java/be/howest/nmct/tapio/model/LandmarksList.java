package be.howest.nmct.tapio.model;

import android.databinding.BaseObservable;


import java.util.List;


public class LandmarksList{
    private List<Landmarks> landmarks;

    public List<Landmarks> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<Landmarks> landmarks) {
        this.landmarks = landmarks;
    }
}
