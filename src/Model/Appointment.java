/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.scheduleController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Connect;

/**
 *
 * @author Lucas
 */
public class Appointment {
    private Customer customer;
    private int appointmentId;
    private String type;
    private String user;    
    private String start;
    private String end;
    private String year;
    private String month;
    private String day;
    private int startHour;
    private int endHour;
    
    
    public Appointment(Customer customer, int appointmentId, String type, String user, String start, String end, String year, String month, String day, boolean newAppointment) throws SQLException{
        this.customer = customer;
        this.appointmentId = appointmentId;
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

    public boolean addAppointmentToDatabase() throws SQLException{
        //Formated start and end time to work with database
        String startTime = year+"-"+month+"-"+day+" "+start+":00:00";
        String endTime = year+"-"+month+"-"+day+" "+end+":00:00";

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

        Main.addToAppointmentList(this);
        Main.updateTable();
        return true;
    }

    public boolean updateAppointment(String type, String user, String start, String end, String year, String month, String day) throws SQLException{
        String startTime = year+"-"+month+"-"+day+" "+start+":00:00";
        String endTime = year+"-"+month+"-"+day+" "+end+":00:00";

        Connection conn = Connect.getConnection();
        String updateStatement = "UPDATE appointment SET type = ?, start = ?, end = ?, lastUpdate = NOW(), lastUpdateBy = ?) "
                + "WHERE appointmentId = ?; "; //Insert customerId(1) value

        PreparedStatement ps = conn.prepareStatement(updateStatement);
        ps.setString(1,type);
        ps.setString(2,startTime);
        ps.setString(3,endTime);
        ps.setString(4,Main.getUserName());

        ps.execute();
        
        this.type = type;
        this.start = start;
        this.end = end;
        this.year = year;
        this.month = month;
        this.day = day;
        
        Main.updateTable();
        
        return true;
    }
    
    public boolean removeAppointment(){
        try {
            Connection conn = Connect.getConnection();
            
            String updateStatement = "DELETE FROM appointment WHERE appointmentId = " + appointmentId;//Insert customerName(6) value
            
            PreparedStatement ps = conn.prepareStatement(updateStatement);
            ps.execute();
            
            Main.removeAppointment(this);
            customer.removeAppointment(this);
            Main.updateTable();

            return true;
            
        }catch (SQLException e) {
            Logger.getLogger(scheduleController.class.getName()).log(Level.SEVERE, null, e);
        }
        return true;
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

    public void setMonth(String month){
        this.month = month;
    }

    public void setYear(String year){
        this.year = year;
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

    public String getMonth(){
        return month;
    }

    public String getYear(){
        return year;
    }    
    
}
