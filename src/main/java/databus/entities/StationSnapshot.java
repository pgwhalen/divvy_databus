package databus.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by paul on 6/22/14.
 */
@Entity
@Table(name = "station_snapshots")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationSnapshot implements Serializable {

    private short id;
    private Date timestamp;
    private short availableDocks;
    private short availableBikes;

    @Id
    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Id
    @Column(name = "timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "available_docks")
    public short getAvailableDocks() {
        return availableDocks;
    }

    public void setAvailableDocks(byte availableDocks) {
        this.availableDocks = availableDocks;
    }

    @Column(name = "available_bikes")
    public short getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(byte availableBikes) {
        this.availableBikes = availableBikes;
    }

}
