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

        for (User user : users) {
            if (user.getName().equals(userText.getText()) &&
                    user.getPassword().equals(passwordText.getText())) {
                switch (user.getRole()) {
                    case "Doctor":
                        changeScene(event, "doctorscreen.fxml");
                        // TODO load the screen of the Doctor
                        System.out.println("The doctor is in");
                        break;
                    case "Secretary":
                        // TODO load the screen of the Secretary
                        System.out.println("The secretary is in");
                        break;
                    case "Patient":
                        // TODO load the screen of the Patient
                        System.out.println("The patient is in");
                        break;
                    default:
                        invalidLabel.setVisible(true);
                        break;
                }
            }
        }
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
        System.out.println("Hello");
        users = DBController.getUsers();

        for (User user : users)
            System.out.println(user.toString());

        invalidLabel.setVisible(false);
    }

    private ObservableList<User> users;
}
