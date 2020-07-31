/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Appointment;
import com.gluonhq.charm.glisten.control.TextField;

import java.lang.reflect.Array;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.Customer;
import Model.Main;
import java.io.IOException;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    private TextField yearTextField;
    @FXML
    private TableView<Customer> customerTable;
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
    private Button viewNextButton;
    private MenuButton viewByMenuButton;
    private MenuItem viewMonthMenuItem;
    private MenuItem viewByWeekMenuItem;
    private Button goButton;
    @FXML
    private Button viewAppointments;
    @FXML
    private Button viewAllAppointmentsButton;
    private ComboBox<String> monthComboBox;
    
    FilteredList<Appointment> filteredAllAppointments = new FilteredList<>(Main.getAppointmentList(), p -> true);
    int viewWeekStartInt = 1;
    @FXML
    private DatePicker datePicker;
    @FXML
    private MenuButton dateViewMenuButton;
    @FXML
    private MenuItem viewWeek;
    @FXML
    private MenuItem viewMonth;
    @FXML
    private TableColumn<?, ?> yearColumn;
    @FXML
    private Button reportButton;
    //Populates customer and appointment tables with current data
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Set button, label, and items text with localized string
        updateCustomerButton.setText(Main.rb.getString("update"));
        updateAppointmentButton.setText(Main.rb.getString("update"));
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

        //Lamba expresssion to get the appointment's customer, then get the customer's name, and set it to cell value
        customerAppColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCustomer().getName()));

        //Lamba expression is used here to convert the String month to an integer then use DateFormatSymbols class to convert it to a
        //localized name of the month string and set it to cell value. Lamba expression is used here to lessen the complexity of the
        //appointment class, since printing the month name is to increase user readability.
        monthColumn.setCellValueFactory(c -> new SimpleStringProperty(new DateFormatSymbols().getMonths()[ Integer.parseInt(c.getValue().getMonth())-1 ]));

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        
        appointmentTable.setPlaceholder(new Label(Main.rb.getString("NoRowsToDisplay")));
        
        SortedList<Appointment> sortedAppointment = new SortedList<>(filteredAllAppointments);
        sortedAppointment.comparatorProperty().bind(appointmentTable.comparatorProperty());
        
        appointmentTable.setItems(sortedAppointment);

        upcomingAppointment();
        
    }

    private void upcomingAppointment() {
        LocalDateTime now = LocalDateTime.now();
        for(int x=0;x<Main.getAppointmentList().size();x++){
            if(Integer.parseInt(Main.getAppointmentList().get(x).getYear()) == now.getYear()){
                if(Integer.parseInt(Main.getAppointmentList().get(x).getMonth()) == now.getMonthValue()){
                    if(Integer.parseInt(Main.getAppointmentList().get(x).getDay()) == now.getDayOfMonth()){
                        if(Integer.parseInt(Main.getAppointmentList().get(x).getStart()) == now.getHour()-1 && now.getMinute() >= 45){
                            alertUser("You have an appointment with "+Main.getAppointmentList().get(x).getCustomer().getName() +"at "+Integer.parseInt(Main.getAppointmentList().get(x).getStart()));
                        }
                    }
                }
            }
        }
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


    private void viewMonthMenuItemHandler(ActionEvent event) {
        appointmentTable.setItems(Main.getAppointmentList());
        customerInfoLabel.setText(Main.rb.getString("all")+" "+Main.rb.getString("customer")+" "+Main.rb.getString("appointments"));
    }

    private void viewByWeekMenuItemHandler(ActionEvent event) {
        appointmentTable.setItems(filteredAllAppointments);

        LocalDate week = LocalDate.of(Integer.parseInt(yearTextField.getText()),monthComboBox.getSelectionModel().getSelectedIndex()+1,1);
        //String weekStart = week.with(DayOfWeek.MONDAY).toString();
        filteredAllAppointments.setPredicate(appointment -> {
            String weekStart = week.with(DayOfWeek.MONDAY).toString();

            for(int x=0;x<7;x++){
                weekStart = week.with(DayOfWeek.MONDAY.plus(x)).toString();
                if(Integer.parseInt(weekStart.substring(weekStart.length()-2,weekStart.length())) == Integer.parseInt(appointment.getDay())){
                    if(Integer.parseInt(weekStart.substring(weekStart.length()-5,weekStart.length()-3)) == Integer.parseInt(appointment.getMonth())){
                        return true;
                    }
                }
            }

            return false;
        });

    }
    
    private void alertUser(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Main.rb.getString("ErrorFound"));
        alert.setHeaderText(null);
        alert.setContentText(message);//Displays error string from inputValidation

        alert.showAndWait();   
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

    @FXML
    private void viewWeekHandler(ActionEvent event) {
        if(datePicker.getValue() == null){
            alertUser("Please select a date before selecting view week of or view month buttons");
        }else {
            appointmentTable.setItems(filteredAllAppointments);
            LocalDate week = datePicker.getValue();
            //Lamba expression to filter appointment table rows based on the week taken from the date picker
            filteredAllAppointments.setPredicate(appointment -> {
                String weekStart = week.with(DayOfWeek.MONDAY).toString();

                for (int x = 0; x < 7; x++) {
                    weekStart = week.with(DayOfWeek.MONDAY.plus(x)).toString();
                    if (Integer.parseInt(weekStart.substring(weekStart.length() - 2, weekStart.length())) == Integer.parseInt(appointment.getDay())) {
                        if (Integer.parseInt(weekStart.substring(weekStart.length() - 5, weekStart.length() - 3)) == Integer.parseInt(appointment.getMonth())) {
                            return true;
                        }
                    }
                }

                return false;
            });
        }
    }

    @FXML
    private void viewMonthHandler(ActionEvent event) {
        if(datePicker.getValue() == null){
            alertUser("Please select a date before selecting view week of or view month buttons");
        }else {
            appointmentTable.setItems(filteredAllAppointments);
            filteredAllAppointments.setPredicate(appointment -> {
                if (Integer.parseInt(appointment.getMonth()) == datePicker.getValue().getMonthValue() && Integer.parseInt(appointment.getYear()) == datePicker.getValue().getYear()) {
                    return true;
                }
                return false;
            });
        }
    }

    @FXML
    private void reportButtonHandler(ActionEvent event) {
        Label report = new Label();
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(report);

        Scene scene = new Scene(pane,500,1000);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        //Makes list of users
        List<String> userList = new ArrayList<String>();
        for(int x=0;x<Main.getAppointmentList().size();x++){
            Appointment a = Main.getAppointmentList().get(x);
            if(userList.isEmpty() || !userList.contains(a.getUser())){
                userList.add(a.getUser());
            }
        }
        //Consultant schedules
        String userSchedule= "";
        for(int i=0;i<userList.size();i++) {
            userSchedule = "        Consultant: "+userList.get(i) + "\n";
            for (int x = 0; x < Main.getAppointmentList().size(); x++) {
                Appointment a = Main.getAppointmentList().get(x);
                if (a.getUser().equals(userList.get(i))) {
                    userSchedule += "       Appointment with " + a.getCustomer().getName() + " from " + a.getStart() + " to " + a.getEnd() + " on " + a.getDay() + "." + a.getMonth() + "." + a.getYear() + "\n";
                }
            }
        }

        //Number of appointment types in each month
        String numAppointment = "";
        int num = 0;
        for (int j=2019;j<=2021;j++){
            for(int k=1;k<=12;k++){
                num=0;
                for(int z=0;z<Main.getAppointmentList().size();z++){
                    Appointment app = Main.getAppointmentList().get(z);
                    if(Integer.parseInt(app.getYear()) == j && Integer.parseInt(app.getMonth()) == k){
                        num++;
                    }
                }
                if(num>0){
                    numAppointment +="      "+num +" in month of: "+k+"."+j+"\n";
                }
            }
        }

        //Meeting hours in each month

        String reportString= "REPORT:\n"+
                "   Consultant schedule-\n"+userSchedule+"\n"+
                "   Number of appointment types in each month-\n"+numAppointment+"\n"+
                "   Number of meeting hours scheduled in each month\n";


        report.setText(reportString);
    }
    
    
}
