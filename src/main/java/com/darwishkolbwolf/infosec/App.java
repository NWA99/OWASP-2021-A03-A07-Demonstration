package com.darwishkolbwolf.infosec;

import java.io.IOException;
import java.sql.Connection;

import org.mindrot.jbcrypt.BCrypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static Connection connection;

    public static boolean secureMode = false;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("LoginWindow"), 1000, 800);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // generateHashedPassword();
        launch();
    }

    public static void generateHashedPassword() {
        // String enc = "password"; // $2a$10$w2EVUwKajtu4pfp6g8S2GuOfZbsmnofjo4VOrLo6xQivxOQBlYjtK
        String enc = "password123"; // $2a$10$wSBFlmTUMtRFWBHSNijIduyDmRg2v1msNBdqR1vm/nfsHL/gjVRJW
        String hashed = BCrypt.hashpw(enc, BCrypt.gensalt(10));
        System.out.println(hashed);
        System.out.println(BCrypt.checkpw(enc, hashed));
    }

}