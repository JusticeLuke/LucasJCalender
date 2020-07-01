/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lucas
 */
public class CustomerFormController implements Initializable {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField streetNumberTextField;
    @FXML
    private TextField streetNameTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField postalTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    
    String name;
    String phone;
    String address;
    String postalCode;
    String country;
    String city;
    int customerId;
    
    String errorString = "";
    boolean updatingCustomer = false;
    Customer customer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void saveButtonHandler(ActionEvent event) {        
        
        Alert alert;//Alert window to display info and input errors
        
        if(inputValidation()){
            if(!updatingCustomer){
                customer = new Customer(0, name, phone, address, postalCode, city, country, true);//Create new customer
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("New Record Created");
                alert.setHeaderText(null);
                alert.setContentText("A new record has been created and added to the database.");

                alert.showAndWait();
                cancelButtonHandler(event);
            }else if(updatingCustomer){
                customer.updateCustomer(country, city, address, phone, postalCode, name);
                
                cancelButtonHandler(event);
            }
        } else{
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Found");
            alert.setHeaderText(null);
            alert.setContentText(errorString);//Displays error string from inputValidation

            alert.showAndWait();  
            errorString = "";
        }        
    }
    
    @FXML
    private void cancelButtonHandler(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    //Checks if all text fields provided a valid input, and creates an error string that displays when the user attempts to save the data
    private boolean inputValidation(){
        boolean valid = true;
        
        name = firstNameTextField.getText().trim() + " " + lastNameTextField.getText().trim();
        phone = phoneTextField.getText().trim();
        address = streetNumberTextField.getText().trim() + " " + streetNameTextField.getText().trim();
        String streetNum = streetNumberTextField.getText().trim();
        String streetName = streetNameTextField.getText().trim();
        postalCode = postalTextField.getText().trim();
        country = countryTextField.getText().trim();
        city = cityTextField.getText().trim();
        
        if(name.equals("") || phone.equals("") || streetNum.equals("") || streetName.equals("") || postalCode.equals("") || country.equals("") || city.equals("")){
            errorString += "Please fill out all fields.\n";
            valid = false;                  
        }
        
        //Checks if postal code and street num contain only numbers
        try{
            int phoneNum = Integer.parseInt(phone);
            if(String.valueOf(phoneNum).length() != 10){
                errorString += "Phone numbers have 10 digits."
            }
            int postal = Integer.parseInt(postalCode);
            if(String.valueOf(postal).length() != 5 || postal < 0){
                errorString += "Postal codes must have 5 digits, and cannot be negative\n";
                valid = false;
            }
            int stNum = Integer.parseInt(streetNum);
        }catch(NumberFormatException e){
            errorString += "Phone number, street number and postal code must contain only numbers.\n";
            valid = false;
        }
        
        //Checks if city and country fields contain only letters
        if(city.chars().allMatch(Character::isLetter) || country.chars().allMatch(Character::isLetter)){
            errorString += "City and country should only contain letters.\n";
            valid = false;
        }
        
        
        
        return valid;
       
    }
    
    //If updating a customer passes the customer object, and sets it variables to the apporiate text fields.
    void setCustomerInfo(Customer customer){
        updatingCustomer = true;
        this.customer = customer;
        customerId = customer.getCustomerId();

        firstNameTextField.setText(customer.getName().substring(0,customer.getName().indexOf(" ")));  
        lastNameTextField.setText(customer.getName().substring(customer.getName().indexOf(" ")).trim());    

        String addressSubString = customer.getAddress();
        streetNumberTextField.setText(addressSubString.substring(0,addressSubString.indexOf(" ")));       
        streetNameTextField.setText(addressSubString.substring(addressSubString.indexOf(" ")+1));    

        postalTextField.setText(customer.getPostalCode());    
        cityTextField.setText(customer.getCity());   
        countryTextField.setText(customer.getCountry());
        phoneTextField.setText(customer.getPhone());
    }
    
}
