/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    private String day;

    public Appointment(Customer customer,String type, String user, String start, String end, String day){
        this.customer = customer;
        this.type = type;
        this.user = user;        
        this.start = start;
        this.end = end;
        this.day = day;
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

    public boolean addAppointmentToDatabase(){
        return true;
    }
}
