package device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Device {

    private static String username;
    private static String password;
    private static String encodedString;
    
    
    private static boolean isLogged() {
        if(username != null && password != null) {
            return true;
        } 
        System.out.println("Please log in first!");
        return false;
    }
    
    private static void login() {
        Scanner inp = new Scanner(System.in);
        System.out.println("Enter username:");
        username = inp.nextLine();
        System.out.println("Enter password:");
        password = inp.nextLine();
        String prebase = username + ":" + password;
        encodedString = Base64.getEncoder().encodeToString(prebase.getBytes());
    }
    
    public static void logout() {
        username = password = null;
    }
    
    public static void playSong() {
        try {
            if(!isLogged()) return;
            System.out.println("Enter song name: ");
            Scanner inp = new Scanner(System.in);
            String songName = inp.nextLine();
            
            
            URL url = new URL("http://localhost:8080/Service/api/music/play");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Basic " + encodedString);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("songName",songName);
            conn.setRequestMethod("POST");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String output;
            
            StringBuilder response = new StringBuilder();
            while((output = in.readLine()) != null){
                response.append(output);
            }
            
            in.close();
            System.out.println("Response: " + response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public static void getSongs() {
        
        try {
            if(!isLogged()) return;
            
            URL recurl = new URL("http://localhost:8080/Service/api/music/reqHistory");
            HttpURLConnection recconn = (HttpURLConnection) recurl.openConnection();
            recconn.setDoOutput(true);
            recconn.setDoInput(true);
            recconn.setUseCaches(false);
            recconn.setRequestProperty("Authorization", "Basic " + encodedString);
            recconn.setRequestProperty("Content-Type", "application/json");
            recconn.setRequestMethod("GET");
            
            BufferedReader recin = new BufferedReader(new InputStreamReader(recconn.getInputStream()));
            
            String recoutput;
            
            StringBuilder recresponse = new StringBuilder();
            while((recoutput = recin.readLine()) != null){
                recresponse.append(recoutput);
            }
            
            recin.close();
            
            URL url = new URL("http://localhost:8080/Service/api/music/recHistory");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Authorization", "Basic " + encodedString);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String output;
            
            StringBuilder response = new StringBuilder();
            while((output = in.readLine()) != null){
                response.append(output);
            }
            
            in.close();
            System.out.println("Response: " + response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void createTask() {
        try {
            if(!isLogged()) return;
            
            System.out.println("Enter destination: ");
            Scanner inp = new Scanner(System.in);
            String destination = inp.nextLine();
            System.out.println("Enter duration in hours: ");
            String duration = inp.nextLine();
            
            
            
            URL url = new URL("http://localhost:8080/Service/api/plan/create");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Basic " + encodedString);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("destination",destination);
            conn.setRequestProperty("duration", duration);
            
            conn.setRequestMethod("POST");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String output;
            
            StringBuilder response = new StringBuilder();
            while((output = in.readLine()) != null){
                response.append(output);
            }
            
            in.close();
            System.out.println("Response: " + response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void getTasks() {
        try {
            if(!isLogged()) return;
            
            URL recurl = new URL("http://localhost:8080/Service/api/plan/reqTasks");
            HttpURLConnection recconn = (HttpURLConnection) recurl.openConnection();
            recconn.setDoOutput(true);
            recconn.setDoInput(true);
            recconn.setUseCaches(false);
            recconn.setRequestProperty("Authorization", "Basic " + encodedString);
            recconn.setRequestProperty("Content-Type", "application/json");
            recconn.setRequestMethod("GET");
            
            BufferedReader inrec = new BufferedReader(new InputStreamReader(recconn.getInputStream()));
            
            String output2;
            
            StringBuilder response2 = new StringBuilder();
            while((output2 = inrec.readLine()) != null){
                response2.append(output2);
            }
            
            inrec.close();
            System.out.println("Response: " + response2.toString());
            
            URL url = new URL("http://localhost:8080/Service/api/plan/recTasks");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Basic " + encodedString);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String output;
            
            StringBuilder response = new StringBuilder();
            while((output = in.readLine()) != null){
                response.append(output);
            }
            
            in.close();
            System.out.println("Response: " + response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteTask() {
        try {
            if(!isLogged()) return;
            System.out.println("Enter id of task (or 0 for quitting)");
            Scanner inp = new Scanner(System.in);
            int taskId = inp.nextInt();
            if(taskId == 0 ) return;
            
            
            URL url = new URL("http://localhost:8080/Service/api/plan/delete/" + taskId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Basic " + encodedString);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String output;
            
            StringBuilder response = new StringBuilder();
            while((output = in.readLine()) != null){
                response.append(output);
            }
            
            in.close();
            System.out.println("Response: " + response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void createAlarm() {
        try {
            if(!isLogged()) return;
            
            System.out.println("Enter song of alarm: ");
            Scanner inp = new Scanner(System.in);
            String song = inp.nextLine();
            System.out.println("Enter how many hours from now: ");
            String time = inp.nextLine();
            
            
            URL url = new URL("http://localhost:8080/Service/api/alarm");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Basic " + encodedString);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("alarmSong",song);
            conn.setRequestProperty("alarmTime", time);
            
            conn.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String output;
            
            StringBuilder response = new StringBuilder();
            while((output = in.readLine()) != null){
                response.append(output);
            }
            
            in.close();
            System.out.println("Response: " + response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

        
    public static void main(String[] args) {
        System.out.println("=== User device ===");
        System.out.println("Device started...");
        
        while(true) {
            
            try {
                System.out.println("Menu: " + 
                        "\n 1) Login \n 2) Logout \n 3) Play song \n 4) Show song history"
                        + "\n 5) Create task \n 6) Show my tasks \n 7) Delete task"
                        + "\n 8) Create alarm \n 0) Exit");
                Scanner inp = new Scanner(System.in);
                String s = inp.nextLine();
                switch(s) {
                    case "1":
                        login();
                        break;
                    case "2":
                        logout();
                        break;
                    case "3":
                        if(!isLogged()) {
                            System.out.println("Please log in first!");
                            break;
                        }
                        playSong();
                        break;
                    case "4":
                        getSongs();
                        break;
                    case "5":
                        createTask();
                        break;
                    case "6":
                        getTasks();
                        break;
                    case "7":
                        deleteTask();
                        break;
                    case "8":
                        createAlarm();
                        break;
                    case "n":
                        // Testing part
                        if(!isLogged()) {
                            System.out.println("Please log in first!");
                            break;
                        }
                        URL url = new URL("http://localhost:8080/Service/api/test");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("Authorization", "Basic " + encodedString);
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestMethod("GET");
                        
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        
                        String output;
                        
                        StringBuilder response = new StringBuilder();
                        while((output = in.readLine()) != null){
                            response.append(output);
                        }
                        
                        in.close();
                        System.out.println("Response: " + response.toString());
                        
                        break;
                        
                    case "0":
                        return;
                        
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }
    
}
