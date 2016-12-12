package be.howest.nmct.addlandmarktest.models;

import java.util.HashMap;
import java.util.Map;

public class LandmarkPostResponse {

    private String awnser;
    private String message;

    public LandmarkPostResponse(String anwser, String message) {
        this.awnser = anwser;
        this.message = message;
    }

    public String getAwnser() {
        return awnser;
    }

    public void setAwnser(String awnser) {
        this.awnser = awnser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}