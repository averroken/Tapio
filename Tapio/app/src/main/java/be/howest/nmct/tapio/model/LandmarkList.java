package be.howest.nmct.tapio.model;

import java.util.List;

/**
 * Created by brianmasschaele on 4/11/16.
 */

public class LandmarkList {

    private List<LandmarkDetailed> landmarks;

    public List<LandmarkDetailed> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<LandmarkDetailed> landmarks) {
        this.landmarks = landmarks;
    }
}
