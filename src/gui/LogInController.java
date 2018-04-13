package gui;

import database.Appointment;
import database.DBController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import database.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML private TextField userText;

    @FXML private PasswordField passwordText;

    @FXML private Label invalidLabel;
    
    @FXML private Button walkInButton;
    @FXML private Button enterButton;

    @FXML
    private void enterClinic(ActionEvent event) {
        String userName = userText.getText();
        String password = passwordText.getText();
        boolean found = false;

        if (userName.equals("") || password.equals(""))
            invalidLabel.setVisible(true);

        else {
            for (User user : users) {
                if (userName.equals(user.getUsername())) {
                    if (password.equals(user.getPassword())) {
                        switch (user.getRole()) {
                            case "Doctor":
                                found = true;
                                DoctorScreenController.setName(user.getName());
                                changeScene(event, "doctorscreen.fxml");
                                break;
                            case "Patient":
                                found = true;
                                PatientScreenController.setName(user.getName());
                                PatientScreenController.setDoc1Name(doctors.get(0).getName());
                                PatientScreenController.setDoc2Name(doctors.get(1).getName());
                                changeScene(event, "patientscreen.fxml");
                                break;
                            case "Secretary":
                                found = true;
                                SecretaryScreenController.setName(user.getName());
                                SecretaryScreenController.setDoc1Name(doctors.get(0).getName());
                                SecretaryScreenController.setDoc2Name(doctors.get(1).getName());
                                changeScene(event, "secretaryscreen.fxml");
                                break;
                        }
                    }
                }
            }
        }
        if (!found)
            invalidLabel.setVisible(true);
    }
    
    @FXML
    private void bookWalkIn(ActionEvent event) {
    	System.out.println("Book Walk in clicked");
    }

    private void changeScene(ActionEvent event, String url) {
        Parent viewParent;
        try {
            viewParent = FXMLLoader.load(getClass().getResource(url));
            Scene viewScene = new Scene(viewParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(viewScene);
            window.show();
            window.centerOnScreen();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbController = new DBController();
        dbController.loadAppointments();
        dbController.loadUsers();

        users = DBController.getUsers();
        doctors = DBController.getDoctors();

        BookInController.setDoc1Name(doctors.get(0).getName());
        BookInController.setDoc2Name(doctors.get(1).getName());

        invalidLabel.setVisible(false);
    }

    private ObservableList<User> users;
    private ObservableList<User> doctors;
    private DBController dbController;
}
