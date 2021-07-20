package alarm;

import entities.Alarm;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import java.util.Timer;
import java.util.TimerTask;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup="alarmQueue")
    private static Queue alarmQueue;
    
    private static final List<Timer> Alarms = new ArrayList<Timer>();
    
    
    private static void setAlarm(TextMessage txtmsg){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AlarmPU");
        EntityManager em = emf.createEntityManager();
        try {
            
            
            Timer timer = new Timer(true);
            Alarms.add(timer);
            if(txtmsg.getBooleanProperty("Repetative")) {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("Repetative alarm: " +  txtmsg.getText());
                        } catch (JMSException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }, 1000, 3);
            } else {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(txtmsg.getText());
                        } catch (JMSException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }, new Date(txtmsg.getLongProperty("alarmTime")));
            }
            
            EntityTransaction transaction = em.getTransaction();
            
            Alarm newAlarm = new Alarm();
            newAlarm.setAlarmTime(new Date(txtmsg.getLongProperty("alarmTime")));
            newAlarm.setUserId(txtmsg.getIntProperty("userId"));
            newAlarm.setSongOfAlarm(txtmsg.getStringProperty("alarmSong"));
            
            transaction.begin();
            
            em.persist(newAlarm);
            
            transaction.commit();
            
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            
            emf.close();
        }
    }
    
    private static void stopAlarm(TextMessage txtmsg){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AlarmPU");
        EntityManager em = emf.createEntityManager();
        
        for(Timer timer : Alarms) {
            timer.cancel();
        }
        
        Alarms.clear();
        
    }
    
    

    public static void main(String[] args) {
        
        System.out.println("Started alarm");

        try {
       
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(alarmQueue);
            while(true) {
                Message msg = consumer.receive();
                if(msg instanceof TextMessage){
                    
                    System.out.println("Got the message");
                    TextMessage txtmsg = (TextMessage) msg;
                    System.out.println(txtmsg.getBooleanProperty("Repetative"));
                    System.out.println(txtmsg.getText());
                
                    switch(txtmsg.getStringProperty("command")) {
                        case "set":
                            setAlarm(txtmsg);
                            break;
                        case "stop":
                            stopAlarm(txtmsg);
                            break;
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
