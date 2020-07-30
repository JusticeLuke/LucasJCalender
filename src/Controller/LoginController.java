/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import Model.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Connect;
import util.DBQuery;

/**
 * FXML Controller class
 *
 * @author Lucas
 */
public class LoginController implements Initializable {

    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label timeLabel;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void loginButtonHandler(ActionEvent event) throws IOException {
        String username = userNameTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        alert.setContentText("");

        if(checkCred(username, password)) {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/Viewer/schedule.fxml"));
            Main.root = load.load();

            Stage formStage = new Stage();
            Scene formScene = new Scene(Main.root);

            formStage.setScene(formScene);
            formStage.show();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        }

    }

    private boolean checkCred(String username, String password){
        try {
            Connection conn = Connect.getConnection();
            String selectStatement = "SELECT * FROM user WHERE userName = ? AND password = ?;";

            PreparedStatement ps = conn.prepareStatement(selectStatement);

            ps.setString(1,username);
            ps.setString(2,password);

            ps.execute();
            ResultSet rs = ps.getResultSet();

            if(rs.next() == false){
                alert.setHeaderText(null);
                alert.setContentText(Main.rb.getString("PleaseInsertTheCorrectCredentials"));
                alert.showAndWait();
                return false;
            }else if (rs.next()){
                Main.setUserId(rs.getInt("userId"));
                Main.setUserName(rs.getString("userName"));
                return true;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
}
