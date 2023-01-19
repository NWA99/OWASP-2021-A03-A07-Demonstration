package com.darwishkolbwolf.infosec;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField userPasswordTextField;

    @FXML
    private CheckBox secureCheckBox;

    @FXML
    public void login() throws SQLException, IOException {

        App.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "root");
        String inputUserName = userNameTextField.getText().trim();
        String inputUserPassword = userPasswordTextField.getText();

        // ----------------------------------------------------------------------------------------------------------
        // INSECURE
        if (checkPasswordInsecure(inputUserName, inputUserPassword)){
            App.secureMode = false;
            App.setRoot("ActorWindow");
        }
        else {
            Alert alert = new Alert(AlertType.WARNING,
                    "Username " + inputUserName + " oder Passwort " + inputUserPassword
                            + " inkorrekt.",
                    ButtonType.OK);
            alert.setTitle("Infosec");
            alert.setHeaderText("Datenbank-Fehler");
            alert.show();
        }

    }

    // ----------------------------------------------------------------------------------------------------------
    // INSECURE

    private boolean checkPasswordInsecure(String inputUserName, String inputUserPassword) throws SQLException {
        if (checkUserNameAndPasswordInDatabaseInsecure(inputUserName, inputUserPassword))
            return true;
        else
            return false;
    }

    public static boolean checkUserNameAndPasswordInDatabaseInsecure(String inputUserName, String inputUserPassword)
            throws SQLException {
        String sqlGetUserCreds = "SELECT username, password FROM staff WHERE username =?;";

        PreparedStatement myPreparedStatement = App.connection.prepareStatement(sqlGetUserCreds);
        myPreparedStatement.setString(1, inputUserName);

        ResultSet resultSet = myPreparedStatement.executeQuery();

        while (resultSet.next()) {
            if (inputUserName.equals(resultSet.getString("USERNAME"))
                    && inputUserPassword.equals(resultSet.getString("PASSWORD"))) {
                return true;
            } else
                throw new IllegalArgumentException("Username or password wrong.");
        }
        return false;

    }

    @FXML
    public void switchToSecureMode() {
        try {
            App.setRoot("SecureLoginWindow");
        } catch (Exception e) {

        }
    }
}