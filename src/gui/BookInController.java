package gui;

import database.Appointment;
import database.DBController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class BookInController implements Initializable {

    @FXML private ComboBox<String> startTimeComboBox;

    @FXML private ComboBox<String> endTimeComboBox;

    @FXML private ComboBox<String> doctorBox;

    @FXML private TextField nameLabel;

    @FXML private DatePicker datePicker;

    @FXML
    private void cancel(ActionEvent event) {
        changeScene(event, "loginscreen.fxml");
    }

    @FXML
    private void bookWalkIn(ActionEvent event) {
        System.out.println("reserved!");

        int appointmentID = appointments.size() + 1;
        String patient = nameLabel.getText();
        String doctor = doctorBox.getSelectionModel().getSelectedItem();

        int day = datePicker.getValue().getDayOfMonth();
        int month = datePicker.getValue().getMonthValue();
        int year = datePicker.getValue().getYear();

        int starthour = Integer.parseInt(startTimeComboBox.getSelectionModel().getSelectedItem().split(":")[0]);
        int startmin = Integer.parseInt(startTimeComboBox.getSelectionModel().getSelectedItem().split(":")[1]);
        int endhour = Integer.parseInt(endTimeComboBox.getSelectionModel().getSelectedItem().split(":")[0]);
        int endmin = Integer.parseInt(endTimeComboBox.getSelectionModel().getSelectedItem().split(":")[1]);

        int status = 1;

        Appointment newApp = new Appointment(appointmentID, patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);

        //check for conflict first
        if (isValidTime(newApp)) {
            dbController.createAppointment(patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);
            newApp.printAppointment();
            appointments.add(newApp);
            changeScene(event, "loginscreen.fxml");
        } else {
            System.out.println("Invalid appointment slot");
        }
    }

    private boolean isValidTime(Appointment appo) {
        for (int i = 0; i < dbController.getAppointments().size(); i++) {
            Appointment a = dbController.getAppointments().get(i);
            if (appo.getMonth() == a.getMonth() && appo.getDay() == a.getDay() && appo.getYear() == a.getYear()) {

                LocalTimeRange range1 = new LocalTimeRange(LocalTime.of(appo.getStartHour(), appo.getStartMinute()), LocalTime.of(appo.getEndHour(), appo.getEndMinute()));
                LocalTimeRange range2 = new LocalTimeRange(LocalTime.of(a.getStartHour(), a.getStartMinute()), LocalTime.of(a.getEndHour(), a.getEndMinute()));
                if (range1.overlaps(range2))
                    return false;
            }
        }
        return true;
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
        setTime(startTimeComboBox.getItems());
        startTimeComboBox.getItems().add("17:00");

        setTime(endTimeComboBox.getItems());
        endTimeComboBox.getItems().add("17:00");

        doctorBox.getItems().add(docName1);
        doctorBox.getItems().add(docName2);

        appointments = DBController.getAppointments();
        dbController = new DBController();
    }

    private void setTime(ObservableList<String> items) {
        for (int i = 8; i < 17; i++) {
            for (int j = 0; j < 60; j += 30) {
                if (j % 60 == 0) {
                    items.add(i + ":00");
                    j = 0;
                }
                else
                    items.add(i + ":30");
            }
        }
    }

    public static void setDoc1Name(String name) { docName1 = name; }

    public static void setDoc2Name(String name) { docName2 = name; }

    private ObservableList<Appointment> appointments;
    private DBController dbController;
    private static String docName1;
    private static String docName2;
}
