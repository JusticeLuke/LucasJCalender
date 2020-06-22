/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Connect;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.scheduleController;
import javafx.scene.control.TableView;
import util.DBQuery;

/**
 *
 * @author Lucas
 */
public class Main extends Application {
    
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static Stage stage = new Stage();
    private static Parent root;
    
    @Override
    public void start(Stage stage) throws Exception {
        grabCustomerRecords();
        root = FXMLLoader.load(getClass().getResource("/Viewer/schedule.fxml"));
        
        Scene scene = new Scene(root);
               
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = Connect.startConnection();             
        
        launch(args);
        Connect.closeConnection();
    }
    
    //Query database for current list of customer's name, phone number, and address. 
    //Creates customer object for each record and adds it to the allCustomers array list.
    private void grabCustomerRecords() throws SQLException{
        Connection conn = Connect.getConnection();
        String selectStatement = "SELECT customer.customerId, customer.customerName, customer.active, address.phone, address.address, address.postalCode, city.city, country.country FROM address " 
                + "INNER JOIN customer ON address.addressId = customer.addressId "
                + "INNER JOIN city ON address.cityId = city.cityId "
                + "INNER JOIN country ON city.countryId = country.countryId;";
                
        
        DBQuery.setPreparedStatement(conn,selectStatement);
        Statement statement = DBQuery.getPreparedStatement();
        
        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        
        Customer customer;
        
        //Create customer object for each customer currently in the database
        while(rs.next()){
            if(rs.getInt("active") == 1){
                customer = new Customer(rs.getInt("customerId"), rs.getString("customerName"), rs.getString("phone"), 
                rs.getString("address"), rs.getString("postalCode"),rs.getString("city"),rs.getString("country"),false);
            }
        }       
       
    }
    
    //Returns observable list of customers
    public static ObservableList<Customer> getCustomerList(){
        return allCustomers;
    }
    
    public static Customer lookupCustomer(int customerId){
        for(int i=0;i<allCustomers.size();i++){
            if(allCustomers.get(i).getCustomerId() == customerId){
                return allCustomers.get(i);                
            }            
        }
        return null;
    }
    
    //Adds new customer to observable list
    public static boolean addToCustomerList(Customer customer){
        allCustomers.add(customer);
        return true;
    }
    
    public static void updateCustomer(Customer customer, String country, String city, String address, String phone, String postalCode, String customerName) {
        customer.setCity(city);
        customer.setCountry(country);
        customer.setName(customerName);
        customer.setPhone(phone);
        customer.setPostalCode(postalCode);
        customer.setAddress(address);
        
        TableView customerTable = (TableView) root.lookup("#customerTable");
        customerTable.refresh();
        
    }
    
    public static boolean removeCustomer(Customer customer){
        customer.removeCustomer();
        allCustomers.remove(customer);
        return true;
    }

    public static boolean checkSchedule(String date, String time){
        return true;
    }
    
    
        
}
