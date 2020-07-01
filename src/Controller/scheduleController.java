/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Appointment;
import com.gluonhq.charm.glisten.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.Customer;
import Model.Main;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lucas
 */
public class scheduleController implements Initializable {

    @FXML
    private Button addCustomerButton;
    @FXML
    private Button updateCustomerButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private TextField yearTextField;
    @FXML
    private TableView<Customer> customerTable = new TableView<Customer>();
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, Integer> phoneColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> postalColumn;
    @FXML
    private TableColumn<Customer, String> cityColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;    
    @FXML
    private TableView<Appointment> appointmentTable;
    
    @FXML
    private TableColumn<Appointment, String> startTmeColumn;
    @FXML
    private TableColumn<Appointment, String> endTimeColumn;
    @FXML
    private TableColumn<Appointment, String> dayColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> consultantColumn;
    @FXML
    private TableColumn<Appointment, String> customerLinkColumn;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Label customerInfoLabel;
    @FXML
    private Button viewNextButton;
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
    private MenuItem juneMenuItem;
    @FXML
    private MenuItem julyMenuItem;
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
    @FXML
    private MenuButton viewByMenuButton;
    @FXML
    private MenuItem viewMonthMenuItem;
    @FXML
    private MenuItem viewByWeekMenuItem;
    @FXML
    private Button goButton;
    @FXML
    private Button viewAppointments;
    @FXML
    private Button viewAllAppointmentsButton;
    
    
    
    
    
    //Populates customer and appointment tables with current data
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Populate customer table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
     
        customerTable.setPlaceholder(new Label("No rows to display")); 
        
        customerTable.setItems(Main.getCustomerList());
        
        //Populate appointment table
        startTmeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        consultantColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        customerLinkColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        appointmentTable.setPlaceholder(new Label("No rows to display")); 
        
        appointmentTable.setItems(Main.getAppointmentList());
        
    }    

    //Opens customer form
    @FXML
    private void addCustomerButtonHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Viewer/customerForm.fxml"));
        
        Scene scene = new Scene(root);
               
        Main.stage.setScene(scene);
        Main.stage.show();
    }
    
    //Update customer info. Opens the customer form with current info.
    @FXML
    private void updateCustomerButtonHandler(ActionEvent event) throws IOException {        
        FXMLLoader load = new FXMLLoader(getClass().getResource("/Viewer/customerForm.fxml"));
        Parent formScreen = load.load();
        
        Stage formStage = new Stage();
        Scene formScene = new Scene(formScreen);       
        formStage.setScene(formScene);
        
        CustomerFormController control = load.<CustomerFormController>getController();
        control.setCustomerInfo(customerTable.getSelectionModel().getSelectedItem());
        
        formStage.show();        
    }
    
    //Delete selected appointment
    @FXML
    private void deleteCustomerButtonHandler(ActionEvent event) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        customer.removeCustomer();
    }

    
    //Views next month or next week
    @FXML
    private void viewNextButtonHandler(ActionEvent event) {
    }
    
    //Updates the selected appointment by opening the appointment form with current values
    @FXML
    private void updateAppointmentButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("/Viewer/appointmentForm.fxml"));
        Parent formScreen = load.load();
        
        Stage formStage = new Stage();
        Scene formScene = new Scene(formScreen);       
        formStage.setScene(formScene);
        
        AppointmentFormController control = load.<AppointmentFormController>getController(); 
        if(appointmentTable.getSelectionModel().getSelectedItem() == null){
            alertUser("Select the appointment you wish to update.");
        }else{
            control.setAppointmentInfo(appointmentTable.getSelectionModel().getSelectedItem());        
            formStage.show(); 
        }
    }

    @FXML
    private void addAppointmentButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("/Viewer/appointmentForm.fxml"));
        Parent formScreen = load.load();
        
        Stage formStage = new Stage();
        Scene formScene = new Scene(formScreen);       
        formStage.setScene(formScene);
        
        AppointmentFormController control = load.<AppointmentFormController>getController(); 
        if(customerTable.getSelectionModel().getSelectedItem() == null){
            alertUser("Select the customer you are meeting with from the Customer Table.");
        }else{
            control.setAppointmentInfo(customerTable.getSelectionModel().getSelectedItem());        
            formStage.show(); 
        }
    }

    @FXML
    private void deleteAppointmentButtonHandler(ActionEvent event) {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        appointment.removeAppointment();
    }

    @FXML
    private void janMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void febMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void marMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void aprMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void mayMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void juneMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void julyMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void augMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void sepMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void octMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void novMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void decMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void viewMonthMenuItemHandler(ActionEvent event) {
    }

    @FXML
    private void viewByWeekMenuItemHandler(ActionEvent event) {
    }
    
    private void alertUser(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error Found");
        alert.setHeaderText(null);
        alert.setContentText(message);//Displays error string from inputValidation

        alert.showAndWait();   
    }

    @FXML
    private void goButtonHandler(ActionEvent event) {
        
    }

    @FXML
    private void viewAppointmentsButtonHandler(ActionEvent event) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        appointmentTable.setItems(customer.getAppointments());
    }

    @FXML
    private void viewAllAppointmentsButtonHandler(ActionEvent event) {
        appointmentTable.setItems(Main.getAppointmentList());
    }
    
    
}
