/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.resources;

import entities.User;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
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
@Path("alarm")
@Stateless
public class Alarm {
    
    @PersistenceContext(unitName="my_persistence_unit")
    EntityManager em;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    ConnectionFactory connectionFactory;
    
    @Resource(lookup="alarmQueue")
    Queue alarmQueue;
    
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
    
    @POST
    @Path("play")
    @Consumes("application/x-www-form-urlencoded")
    public Response getAlarm(@Context HttpHeaders httpHeaders, 
            @FormParam("alarmSong") String songName,
            @FormParam("alarmTime") String time) {
    
        
        int userId = getUserIdFromAuthHeader(httpHeaders);

        try {
            System.out.println("Started alarm endpoint");
            
            JMSContext context = connectionFactory.createContext();
            JMSProducer producer = context.createProducer();

            int hours = Integer.parseInt(time);
            TextMessage musicMsg = context.createTextMessage();
            musicMsg.setText("Alarm msg");
            musicMsg.setIntProperty("userId", userId);
            musicMsg.setStringProperty("command", "set");
            musicMsg.setBooleanProperty("Repetative", false);
            Date oldDate = new Date();
            Date newDate = new Date(oldDate.getTime() + hours * 60 *1000 * 60);
            musicMsg.setLongProperty("alarmTime",(newDate.getTime()));
            musicMsg.setStringProperty("alarmSong", songName);
            producer.send(alarmQueue, musicMsg);
            
            return Response.ok("alarm je setovan").build();
            
        } catch (JMSException ex) {
            
            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok("alarm nije ok").build();
    }
}
