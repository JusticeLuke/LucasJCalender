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
    private String time;
    private String date;

    public Appointment(Customer customer,String time, String date){
        this.customer = customer;
        this.date = date;
        this.time = time;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setDate(String date){
        this.date = date;
    }


    public Customer getCustomer(){
        return customer;
    }

    public String getTime(){
        return time;
    }

    public String getDate(){
        return date;
    }
}
