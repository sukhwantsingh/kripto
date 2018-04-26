package braille.kofefe.app.modules_.profile.model;

/**
 * Created by Snow-Dell-05 on 12/27/2017.
 */

public class ModelLocation {
    private String city;
    private String state;
    private String country;
    private double latitude;
    private double longitude;

    public ModelLocation(String city, String state, String country, double latitude, double longitude) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

