/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Appointment;
import Model.Customer;
import Model.Main;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Connect;

/**
 * FXML Controller class
 *
 * @author Lucas
 */
public class AppointmentFormController implements Initializable {

    @FXML
    private TextField typeTextField;
    @FXML
    private TextField yearTextField;
   
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> startComboBox;
    @FXML
    private ComboBox<String> endComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    
    @FXML
    private MenuButton monthMenuButton;
    @FXML
    private MenuItem janMenuItem;
    @FXML
    private MenuItem febMenuItem;
    @FXML
    private MenuItem marMenuItem;
    @FXML
    private MenuItem aprMenuItem;
    @FXML
    private MenuItem mayMenuItem;
    @FXML
    private MenuItem junMenuItem;
    @FXML
    private MenuItem julMenuItem;
    @FXML
    private MenuItem augMenuItem;
    @FXML
    private MenuItem sepMenuItem;
    @FXML
    private MenuItem octMenuItem;
    @FXML
    private MenuItem novMenuItem;
    @FXML
    private MenuItem decMenuItem;
    
    String type;
    String start;
    String end;
    String year;
    String month;
    String day;
    
    String errorString = "";
    private boolean updatingAppointment = false;
    Customer customer;
    Appointment appointment;
    String user;    
    @FXML
    private Label appointmentLabel;
    @FXML
    private Label lengthLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Populate combo box
        //Populate start times combo box
        startComboBox.getItems().add("08AM");
        startComboBox.getItems().add("09AM");
        startComboBox.getItems().add("10AM");
        startComboBox.getItems().add("11AM");
        startComboBox.getItems().add("12PM");
        startComboBox.getItems().add("01PM");
        startComboBox.getItems().add("02PM");
        startComboBox.getItems().add("03PM");
        startComboBox.getItems().add("04PM");
        
        //Populate end times combo box
        endComboBox.getItems().add("09AM");
        endComboBox.getItems().add("10AM");
        endComboBox.getItems().add("11AM");
        endComboBox.getItems().add("12PM");
        endComboBox.getItems().add("01PM");
        endComboBox.getItems().add("02PM");
        endComboBox.getItems().add("03PM");
        endComboBox.getItems().add("04PM");
        endComboBox.getItems().add("05PM");
        
        appointmentLabel.setText(Main.rb.getString("appointments")+" "+Main.rb.getString("type"));
        lengthLabel.setText(Main.rb.getString("length"));

        startComboBox.setPromptText(Main.rb.getString("start"));
        endComboBox.setPromptText(Main.rb.getString("end"));
        monthMenuButton.setText(Main.rb.getString("month"));
        dayComboBox.setPromptText(Main.rb.getString("day"));
    }    

    @FXML
    private void saveButtonHandler(ActionEvent event) throws SQLException {        
        Alert alert;
        errorString="";
        if(inputValidation()){
            if(!updatingAppointment){
                appointment = new Appointment(customer, 0, type, user, start, end, year, month, day, true);//Create new appointment
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(Main.rb.getString("NewRecordCreated"));
                alert.setHeaderText(null);
                alert.setContentText(Main.rb.getString("ANewAppointHasBeenCreatedAndAddedToTheDatabase"));

                alert.showAndWait();
                cancelButtonHandler(event);
            }else if(updatingAppointment){     
               appointment.updateAppointment(type, user, start, end, year, month, day);
                
                cancelButtonHandler(event);
            }
        } else{
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(Main.rb.getString("ErrorFound"));
            alert.setHeaderText(null);
            alert.setContentText(errorString);//Displays error string from inputValidation

            alert.showAndWait();        
        }        
    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }
    
    //Checks input of form, sets a warning message and returns true if their are no errors 
    private boolean inputValidation(){
        type = typeTextField.getText().trim();
        
        year = yearTextField.getText().trim();
        if(dayComboBox.getValue() == null || startComboBox.getValue() == null || endComboBox.getValue() == null){
            errorString += Main.rb.getString("PleaseFillOutAllFieldsBeforeSaving")+"\n";
            return false;
        }
        day = dayComboBox.getValue();

        start = formatTime(startComboBox.getValue());
        end = formatTime(endComboBox.getValue());
        
        if( day == null || year.equals("") || year == null || type.equals("") || type == null || start.equals("start") || end.equals("end") || day.equals("day") || month == null){
            errorString += Main.rb.getString("PleaseFillOutAllFieldsBeforeSaving")+"\n";
            return false;
        }
        try{
            int x = Integer.parseInt(year);
            if(x>2022 || x<2019){
                errorString += Main.rb.getString("PleaseInsertAvalidYearBetween2019And2022")+"\n";
            }
        }catch (NumberFormatException e){
            errorString += Main.rb.getString("PleaseInsertAvalidYearBetween2019And2022")+"\n";
            return false;
        }

        if(scheduleConflict(start, end)){
            errorString += Main.rb.getString("ScheduleConflict");
            return false;
        }

        return true;
    }

    //
    private boolean scheduleConflict(String start, String end){
        Appointment appointment;
        int startInt = Integer.parseInt(start);
        int endInt = Integer.parseInt(end);
        int startCompare;
        int endCompare;

        if(startInt >= endInt){
            return true;
        }

        for (int x=0;x<Main.getAppointmentList().size();x++){
            appointment = Main.getAppointmentList().get(x);
            if(appointment.getUser().equals(Main.getUserName()) && appointment.getYear().equals(year) && appointment.getMonth().equals(month) && appointment.getDay().equals(day)){

                startCompare = Integer.parseInt(appointment.getStart());
                endCompare = Integer.parseInt(appointment.getEnd());
                if(startInt>=startCompare && startInt<endCompare){
                    return true;
                }
                if(endInt>startCompare && endInt<=endCompare ){
                    return true;
                }
                if(startCompare >= startInt && startCompare<endInt){
                    return true;
                }
            }

        }

        return false;
    }

    private String formatTime(String time){
        int militaryTime;
        if(time.contains("PM") && !time.equals("12PM")){
            militaryTime = Integer.parseInt(time.substring(0,2)) + 12;
        }else{
            militaryTime = Integer.parseInt(time.substring(0,2));
        }

        time = Integer.toString(militaryTime);

        return time;
    }
    
    //Grabs selected customer and assoicates it with the new appointment
    public void setAppointmentInfo(Customer customer){
        this.customer = customer;
    }
    
    //Takes appointment and populates appointment form text fields and menus with the appointment's variables
    public void setAppointmentInfo(Appointment appointment){
        updatingAppointment = true;
        this.appointment = appointment;

        typeTextField.setText(appointment.getType());
        yearTextField.setText(appointment.getYear());
          
    }

    @FXML
    private void janMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("january"));
        populateDayComboBoxFor(31);
        month = "01";
    }

    @FXML
    private void febMenuItemHandler(ActionEvent event) {
       int year = 1;
       monthMenuButton.setText(Main.rb.getString("february"));
       if(yearTextField.getText() != null && !yearTextField.getText().trim().equals("") ){ 
            year = Integer.parseInt(yearTextField.getText());            
       }
       if(year%4 == 0){
            populateDayComboBoxFor(28);
       }else{
            populateDayComboBoxFor(29);
        }
       month = "02";
    }

    @FXML
    private void marMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("march"));
        populateDayComboBoxFor(31);
        month = "03";
    }

    @FXML
    private void aprMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("april"));
        populateDayComboBoxFor(30);
        month = "04";
    }

    @FXML
    private void mayMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("may"));
        populateDayComboBoxFor(31);
        month = "05";
    }

    @FXML
    private void junMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("june"));
        populateDayComboBoxFor(30);
        month = "06";
    }

    @FXML
    private void julMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("july"));
        populateDayComboBoxFor(31);
        month = "07";
    }

    @FXML
    private void augMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("august"));
        populateDayComboBoxFor(31);
        month = "08";
    }

    @FXML
    private void sepMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("september"));
        populateDayComboBoxFor(30);
        month = "09";
    }

    @FXML
    private void octMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("october"));
        populateDayComboBoxFor(31);
        month = "10";
    }

    @FXML
    private void novMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("novmeber"));
        populateDayComboBoxFor(30);
        month = "11";
    }

    @FXML
    private void decMenuItemHandler(ActionEvent event) {
        monthMenuButton.setText(Main.rb.getString("december"));
        populateDayComboBoxFor(31);
        month = "12";
    }
    
    //Populates day combo box with number of given days
    private void populateDayComboBoxFor(int days){
        dayComboBox.getItems().clear();
        for(int x=1;x<=days;x++){
            if(x>=1 && x<10) {
                dayComboBox.getItems().add("0"+Integer.toString(x));
            }else{
                dayComboBox.getItems().add(Integer.toString(x));
            }
        }
    }
}
