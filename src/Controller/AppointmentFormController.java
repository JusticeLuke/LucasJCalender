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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
    private ComboBox<String> monthComboBox;
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

    String type;
    String start;
    String end;
    String year;
    String month;
    String day;
    
    String errorString;
    private boolean updatingAppointment = false;
    Customer customer;
    String user;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void saveButtonHandler(ActionEvent event) {
        type = typeTextField.getText();
        start = startComboBox.getValue();
        end = endComboBox.getValue();
        year = yearTextField.getText();
        month = monthComboBox.getValue();
        day = dayComboBox.getValue();
        
        Alert alert;
        Appointment appointment;
        
        if(inputValidation()){
            if(!updatingAppointment){
                appointment = new Appointment(customer, type, user, start, end, day);//Create new customer
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("New Record Created");
                alert.setHeaderText(null);
                alert.setContentText("A new appoint has been created and added to the database.");

                alert.showAndWait();
                cancelButtonHandler(event);
            }else if(updatingAppointment){     
                
                
                cancelButtonHandler(event);
            }
        } else{
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Found");
            alert.setHeaderText(null);
            alert.setContentText(errorString);//Displays error string from inputValidation

            alert.showAndWait();        
        }        
    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) {
    }
    
    private boolean inputValidation(){
        return true;
    }
    
    public void setAppointmentInfo(Customer customer, String user){
        this.customer = customer;
        this.user = user;
    }
    
}
