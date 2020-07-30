/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Appointment;
import com.gluonhq.charm.glisten.control.TextField;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.Customer;
import Model.Main;
import java.io.IOException;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    private TableColumn<Appointment, String> customerAppColumn;
    @FXML
    private TableColumn<Appointment, String> monthColumn;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Label customerInfoLabel;
    @FXML
    private Label customerTableLabel;
    @FXML
    private Button viewNextButton;
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
    @FXML
    private ComboBox<String> monthComboBox;
    
    FilteredList<Appointment> filteredAllAppointments = new FilteredList<>(Main.getAppointmentList(), p -> true);
    
    
    
    
    //Populates customer and appointment tables with current data
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set button, label, and items text with localized string
        monthComboBox.setPromptText(Main.rb.getString("month"));
        viewByMenuButton.setText(Main.rb.getString("view"));
        viewByWeekMenuItem.setText(Main.rb.getString("week"));
        viewMonthMenuItem.setText(Main.rb.getString("month"));
        goButton.setText(Main.rb.getString("go"));
        updateCustomerButton.setText(Main.rb.getString("update"));
        updateAppointmentButton.setText(Main.rb.getString("update"));
        viewNextButton.setText(Main.rb.getString("next"));
        viewAppointments.setText(Main.rb.getString("view")+" "+Main.rb.getString("appointments"));
        viewAllAppointmentsButton.setText(Main.rb.getString("all")+" "+Main.rb.getString("view")+" "+Main.rb.getString("appointments"));
        customerInfoLabel.setText(Main.rb.getString("all")+" "+Main.rb.getString("customer")+" "+Main.rb.getString("appointments"));
        customerTableLabel.setText(Main.rb.getString("customer"));
        nameColumn.setText(Main.rb.getString("name"));
        phoneColumn.setText(Main.rb.getString("phone"));
        addressColumn.setText(Main.rb.getString("address"));

        typeColumn.setText(Main.rb.getString("type"));
        consultantColumn.setText(Main.rb.getString("consultant"));
        cityColumn.setText(Main.rb.getString("city"));
        startTmeColumn.setText(Main.rb.getString("start"));
        endTimeColumn.setText(Main.rb.getString("end"));
        dayColumn.setText(Main.rb.getString("day"));

        //Populate month combo box
        monthComboBox.getItems().add(Main.rb.getString("january"));
        monthComboBox.getItems().add(Main.rb.getString("february"));
        monthComboBox.getItems().add(Main.rb.getString("march"));
        monthComboBox.getItems().add(Main.rb.getString("april"));
        monthComboBox.getItems().add(Main.rb.getString("may"));
        monthComboBox.getItems().add(Main.rb.getString("june"));
        monthComboBox.getItems().add(Main.rb.getString("july"));
        monthComboBox.getItems().add(Main.rb.getString("august"));
        monthComboBox.getItems().add(Main.rb.getString("september"));
        monthComboBox.getItems().add(Main.rb.getString("october"));
        monthComboBox.getItems().add(Main.rb.getString("november"));
        monthComboBox.getItems().add(Main.rb.getString("december"));
        
        
        //Populate customer table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
     
        customerTable.setPlaceholder(new Label(Main.rb.getString("NoRowsToDisplay")));
        
        customerTable.setItems(Main.getCustomerList());
        
        //Populate appointment table
        //Lamba expression used to append start time and end time string and increase user readability so the
        //given time given looks like military time and not just an integer
        startTmeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStart()+":00"));
        endTimeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEnd()+":00"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        consultantColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        //Lamba expresssion to get the appointment's customer, then get the customer's name, and set to cell value
        customerAppColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCustomer().getName()));
        //Lamba expression is used here to convert the String month to an integer then use DateFormatSymbols class to convert it to a
        //localized name of the month string and set it to cell value. Lamba expression is used here to lessen the complexity of the
        //appointment class, since printing the month name is to increase user readability.
        monthColumn.setCellValueFactory(c -> new SimpleStringProperty(new DateFormatSymbols().getMonths()[ Integer.parseInt(c.getValue().getMonth())-1 ]));
        
        appointmentTable.setPlaceholder(new Label(Main.rb.getString("NoRowsToDisplay")));
        
        SortedList<Appointment> sortedAppointment = new SortedList<>(filteredAllAppointments);
        sortedAppointment.comparatorProperty().bind(appointmentTable.comparatorProperty());
        
        appointmentTable.setItems(sortedAppointment);
        
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
            alertUser(Main.rb.getString("SelectTheAppointmentYouWishToUpdate"));
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
            alertUser(Main.rb.getString("SelectTheCustomerYouAreMeetingWithFromTheCustomerTable"));
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
    private void viewMonthMenuItemHandler(ActionEvent event) {

    }

    @FXML
    private void viewByWeekMenuItemHandler(ActionEvent event) {
    }
    
    private void alertUser(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Main.rb.getString("ErrorFound"));
        alert.setHeaderText(null);
        alert.setContentText(message);//Displays error string from inputValidation

        alert.showAndWait();   
    }

    @FXML
    private void goButtonHandler(ActionEvent event) {
        appointmentTable.setItems(filteredAllAppointments);
        filteredAllAppointments.setPredicate(appointment -> {
            if(Integer.parseInt(appointment.getMonth()) == monthComboBox.getSelectionModel().getSelectedIndex()+1 && appointment.getYear().equals(yearTextField.getText().trim())){
                return true;
            }

            return false;
        });
        
    }

    @FXML
    private void viewAppointmentsButtonHandler(ActionEvent event) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        appointmentTable.setItems(customer.getAppointments());
        customerInfoLabel.setText(customer.getName()+"'s "+ Main.rb.getString("appointments"));
    }

    @FXML
    private void viewAllAppointmentsButtonHandler(ActionEvent event) {
        appointmentTable.setItems(Main.getAppointmentList());
        customerInfoLabel.setText(Main.rb.getString("all")+" "+Main.rb.getString("customer")+" "+Main.rb.getString("appointments"));
    }
    
    
}
