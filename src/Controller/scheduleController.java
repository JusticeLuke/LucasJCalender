/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.Customer;
import Model.Main;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private MenuButton monthMenu;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private ToggleGroup weekMonth;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private Button scheduleButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button dateButton;
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
    private TableView<?> calendarTable;
    
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
     
        customerTable.setPlaceholder(new Label("No rows to display")); 
        
        customerTable.setItems(Main.getCustomerList());
        
    }    


    @FXML
    private void addCustomerButtonHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Viewer/customerForm.fxml"));
        
        Scene scene = new Scene(root);
               
        Main.stage.setScene(scene);
        Main.stage.show();
    }

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

    @FXML
    private void deleteCustomerButtonHandler(ActionEvent event) {
    }

    @FXML
    private void monthRadioButtonHandler(ActionEvent event) {
    }

    @FXML
    private void weekRadioButtonHandler(ActionEvent event) {
    }

    @FXML
    private void scheduleButtonHandler(ActionEvent event) {
    }

    @FXML
    private void dateButtonHandler(ActionEvent event) {
    }
    
    
}
