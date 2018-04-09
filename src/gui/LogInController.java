package gui;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML private TextField userText;

    @FXML private TextField passwordText;

    @FXML private Label invalidLabel;

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
                        System.out.println("Password match");
                        switch (user.getRole()) {
                            case "Doctor":
                                found = true;
                                DoctorScreenController.setName(user.getName());
                                changeScene(event, "doctorscreen.fxml");
                                break;
                            case "Patient":
                                found = true;
                                changeScene(event, "patientscreen.fxml");
                                break;
                            case "Secretary":
                                found = true;
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

        invalidLabel.setVisible(false);
    }

    private ObservableList<User> users;
    private DBController dbController;
}
