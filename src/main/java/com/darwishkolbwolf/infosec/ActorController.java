package com.darwishkolbwolf.infosec;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ActorController implements Initializable {

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<Actor> actorsDataTable;

    @FXML
    private TableColumn<Actor, String> actorIDColumn;

    @FXML
    private TableColumn<Actor, String> firstNameColumn;

    @FXML
    private TableColumn<Actor, String> lastNameColumn;

    @FXML
    private TableColumn<Actor, String> lastUpdateColumn;

    private ObservableList<Actor> actorsObservableList = FXCollections.observableArrayList();

    private final static String sqlAll = "SELECT * FROM actor LIMIT 10;";

    @FXML
    public void search() {
        String lastName = lastNameTextField.getText();
        String firstName = firstNameTextField.getText();
        List<String> searchDetails = new ArrayList<String>();
        actorsObservableList.clear();

        // ----------------------------------------------------------------------------------------------------------
        // SECURE
        if (App.secureMode) {
            searchDetails = getSearchedActorDetailsSecure(lastName.trim(), firstName.trim()); // secure
        }
        // ----------------------------------------------------------------------------------------------------------
        // INSECURE
        else { // injection: GUINESS' OR '1=1
            searchDetails = getSearchedActorDetailsInSecure(lastName.trim(), firstName.trim()); // insecure
        }

        addActorsToTableView(searchDetails);
        actorsDataTable.setItems(actorsObservableList);
    }

    // ----------------------------------------------------------------------------------------------------------
    // SECURE
    public static List<String> getSearchedActorDetailsSecure(String lastName, String firstName) {
        PreparedStatement myPreparedStatement = null;

        String sqlFirstNameSearch = "SELECT * FROM actor WHERE first_name = ?";

        String sqlLastNameSearch = "SELECT * FROM actor WHERE last_name = ?";

        String sqlFullNameSearch = "SELECT * FROM actor WHERE first_name = ? AND last_name = ?";

        List<String> allActors = new ArrayList<String>();

        try {
            if (firstName.isBlank() && !lastName.isBlank()) {
                myPreparedStatement = App.connection.prepareStatement(sqlLastNameSearch);
                myPreparedStatement.setString(1, lastName);
            } else if (!firstName.isBlank() && lastName.isBlank()) {
                myPreparedStatement = App.connection.prepareStatement(sqlFirstNameSearch);
                myPreparedStatement.setString(1, firstName);
            } else if (!firstName.isBlank() && !lastName.isBlank()) {
                myPreparedStatement = App.connection.prepareStatement(sqlFullNameSearch);
                myPreparedStatement.setString(1, firstName);
                myPreparedStatement.setString(2, lastName);
            } else if (firstName.isBlank() && lastName.isBlank()) {
                myPreparedStatement = App.connection.prepareStatement(sqlAll);
            }

            ResultSet resultSet = myPreparedStatement.executeQuery();

            while (resultSet.next()) {
                allActors.add(resultSet.getString("ACTOR_ID"));
                allActors.add(resultSet.getString("FIRST_NAME"));
                allActors.add(resultSet.getString("LAST_NAME"));
                allActors.add(resultSet.getString("LAST_UPDATE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allActors;

    }

    // ----------------------------------------------------------------------------------------------------------
    // INSECURE:
    public static List<String> getSearchedActorDetailsInSecure(String lastName, String firstName) {
        Statement myStatement = null;

        String sqlFirstNameSearch = "SELECT * FROM actor WHERE first_name = ";

        String sqlLastNameSearch = "SELECT * FROM actor WHERE last_name = ";

        List<String> allActors = new ArrayList<String>();

        ResultSet resultSet = null;
        try {
            if (firstName.isBlank() && !lastName.isBlank()) {
                myStatement = App.connection.createStatement();
                resultSet = myStatement.executeQuery(sqlLastNameSearch + "'" + lastName + "'");
            } else if (!firstName.isBlank() && lastName.isBlank()) {
                myStatement = App.connection.createStatement();
                resultSet = myStatement.executeQuery(sqlFirstNameSearch + "'" + firstName + "'");
            } else if (firstName.isBlank() && lastName.isBlank()) {
                myStatement = App.connection.createStatement();
                resultSet = myStatement.executeQuery(sqlAll);
            }

            while (resultSet.next()) {
                allActors.add(resultSet.getString("ACTOR_ID"));
                allActors.add(resultSet.getString("FIRST_NAME"));
                allActors.add(resultSet.getString("LAST_NAME"));
                allActors.add(resultSet.getString("LAST_UPDATE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allActors;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupActorsDataTable();
    }

    public void addActorsToTableView(List<String> allActors) {
        for (int i = 0; i < allActors.size(); i++) {
            Actor actor = new Actor(null, null, null, null);
            actor.setActorID(allActors.get(i));
            actor.setFirstName(allActors.get(i + 1));
            actor.setLastName(allActors.get(i + 2));
            actor.setLastUpdate(allActors.get(i + 3));
            i += 3;

            actorsObservableList.add(actor);
        }
    }

    public void setupActorsDataTable() {
        actorIDColumn.setCellValueFactory(new PropertyValueFactory<Actor, String>("ActorID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Actor, String>("FirstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Actor, String>("LastName"));
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<Actor, String>("LastUpdate"));

        addActorsToTableView(getActorsFromDB());
        actorsDataTable.setItems(actorsObservableList);
    }

    private List<String> getActorsFromDB() {
        String sqlGetActors = "SELECT * FROM actor LIMIT 10";

        List<String> allActors = new ArrayList<String>();

        try {
            PreparedStatement stmt = App.connection.prepareStatement(sqlGetActors);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                allActors.add(resultSet.getString("ACTOR_ID"));
                allActors.add(resultSet.getString("FIRST_NAME"));
                allActors.add(resultSet.getString("LAST_NAME"));
                allActors.add(resultSet.getString("LAST_UPDATE"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allActors;
    }

    @FXML
    public void goBack() {
        try {
            App.setRoot("LoginWindow");
        } catch (Exception e) {
        }
    }

}