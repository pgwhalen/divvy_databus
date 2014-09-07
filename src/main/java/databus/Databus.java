package databus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import databus.entities.Station;
import databus.entities.StationSnapshot;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by paul on 5/5/14.
 */
public class Databus implements Runnable {

    private SessionFactory sessionFactory;
    private static Logger log = LoggerFactory.getLogger(Databus.class);

    ObjectMapper objectMapper = new ObjectMapper();
    Date now;

    @Override
    public void run() {
        try {
            start();
        } catch (Exception e) {
            log.error("Failure to load snapshots.");
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        InputStream stream = new URL("http://divvybikes.com/stations/json").openStream();
        now = new Date();
        log.info("Station data queried at " + now);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));


        JsonNode json = objectMapper.readValue(stream, JsonNode.class);
        JsonNode stationsJson = json.get("stationBeanList");

        addNewStations(stationsJson);
        createSnapshots(stationsJson);
    }

    public List<Station> addNewStations(JsonNode stationsResponse) throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Station> existingStations = session.createQuery(" from Station ").list();


        List<Integer> ids = new ArrayList<>();
        for (Station existingStation : existingStations) {
            ids.add(existingStation.getId());
        }

        List<Station> allStations = new ArrayList<>();
        int count = 0;
        for (JsonNode stationJson : stationsResponse) {
            Station newStation = objectMapper.readValue(stationJson.toString(), Station.class);
            allStations.add(newStation);

            if (!ids.contains(newStation.getId())) {
                session.save(newStation);
                count++;
                if (count % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
        }
        session.getTransaction().commit();

        if (count > 0) {
            log.info("Added " + count + " stations to the database.");
        }

        return allStations;
    }

    private void createSnapshots(JsonNode stationsResponse) throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        int count = 0;
        for (JsonNode stationSnapshotJson : stationsResponse) {
            StationSnapshot newStationSnapshot = objectMapper.readValue(stationSnapshotJson.toString(), StationSnapshot.class);
            newStationSnapshot.setTimestamp(now);
            session.save(newStationSnapshot);

            count++;
            if (count % 20 == 0) {
                session.flush();
                session.clear();
            }
        }
        log.info("Loaded snapshots for " + stationsResponse.size() + " stations.");

        session.getTransaction().commit();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}

