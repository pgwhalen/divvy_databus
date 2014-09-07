package databus;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Created by paul on 7/8/14.
 */
public class Main {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int QUERY_FREQUENCY = 1; // in minutes
    public static SessionFactory sessionFactory;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");


        Databus databus = new Databus();
        databus.setSessionFactory(sessionFactory);
        // Schedule for the beginning of every minute
        int delay = secondsUntilNextMinute();
        System.out.println("until next min " + delay);
        scheduler.scheduleAtFixedRate(databus, secondsUntilNextMinute(), QUERY_FREQUENCY * 60, TimeUnit.SECONDS);
    }

    /**
     * @return seconds until the top of the next minute
     */
    private static int secondsUntilNextMinute() {
        return (60 - Calendar.getInstance().get(Calendar.SECOND));
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
