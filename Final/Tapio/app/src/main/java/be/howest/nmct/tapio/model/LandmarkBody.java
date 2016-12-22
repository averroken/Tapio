package be.howest.nmct.tapio.model;

public class LandmarkBody {

    final String Name;
    final String Type;
    final String Lat;
    final String Long;
    final String Country;
    final String ImageURLBig;
    final Boolean Public;
    final String Description;


    public LandmarkBody(String Name, String type, String lat, String aLong, String country, String ImageURLBig, Boolean aPublic, String description) {
        this.Name = Name;
        this.Type = type;
        this.Lat = lat;
        this.Long = aLong;
        this.Country = country;
        this.ImageURLBig = ImageURLBig;
        this.Public = aPublic;
        this.Description = description;
    }
}
