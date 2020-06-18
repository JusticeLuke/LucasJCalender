/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.scheduleController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Connect;

/**
 *
 * @author Lucas
 */
public class Customer {
    
    private String name;//full name of customer
    private String phone;//phone number as a string
    private String address;//just the street number and name
    private String postalCode, city, country;
    private int customerId;//Unique id of customer used to locate record in the database
    
    
           
    public Customer(int customerId, String name, String phone, String address, String postalCode, String city, String country, boolean newCustomer){
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        
                
        
        Main.addToCustomerList(this);
        
        if(newCustomer){
            this.addCustomerToDatabase(country, city, address, phone, postalCode, name);
        }
    }
    
  
    
    
    //Set customer's name
    public void setName(String name){
        this.name = name;
    }
    
    //Set customer's phone number as a 7 digit integer
    public void setPhone(String phoneNum){
        this.phone = phoneNum;
    }
    
    //Set full address includes street address, postal code, city, and country
    public void setAddress(String address){
        this.address = address;
    }
    
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    
    public void setCity(String City){
        this.city = city;
    }
    
    public void setCountry(String country){
        this.country = country;
    }
    
    //Get customer's name
    public String getName(){
        return name;
    }

    //Get customer's phone number
    public String getPhone(){
        return phone;
    }
    
    //Get customer's address
    public String getAddress(){
        return address;
    }
    
    public String getPostalCode(){
        return postalCode;
    }
    
    public String getCity(){
        return city;
    }
    
    public String getCountry(){
        return country;
    }
    
    //Get unique customerId
    public int getCustomerId() {
        return customerId;
    }
    
    //Adds new customer to database. Returns false in it fails, and true if successful
    public boolean addCustomerToDatabase(String country, String city, String address, String phone, String postalCode, String customerName) {
       
        try {
            Connection conn = Connect.getConnection();//Get Connection
            
            String insertStatement = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, now(), 'admin', now(), 'admin'); ";//Insert country(1) value          
                    
            PreparedStatement ps = conn.prepareStatement(insertStatement);
            ps.setString(1, country);
            ps.execute();
            
            insertStatement = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES (?, LAST_INSERT_ID(), now(), 'admin', now(), 'admin'); ";//Insert city(2) value
            
            ps = conn.prepareStatement(insertStatement);
            ps.setString(1, city);
            ps.execute();
            
            insertStatement = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES (?, '', LAST_INSERT_ID(), ?, ?, now(), 'admin', now(), 'admin'); ";//Insert address(3), postalCode(4), and phone(5) values
            
            ps = conn.prepareStatement(insertStatement);
            ps.setString(1, address);
            ps.setString(2, postalCode);
            ps.setString(3, phone);
            ps.execute();
            
            insertStatement = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES(?, LAST_INSERT_ID(), 1, now(), 'admin', now(), 'admin');";//Insert customerName(6) value 
            
            ps = conn.prepareStatement(insertStatement);
            ps.setString(1, customerName);

            ps.execute();
            
            return true;
        } catch (SQLException e) {
            Logger.getLogger(scheduleController.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return false;
        
    }
    
    //Update database's customer record with new values
    //First goes through customer's input to make sure no new countries, cities, or address are inputed
    public boolean updateCustomer(String country, String city, String address, String phone, String postalCode, String customerName){
        try {
            Connection conn = Connect.getConnection();
            
            //Insert a country record if the given country is unique
            String updateStatement = "INSERT INTO country (country, createDate, createdBy, lastUpdateBy)" +
                "SELECT * FROM (SELECT ?, NOW(),'ADMIN','administer') AS tmp " +//country(1) 
                "WHERE NOT EXISTS " +
                "(SELECT country FROM country WHERE country = ?) LIMIT 1;";//country(2)
            //System.out.println(updateStatement);
            PreparedStatement ps = conn.prepareStatement(updateStatement);
            ps.setString(1,country);
            ps.setString(2,country);
            ps.execute();
            
            //Insert a city record if the given city does not exist
           updateStatement = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy)" +
                "SELECT * FROM (SELECT ?, (SELECT countryId FROM country WHERE country = ? LIMIT 1), NOW(),'ADMIN','administer') AS tmp " +//sets city(1) valuses, grabs countryId from inptued country(2)
                "WHERE NOT EXISTS " +
                "(SELECT city FROM city WHERE city = ?) LIMIT 1;";// Inserts city if inputed city(3) name does not exist       
            ps = conn.prepareStatement(updateStatement);
            ps.setString(1,city);
            ps.setString(2,country);
            ps.setString(3,city);
            ps.execute();
            
            //Inserts an address record if the given address does not exist
            updateStatement = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)" +
                "SELECT * FROM (SELECT ?, '', (SELECT cityId FROM city WHERE city = ? LIMIT 1), ?, ?, NOW(),'ADMIN','administer') AS tmp " +//address(1) city(2) postalCode(3) phone(4)
                "WHERE NOT EXISTS " +
                "(SELECT address FROM address WHERE address = ? AND postalCode = ? AND phone = ?) LIMIT 1;";//address(5) postalCode(5) phone(6)         
            ps = conn.prepareStatement(updateStatement);
            ps.setString(1,address);
            ps.setString(2,city);
            ps.setString(3,postalCode);
            ps.setString(4,phone);
            ps.setString(5,address);
            ps.setString(6, postalCode);
            ps.setString(7, phone);
            ps.execute();
            
            //Updates customer record with a new name value
            updateStatement = "UPDATE customer SET customerName = ? WHERE customerId = ?;";            
            ps = conn.prepareStatement(updateStatement);
            
            ps.setString(1,customerName);
            ps.setString(2,Integer.toString(customerId));            
            ps.execute();
            /*
            //Now that its ensured that a customer's record is complete
            //Update address, phone, and postal attributes
            updateStatement = "UPDATE address "
                    + "SET address = "+ customerName +", postalCode = "+ postalCode +", phone = "+ phone +", "
                    + "cityId =(SELECT cityId FROM city WHERE EXISTS (SELECT city FROM city WHERE city = "+ city +"))"
                    + "WHERE addressId = (SELECT addressId FROM customer WHERE customerId = "+ customerId +")";            
            ps = conn.prepareStatement(updateStatement);
            ps.execute();
            
            //Update customerName 
            updateStatement = "UPDATE customer SET customer.customerName = " + customerName + " WHERE customerId = " + customerId;            
            ps = conn.prepareStatement(updateStatement);
            ps.execute();
            */
            
            Main.updateCustomer(this, country, city, address, phone, postalCode, customerName);
            return true;
            
        }catch (SQLException e) {
            Logger.getLogger(scheduleController.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
    
    //Remove customer sets the customer to inactive in the database and removes them from the allCustomers list, then deletes them
    public boolean removeCustomer(){
        try {
            Connection conn = Connect.getConnection();
            
            String updateStatement = "UPDATE customer SET active = 0 WHERE customerId = " + customerId;//Insert customerName(6) value
            
            PreparedStatement ps = conn.prepareStatement(updateStatement);
            ps.execute();
            
            Main.removeCustomer(this);
            
            return true;
            
        }catch (SQLException e) {
            Logger.getLogger(scheduleController.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }    
    
}
