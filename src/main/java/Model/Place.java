package Model;

/**
 * Created by taniya on 28-07-2017.
 */

public class Place {
    private float lon;
    private float lat;
    private long sunset;
    private long sunrise;
    private String country;
    private String city;
    private long lastpdate;
    private long sys_id;

    public long getSys_id() {
        return sys_id;
    }

    public void setSys_id(long sys_id) {
        this.sys_id = sys_id;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getLastpdate() {
        return lastpdate;
    }

    public void setLastpdate(long lastpdate) {
        this.lastpdate = lastpdate;
    }
}
