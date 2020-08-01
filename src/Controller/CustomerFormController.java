/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Customer;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    private Label customerInfoLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label streetNumberLabel;
    @FXML
    private Label streetNameLabel;
    @FXML
    private Label postalLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label addressLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        saveButton.setText(Main.rb.getString("save"));
        cancelButton.setText(Main.rb.getString("cancel"));
        customerInfoLabel.setText(Main.rb.getString("customer"));
        firstNameLabel.setText(Main.rb.getString("firstName"));
        lastNameLabel.setText(Main.rb.getString("lastName"));
        phoneLabel.setText(Main.rb.getString("phone"));
        streetNumberLabel.setText(Main.rb.getString("street")+" "+Main.rb.getString("number"));
        streetNameLabel.setText(Main.rb.getString("street")+" "+Main.rb.getString("name"));
        postalLabel.setText(Main.rb.getString("postal"));
        cityLabel.setText(Main.rb.getString("city"));
        countryLabel.setText(Main.rb.getString("country"));
        addressLabel.setText(Main.rb.getString("address"));
    }    

    @FXML
    private void saveButtonHandler(ActionEvent event) {        
        
        Alert alert;//Alert window to display info and input errors
        
        if(inputValidation()){
            if(!updatingCustomer){
                customer = new Customer(0, name, phone, address, postalCode, city, country, true);//Create new customer
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(Main.rb.getString("NewRecordCreated"));
                alert.setHeaderText(null);
                alert.setContentText(Main.rb.getString("ANewRecordHasBeenCreatedAndAddedToTheDatabase"));
                alert.showAndWait();
                cancelButtonHandler(event);
            }else if(updatingCustomer){
                customer.updateCustomer(country, city, address, phone, postalCode, name);
                
                cancelButtonHandler(event);
            }
        } else{
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(Main.rb.getString("ErrorFound"));
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

        if(firstNameTextField.getText().trim().isEmpty() || lastNameTextField.getText().trim().isEmpty()){
            errorString += Main.rb.getString("PleaseFillOutAllFieldsBeforeSaving");
            return false;
        }
        name = firstNameTextField.getText().trim() + " " + lastNameTextField.getText().trim();
        
        phone = phoneTextField.getText().trim();
        phone = phone.replaceAll("-", "");//Removes all dashes from the phone string. They are added back after the string in checked for errors
        address = streetNumberTextField.getText().trim() + " " + streetNameTextField.getText().trim();
        String streetNum = streetNumberTextField.getText().trim();
        String streetName = streetNameTextField.getText().trim();
        postalCode = postalTextField.getText().trim();
        country = countryTextField.getText().trim();
        city = cityTextField.getText().trim();
        
        if(name.equals("") || phone.equals("") || streetNum.equals("") || streetName.equals("") || postalCode.equals("") || country.equals("") || city.equals("")){
            errorString += Main.rb.getString("PleaseFillOutAllFieldsBeforeSaving");
            valid = false;                  
        }
        

        //Checks if postal code and street num contain only numbers
        try{
                      
            if(String.valueOf(phone).length() != 10 || !phone.chars().allMatch(Character::isDigit)){
                errorString += Main.rb.getString("PhoneNumbersHave10Digits")+"\n";
                valid = false;
            }
            int postal = Integer.parseInt(postalCode);
            if(String.valueOf(postal).length() != 5 || postal < 0){
                errorString += Main.rb.getString("PostalCodesMustHave5DigitsAndCannotBeNegative")+"\n";
                valid = false;
            }
            int stNum = Integer.parseInt(streetNum);
        }catch(NumberFormatException e){
            errorString += Main.rb.getString("PhoneNumberStreetNumberAndPostalCodeMustContainOnlyNumbers")+"\n";
            valid = false;
        }
        
        //Checks if city and country fields contain only letters
        if(!city.replaceAll(" ","").chars().allMatch(Character::isLetter) || !country.replaceAll(" ","").chars().allMatch(Character::isLetter)){
            errorString += Main.rb.getString("CityAndCountryShouldOnlyContainLetters")+"\n";
            valid = false;
        }
        
        phone = phone.substring(0,3)+"-"+phone.substring(3,6)+"-"+phone.substring(6);//Returns or places dashes into phone number
        
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
