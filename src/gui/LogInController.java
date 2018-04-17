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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable{
    @FXML private MainController mc;

    @FXML private TextField userText;

    @FXML private PasswordField passwordText;

    @FXML private Label invalidLabel;

    @FXML
    private void enterClinic(ActionEvent event) {
        String userName = userText.getText();
        String password = passwordText.getText();
        boolean found = false;
        Stage newStage = new Stage();

        if (userName.equals("") || password.equals(""))
            invalidLabel.setVisible(true);

        else {
            for (User user : users) {
                if (userName.equals(user.getUsername())) {
                    if (password.equals(user.getPassword())) {
                        switch (user.getRole()) {
                            case "Doctor":
                                found = true;
                                DoctorScreenController dc=  new DoctorScreenController(mc);
                                dc.setName(user.getName());
                                mc.getControllerList().add(dc);
                                changeToDoctorScene(event, "doctorscreen.fxml", dc );
                                clearFields();
                                break;
                            case "Patient":
                                found = true;
                                PatientScreenController pc = new PatientScreenController(mc);
                                pc.setName(user.getName());
                                pc.setDoc1Name(doctors.get(0).getName());
                                pc.setDoc2Name(doctors.get(1).getName());
                                mc.getControllerList().add(pc);
                                changeToPatientScene(event, "patientscreen.fxml", pc);
                                clearFields();
                                break;
                            case "Secretary":
                                found = true;
                                SecretaryScreenController sc = new SecretaryScreenController(mc);
                                sc.setName(user.getName());
                                sc.setDoc1Name(doctors.get(0).getName());
                                sc.setDoc2Name(doctors.get(1).getName());
                                mc.getControllerList().add(sc);
                                changeToSecretaryScene(event, "secretaryscreen.fxml", sc);
                                clearFields();
                                break;
                        }
                    }
                }
            }
        }
        if (!found)
            invalidLabel.setVisible(true);
    }
    private void clearFields(){
        userText.clear();
        passwordText.clear();

    }

    private void changeToDoctorScene(ActionEvent event, String url, DoctorScreenController controller) {
        Parent viewParent;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setController(controller);
            viewParent = loader.load();

            Scene viewScene = new Scene(viewParent);
            if(counter < 5){
                Stage window = new Stage();
                window.setScene(viewScene);
                window.show();
                window.centerOnScreen();
            }
            else if (counter == 5) {
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(viewScene);
                window.show();
                window.centerOnScreen();
            }
            counter++;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void changeToSecretaryScene(ActionEvent event, String url, SecretaryScreenController controller) {
        Parent viewParent;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setController(controller);
            viewParent = loader.load();



            Scene viewScene = new Scene(viewParent);
            if(counter < 5){
                Stage window = new Stage();
                window.setScene(viewScene);
                window.show();
                window.centerOnScreen();
            }
            else if (counter == 5) {
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(viewScene);
                window.show();
                window.centerOnScreen();
            }
            counter++;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void changeToPatientScene(ActionEvent event, String url, PatientScreenController controller) {
        Parent viewParent;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setController(controller);
            viewParent = loader.load();



            Scene viewScene = new Scene(viewParent);
            if(counter < 5){
                Stage window = new Stage();
                window.setScene(viewScene);
                window.show();
                window.centerOnScreen();
            }
            else if (counter == 5) {
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(viewScene);
                window.show();
                window.centerOnScreen();
            }
            counter++;
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
        mc = new MainController();



        invalidLabel.setVisible(false);
    }

    private ObservableList<User> users;
    private ObservableList<User> doctors;
    private DBController dbController;
    private static int counter = 0;
}
