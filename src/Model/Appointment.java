/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.Connect;

/**
 *
 * @author Lucas
 */
public class Appointment {
    private Customer customer;
    private String type;
    private String user;    
    private String start;
    private String end;
    private String year;
    private String month;
    private String day;
    private int startHour;
    private int endHour;
    
    
    public Appointment(Customer customer,String type, String user, String start, String end, String year, String month, String day, boolean newAppointment) throws SQLException{
        this.customer = customer;
        this.type = type;
        this.user = user;        
        this.start = start;
        this.end = end;
        this.year = year;
        this.month = month;
        this.day = day;
        
        startHour = Integer.parseInt(start);
        endHour = Integer.parseInt(end);
        
        if(newAppointment)
            addAppointmentToDatabase();
    }    
    

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setStart(String start){
        this.start = start;
    }

    public void setEnd(String end){
        this.end = end;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Customer getCustomer(){
        return customer;
    }

    public String getType(){
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getDay() {
        return day;
    }

    private boolean addAppointmentToDatabase() throws SQLException{
        //Formated start and end time to work with database
        String startTime = year+"-"+month+"-"+day+" "+start+":00:00";
        String endTime = year+"-"+month+"-"+day+" "+end+":00:00";
        System.out.println(startTime);
        System.out.println(endTime);
        Connection conn = Connect.getConnection();//Get Connection
            
        String insertStatement = "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "VALUES (?, ?, '', '', '', '', ?, '', ?, ?, now(), 'admin', now(), 'admin'); ";//Insert customerId(1) value          
                    
        PreparedStatement ps = conn.prepareStatement(insertStatement);
        ps.setInt(1,customer.getCustomerId());
        ps.setInt(2, Main.getUserId());
        ps.setString(3, type);
        ps.setString(4, startTime);
        ps.setString(5, endTime);
        ps.execute();
        return true;
    }
    
    
}
