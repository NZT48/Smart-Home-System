package planner;

import entities.Task;
import java.util.Date;
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
import javax.persistence.Query;

public class Main {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup="plannerQueue")
    private static Queue plannerQueue;
    
    @Resource(lookup="plannerReturnQueue")
    private static Queue plannerReturnQueue;
    
    
    private static void createTask(TextMessage txtmsg) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PlannerPU");
        EntityManager em = emf.createEntityManager();

        try {
            EntityTransaction transaction = em.getTransaction();
            
            Task newTask = new Task();
            
            newTask.setStartDatetime(new Date(txtmsg.getLongProperty("starttime")));
            newTask.setDuration(txtmsg.getIntProperty("duration"));
            newTask.setDestination(txtmsg.getStringProperty("destination"));
            newTask.setAlarmId(32);
            newTask.setUserId(txtmsg.getIntProperty("userId"));
     
            transaction.begin();
            
            em.persist(newTask);
            
            transaction.commit();

        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            
            emf.close();
        }

    }
    
    private static void deleteTask(TextMessage txtmsg){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PlannerPU");
        EntityManager em = emf.createEntityManager();
        
        try {
            int taskId = txtmsg.getIntProperty("taskId");
            int userId = txtmsg.getIntProperty("userId");
            
            System.out.println("Deleting task with id " + taskId);
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();
            
            Task t = em.find(Task.class, taskId);
            System.out.println(t.getDestination());
            
            em.remove(t);
            transaction.commit();
                               
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            
            emf.close();
        }
        
    }
    
    private static void getTasks(int userId) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PlannerPU");
        EntityManager em = emf.createEntityManager();
        
        System.out.println("Tasks for user with Id: " + userId);
        List<Task> tasks;
        tasks = em.createNamedQuery("Task.findByUserId", Task.class).setParameter("userId",userId).getResultList();
        
        StringBuilder sb = new StringBuilder();
        for(Task t: tasks){ 
            String taskStr = ("Task" + t.getTaskId() + "! Destination: " + t.getDestination() 
                    + " ,Datetime: " + t.getStartDatetime().toString() 
                    + " , Alarm id: " + t.getAlarmId() + ";");
            System.out.println(taskStr);
            sb.append(taskStr);
        }
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        System.out.println("Created producer");
        TextMessage taskMsg = context.createTextMessage();
        try {
            taskMsg.setText(sb.toString());
            System.out.println("Sending " + taskMsg.getText());
            producer.send(plannerReturnQueue, taskMsg);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        System.out.println("Started task planner");  
        
        try {
       
            JMSContext context = connectionFactory.createContext();
            JMSConsumer consumer = context.createConsumer(plannerQueue);
            while(true) {
                Message msg = consumer.receive();
                if(msg instanceof TextMessage){
                    TextMessage txtmsg = (TextMessage) msg;
                    System.out.println("New msg arrived " + txtmsg.getText());
                    switch(txtmsg.getStringProperty("command")) {
                        case "get":
                            getTasks(txtmsg.getIntProperty("userId"));
                            break;
                        case "create":
                            createTask(txtmsg);
                            break;
                        case "delete":
                            deleteTask(txtmsg);
                            break;
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Ende");
    }
    
    public static int distanceCalculator(String from, String to) {
        return 0;
    }
    
}
