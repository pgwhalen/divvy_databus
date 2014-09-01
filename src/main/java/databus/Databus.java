package databus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import databus.entities.Station;
import databus.entities.StationSnapshot;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

    ObjectMapper objectMapper = new ObjectMapper();
    Date now;

    public Databus() {
    }

    @Override
    public void run() {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();

        InputStream stream = new URL("http://divvybikes.com/stations/json").openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        now = new Date();

        JsonNode json = objectMapper.readValue(stream, JsonNode.class);
        JsonNode stationsJson = json.get("stationBeanList");

        List<Station> allStations = addNewStations(stationsJson);
        createSnapshots(stationsJson);


        List<StationSnapshot> stationSnapshotList = new ArrayList<StationSnapshot>();
        for (JsonNode station : stationsJson) {
            stationSnapshotList.add(objectMapper.readValue(station.toString(), StationSnapshot.class));
        }

        int size = stationsJson.size();
        System.out.println("size: " + size);
    }

    public List<Station> addNewStations(JsonNode stationsResponse) throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Station> existingStations = session.createQuery(" from Station ").list();

        System.out.println("existing: " + existingStations.size());

        List<Integer> ids = new ArrayList<>();
        for (Station existingStation : existingStations) {
            ids.add(existingStation.getId());
            System.out.println("station: " + existingStation.getId());
        }

        List<Station> allStations = new ArrayList<>();
        int count = 0;
        for (JsonNode stationJson : stationsResponse) {
            Station newStation = objectMapper.readValue(stationJson.toString(), Station.class);
            allStations.add(newStation);

            if (!ids.contains(newStation.getId())) {
                System.out.print(" New Id" + newStation.getId());
                session.save(newStation);
                count++;
            }
        }
        session.getTransaction().commit();

        System.out.println("");
        System.out.println("added " + count + " stations");

        return allStations;
    }

    private void createSnapshots(JsonNode stationsResponse) throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (JsonNode stationSnapshotJson : stationsResponse) {
            StationSnapshot newStationSnapshot = objectMapper.readValue(stationSnapshotJson.toString(), StationSnapshot.class);
            newStationSnapshot.setTimestamp(now);
            session.save(newStationSnapshot);
        }

        session.getTransaction().commit();
    }

}

