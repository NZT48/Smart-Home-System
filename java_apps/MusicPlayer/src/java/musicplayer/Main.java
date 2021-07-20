package musicplayer;

import entities.Song;
import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
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
    
    @Resource(lookup="musicQueue")
    private static Queue musicQueue;
    
    @Resource(lookup="musicReturnQueue")
    private static Queue musicReturnQueue;
    
    private static void play(TextMessage txtmsg){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MusicPlayerPU");
        EntityManager em = emf.createEntityManager();
        
        try {
            String songName = txtmsg.getText();
            int userId = txtmsg.getIntProperty("userId");
            EntityTransaction transaction = em.getTransaction();
            
            Song newSong = new Song();
            newSong.setName(songName);
            newSong.setUserId(userId);
            
            transaction.begin();
            
            em.persist(newSong);
            
            transaction.commit();

            String urlString = "https://www.youtube.com/results?search_query=" + URLEncoder.encode(songName, StandardCharsets.UTF_8.toString());
            Desktop.getDesktop().browse(new URL(urlString).toURI());
            System.out.println("Played song " + songName);
        } catch (MalformedURLException ex ) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException | IOException | JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            emf.close();
        }
    }
    
    private static void getSongs(int userId){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MusicPlayerPU");
        EntityManager em = emf.createEntityManager();
        System.out.println("Played songs for user with Id: " + userId);
        List<Song> songs;
        songs = em.createNamedQuery("Song.findByUserId", Song.class).setParameter("userId",userId).getResultList();
        
        StringBuilder sb = new StringBuilder();
        for(Song s: songs){ 
            System.out.println(s.getName());
            sb.append(s.getName() + ";");
        }
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        System.out.println("Created producer");
        TextMessage musicMsg = context.createTextMessage();
        try {
            musicMsg.setText(sb.toString());
            System.out.println("Sending " + musicMsg.getText());
            producer.send(musicReturnQueue, musicMsg);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
    
    public static void main(String[] args) {
        
        System.out.println("Started music player!");
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(musicQueue);
        
        try {

            while(true) {
                System.out.println("Waiting for new requests...");
                Message msg = consumer.receive();
                if(msg instanceof TextMessage){
                    TextMessage txtmsg = (TextMessage) msg;
                    switch(txtmsg.getStringProperty("command")) {
                        case "play":
                            play(txtmsg);
                            break;
                        case "get songs":
                            getSongs(txtmsg.getIntProperty("userId"));
                            break;
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
