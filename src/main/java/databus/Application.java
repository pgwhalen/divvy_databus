package databus;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;

import java.util.concurrent.*;

/**
 * Created by paul on 7/8/14.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new databus.Databus(), 0, 1, TimeUnit.MINUTES);
    }
}
