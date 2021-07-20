/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.resources;


import entities.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author hp
 */
@Path("plan")
@Stateless
public class Planner {
    
    @PersistenceContext(unitName="my_persistence_unit")
    EntityManager em;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    ConnectionFactory connectionFactory;
    
    @Resource(lookup="plannerQueue")
    Queue plannerQueue;
    
    @Resource(lookup="plannerReturnQueue")
    Queue plannerReturnQueue;
    
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
    @Path("create")
    @Consumes("application/x-www-form-urlencoded")
    public Response playSong(@Context HttpHeaders httpHeaders, 
            @FormParam("starttime") long starttime,
            @FormParam("destination") String destination,
            @FormParam("duration") int duration
    ) {
        
        int userId = getUserIdFromAuthHeader(httpHeaders);
        
        try {
            
            JMSContext context = connectionFactory.createContext();
            JMSProducer producer = context.createProducer();
            
            TextMessage taskMsg = context.createTextMessage();
            taskMsg.setStringProperty("command", "create");
            taskMsg.setLongProperty("starttime", starttime);
            taskMsg.setStringProperty("destination", destination);
            taskMsg.setIntProperty("userId", userId);
            taskMsg.setIntProperty("duration", duration);
            producer.send(plannerQueue, taskMsg);
            
            
        } catch (JMSException ex) {
            Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok("Zahtevano je kreiranje taska").build();
    }
    
    @DELETE
    @Path("delete/{tid}")
    public Response deleteTask(@Context HttpHeaders httpHeaders, @PathParam("tid") int taskId) {
        int userId = getUserIdFromAuthHeader(httpHeaders);
        
        try {

            JMSContext context = connectionFactory.createContext();
            JMSProducer producer = context.createProducer();
            
            TextMessage planMsg = context.createTextMessage();

            planMsg.setIntProperty("userId", userId);
            planMsg.setIntProperty("taskId", taskId);
            planMsg.setStringProperty("command", "delete");            
            
            producer.send(plannerQueue, planMsg);
            
            return Response.ok("zahtevano brisanje taska").build();
            
        } catch (JMSException ex) {
            
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok("not deleted").build();

    }
    
    @GET
    @Path("reqTasks")
    public Response requestTasks(@Context HttpHeaders httpHeaders) {
        
        int userId = getUserIdFromAuthHeader(httpHeaders);
        
        try {

            JMSContext context = connectionFactory.createContext();
            JMSProducer producer = context.createProducer();

            TextMessage planMsg = context.createTextMessage();

            planMsg.setIntProperty("userId", userId);
            planMsg.setStringProperty("command", "get");            
            
            producer.send(plannerQueue, planMsg);
            
            return Response.ok("planner je ok").build();
            
        } catch (JMSException ex) {
            
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok("planner nije ok").build();
    }
    
    @GET
    @Path("recTasks")
    public Response recieveTasks(@Context HttpHeaders httpHeaders) {

        int userId = getUserIdFromAuthHeader(httpHeaders);
        String tasks = "";

        try {
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(plannerReturnQueue);

            Message msg = consumer.receive();
            
            if(msg instanceof TextMessage){
                    TextMessage txtmsg = (TextMessage) msg;
                    tasks = txtmsg.getText();
            }
            
            return Response.ok(tasks).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     
        return Response.ok("Recived error").build();
    }
}
