/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Date;
import java.util.MissingResourceException;
import util.Connect;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ResourceBundle;
import javafx.scene.control.TableView;
import util.DBQuery;

/**
 *
 * @author Lucas
 */
public class Main extends Application {

    public static ResourceBundle rb;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public static Stage stage = new Stage();
    public static Parent root;
    
    private static String currentUser = "test";
    private static int userId = 1;
    
    @Override
    public void start(Stage stage) throws Exception {
        grabCustomerRecords();
        root = FXMLLoader.load(getClass().getResource("/Viewer/login.fxml"));
        
        Scene scene = new Scene(root);
               
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        try{

            rb = ResourceBundle.getBundle("util/localization/Nat", Locale.getDefault());

        }catch(MissingResourceException e){
            System.out.println("Localization problem: " + e.getMessage());
        }


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
                rs.getString("address"), rs.getString("postalCode"),rs.getString("city"),rs.getString("country"),false);//Create customer
                grabAppointments(customer);//Get customer's appointments from database
            }
        }       
       
    }
    
    //Query database for customer's appointments then add them to the customer's appointment list and the allAppointment's list
    private void grabAppointments(Customer customer) throws SQLException{

        ZonedDateTime appointmentUTC = null;
        ZonedDateTime appointmentEndUTC = null;
        ZonedDateTime appointmentLocal = null;
        ZonedDateTime appointmentEndLocal = null;

        int appointmentId;

        String type;
        String user;
        String start;
        String end;
        String year;
        String month;
        String day;
        
        Connection conn = Connect.getConnection();
        String selectStatement = "SELECT appointment.appointmentId, appointment.type, appointment.userId, appointment.start, appointment.end, user.userName "
                + "FROM appointment "
                + "INNER JOIN user ON appointment.userId = user.userId "
                + "WHERE customerId = "+ customer.getCustomerId()+";";
         
        DBQuery.setPreparedStatement(conn,selectStatement);
        Statement statement = DBQuery.getPreparedStatement();
        
        statement.execute(selectStatement);
        
        ResultSet rs = statement.getResultSet();
        
        Appointment appointment;
        while(rs.next()){
            appointmentUTC = ZonedDateTime.of(rs.getTimestamp("start").toLocalDateTime().getYear(), rs.getDate("start").toLocalDate().getMonthValue(), rs.getDate("start").toLocalDate().getDayOfMonth(), rs.getTimestamp("start").toLocalDateTime().getHour(), rs.getTimestamp("start").toLocalDateTime().getMinute(), rs.getTimestamp("start").toLocalDateTime().getSecond(),0, ZoneId.of("UTC"));
            appointmentEndUTC = ZonedDateTime.of(rs.getTimestamp("end").toLocalDateTime().getYear(), rs.getDate("end").toLocalDate().getMonthValue(), rs.getDate("end").toLocalDate().getDayOfMonth(), rs.getTimestamp("end").toLocalDateTime().getHour(), rs.getTimestamp("end").toLocalDateTime().getMinute(), rs.getTimestamp("end").toLocalDateTime().getSecond(),0, ZoneId.of("UTC"));

            appointmentLocal = appointmentUTC.withZoneSameInstant(ZoneId.systemDefault());
            appointmentEndLocal = appointmentEndUTC.withZoneSameInstant(ZoneId.systemDefault());
            appointmentId = rs.getInt("appointmentId");
            type = rs.getString("type");
            start = Integer.toString(appointmentLocal.getHour());
            end = Integer.toString(appointmentEndLocal.getHour());
            year = Integer.toString(appointmentLocal.getYear());
            month = Integer.toString(appointmentLocal.getMonthValue());
            day = Integer.toString(appointmentLocal.getDayOfMonth());
            user = rs.getString("userName");
            appointment = new Appointment(customer, appointmentId, type, user, start, end, year, month, day, false);
            customer.addAppointment(appointment);
            Main.addToAppointmentList(appointment);
        }
        
        
    }
    
    //Returns observable list of customers
    public static ObservableList<Customer> getCustomerList(){
        return allCustomers;
    }
    
    public static ObservableList<Appointment> getAppointmentList(){ return allAppointments; }
    
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
    
    public static void updateTable() {

        TableView customerTable = (TableView) root.lookup("#customerTable");
        TableView appointmentTable = (TableView) root.lookup("#appointmentTable");
        customerTable.refresh();
        appointmentTable.refresh();
        
    }
    
    public static boolean removeCustomer(Customer customer){
        allCustomers.remove(customer);
        return true;
    }
    
    //Removes appointment from allAppointment list. Should only be called from inside the appointment class's removeAppointment method
    public static boolean removeAppointment(Appointment appointment){
        allAppointments.remove(appointment);
        return true;
    }
    
    public static boolean addToAppointmentList(Appointment appointment){
        allAppointments.add(appointment);
        return true;
    }
    
    //User id is set when the user logins
    public static void setUserId(int id){
        userId = id;
    }

    public static void setUserName(String user){
        currentUser = user;
    }
    
    //Returns user id. Used     
    public static int getUserId(){
        return userId;
    }

    public static String getUserName(){
        return currentUser;
    }


}
