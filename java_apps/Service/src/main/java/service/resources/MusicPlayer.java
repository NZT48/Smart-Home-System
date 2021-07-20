/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.resources;

import entities.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author hp
 */
@Path("music")
@Stateless
public class MusicPlayer {
    
    @PersistenceContext(unitName="my_persistence_unit")
    EntityManager em;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    ConnectionFactory connectionFactory;
    
    @Resource(lookup="musicQueue")
    Queue musicQueue;
    
    @Resource(lookup="musicReturnQueue")
    Queue musicReturnQueue;
    
    private int getUserIdFromAuthHeader(@Context HttpHeaders httpHeaders) {
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        
        if(authHeaderValues != null && authHeaderValues.size()>0){
            
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(
                    Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")),
                    StandardCharsets.UTF_8);
            StringTokenizer strTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = strTokenizer.nextToken();
            
            User user = em.createNamedQuery("User.findByUsername", User.class)
                    .setParameter("username", username).getSingleResult();
            
            return user.getUserId();
            
        } else
            return -1;
    }
    
    @GET
    @Path("reqHistory")
    public Response getSongs(@Context HttpHeaders httpHeaders) {
        
        int userId = getUserIdFromAuthHeader(httpHeaders);
        String songs = "No songs";

        try {
            
            JMSContext context = connectionFactory.createContext();
            JMSProducer producer = context.createProducer();
            
            
            TextMessage musicMsg = context.createTextMessage();
            musicMsg.setText("text");
            musicMsg.setStringProperty("command", "get songs");
            musicMsg.setIntProperty("userId", userId);
            producer.send(musicQueue, musicMsg);
            

        } catch (JMSException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok("Request poslat").build();
    }
        
    @GET
    @Path("recHistory")
    public Response recieveSongs(@Context HttpHeaders httpHeaders) {
        
        int userId = getUserIdFromAuthHeader(httpHeaders);
        String songs = "No songs";

        try {
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(musicReturnQueue);

            Message msg = consumer.receive();
            
            if(msg instanceof TextMessage){
                    TextMessage txtmsg = (TextMessage) msg;
                    songs = txtmsg.getText();
            }
            
            return Response.ok(songs).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok("Drugo respons").build();
    }
    
    
    @POST
    @Path("play")
    @Consumes("application/x-www-form-urlencoded")
    public Response playSong(@Context HttpHeaders httpHeaders, @FormParam("songName") String songName) {
        
        int userId = getUserIdFromAuthHeader(httpHeaders);
        
        try {
            
            JMSContext context = connectionFactory.createContext();
            JMSProducer producer = context.createProducer();
            
            TextMessage musicMsg = context.createTextMessage();
            musicMsg.setText(songName);
            musicMsg.setStringProperty("command", "play");
            musicMsg.setIntProperty("userId", userId);
            producer.send(musicQueue, musicMsg);
            
            
        } catch (JMSException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok("Pesma pustena").build();
    }
}