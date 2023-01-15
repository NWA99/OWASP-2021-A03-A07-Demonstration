package com.darwishkolbwolf.infosec;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SecureLoginController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField userPasswordTextField;

    public static Connection connection;

    @FXML
    public void login() throws SQLException, IOException {

        App.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "root");
        String inputUserName = userNameTextField.getText().trim();
        String inputUserPassword = userPasswordTextField.getText();

        // ----------------------------------------------------------------------------------------------------------
        // SECURE
        if (checkPasswordSecure(inputUserName, inputUserPassword)){
            App.secureMode = true;
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
    // SECURE

    private boolean checkPasswordSecure(String inputUserName, String inputUserPassword) throws SQLException {
        if (checkUserNameAndPasswordInDatabaseSecure(inputUserName, inputUserPassword))
            return true;
        else
            return false;
    }

    public static boolean checkUserNameAndPasswordInDatabaseSecure(String inputUserName, String inputUserPassword)
            throws SQLException {
        String sqlGetUserCreds = "SELECT username, password FROM staff_secure WHERE username =?;";

        PreparedStatement myPreparedStatement = App.connection.prepareStatement(sqlGetUserCreds);
        myPreparedStatement.setString(1, inputUserName);

        ResultSet resultSet = myPreparedStatement.executeQuery();

        while (resultSet.next()) {
            if (inputUserName.equals(resultSet.getString("USERNAME"))
                    && BCrypt.checkpw(inputUserPassword, resultSet.getString("PASSWORD"))) {
                return true;
            } else
                throw new IllegalArgumentException("Username or password wrong.");
        }
        return false;

    }

}