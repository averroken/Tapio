package be.howest.nmct.tapio.model;

import android.databinding.BaseObservable;


public class Landmarks extends BaseObservable{

    /*
    "_id": "581a3e83dcba0f285b2d7cd1",
    "Naam": "Mijn tweede Landmark",
    "Description": "Blablabla",
    "Type": "Natuur",
    "Afstand": 845,
    "Locatie": [
      {
        "lon": 54,
        "lat": 52
      }
    ],
    "ImageURLBig": "https://www.google.be/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiM_rr9m4jQAhVFaxQKHTmWASsQjRwIBw&url=https%3A%2F%2Fplay.goog.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dde.lotumapps.vibes&psig=AFQjCNGUoaqgYXl2i0s9isnDCpTZaaBt1g&ust=1478112412918875",
    "Visits": 28,
    "Likes": 35
     */

    private String _id;
    private String Name;
    private String Description;
    //    private LocationObject Locatie;
    private String ImageURLBig;
    private Integer Visits;
    private Integer Likes;
    private String Type;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

//    public LocationObject getLocatie() {
//        return Locatie;
//    }
//
//    public void setLocatie(LocationObject locatie) {
//        Locatie = locatie;
//    }

    public String getImageURLBig() {
        return ImageURLBig;
    }

    public void setImageURLBig(String imageURLBig) {
        ImageURLBig = imageURLBig;
    }

    public Integer getVisits() {
        return Visits;
    }

    public void setVisits(Integer visits) {
        Visits = visits;
    }

    public Integer getLikes() {
        return Likes;
    }

    public void setLikes(Integer likes) {
        Likes = likes;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}

