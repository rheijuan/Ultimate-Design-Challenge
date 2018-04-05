package gui;

import database.DBController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import database.User;

import java.awt.*;

public class LogInController {

    @FXML private TextField userText;

    @FXML private TextField passwordText;

    @FXML
    private void enterClinic() {
        ObservableList<User> users = DBController.getUsers();

        for (User user : users) {
            if (user.getName().equals(userText.getText()) &&
                    user.getPassword().equals(passwordText.getText())) {
                switch (user.getRole()) {
                    case "Doctor":
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
                        // TODO load the invalid screen
                        break;
                }
            }
        }
    }

}
