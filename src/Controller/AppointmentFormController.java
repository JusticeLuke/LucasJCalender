/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
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
    private MenuButton startMenu;
    @FXML
    private MenuButton endMenu;
    @FXML
    private MenuButton yearMenu;
    @FXML
    private MenuButton monthMenu;
    @FXML
    private MenuButton dayMenu;
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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void saveButtonHandler(ActionEvent event) {
        type = typeTextField.getText();
        //start = startMenu.get
    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) {
    }
    
}
