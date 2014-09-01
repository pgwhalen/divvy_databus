package databus.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.SessionFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * Created by paul on 7/13/14.
 */
@Entity
@Table(name = "stations")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {

    private int id;
    private String stationName;
    private double latitude;
    private double longitude;
    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String postalCode;
    private String location;
    private String altitude;
    private int landMark;

    public Station() {
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "station_name")
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Column(name = "latitude")
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Column(name = "longitude")
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Column(name = "street_address_1")
    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    @Column(name = "street_address_2")
    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "postal_code")
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "altitude")
    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    @Column(name = "landmark")
    public int getLandMark() {
        return landMark;
    }

    public void setLandMark(int landMark) {
        this.landMark = landMark;
    }

}
