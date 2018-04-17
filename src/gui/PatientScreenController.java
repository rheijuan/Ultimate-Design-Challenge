package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import database.Appointment;
import database.DBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PatientScreenController extends AbstractController implements Initializable, ControllerParent{


    @FXML private Label doctor1DayTag;

    @FXML private Label doctor2DayTag;

    @FXML private ComboBox<String> doctorBox;

    @FXML
    private void setAppointment () {
        mc.refreshAll();
        profilePane.setVisible(false);
    }

    @FXML
    public void displayDayView() {
        userDayTag.setText("Patient " + name);
        dayDateTag.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
        doctor1DayTag.setText("Doctor " + docName1);
        doctor2DayTag.setText("Doctor " + docName2);

        ObservableList<DayTableItem> data = initializeDayView();
        dayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dayPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));

        dayPatientColumn.setCellFactory(column -> new TableCell<DayTableItem, String> (){
            @Override
            protected void updateItem(String event, boolean empty) {
                super.updateItem(event, empty);

                if (event == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    DayTableItem currentItem = getTableView().getItems().get(getIndex());
                    if (currentItem.getColor() != null) {
                        setText(event);
                        if (currentItem.getColor().equals(java.awt.Color.decode("#78B4BF"))) {
                            setStyle("-fx-background-color: #78B4BF");
                            setTextFill(javafx.scene.paint.Color.WHITE);
                        }
                        else if (currentItem.getColor().equals(java.awt.Color.decode("#DC654D"))) {
                            setStyle("-fx-background-color: #DC654D");
                            setTextFill(javafx.scene.paint.Color.WHITE);
                        }
                        else if (currentItem.getColor().equals(java.awt.Color.decode("#98FF98"))) {
                            setStyle("-fx-background-color: #98FF98");
                            setTextFill(javafx.scene.paint.Color.BLACK);
                        }
                        else if (currentItem.getColor().equals(java.awt.Color.decode("#FDFD96"))) {
                            setStyle("-fx-background-color: #FDFD96");
                            setTextFill(javafx.scene.paint.Color.BLACK);
                        }

                    } else {
                        setStyle("");
                    }
                }
            }
        });

        dayTable.setItems(data);
    }

    @Override
    ObservableList<DayTableItem> initializeDayView() {
        ArrayList<Appointment> itemsToDisplay = new ArrayList<>();

        ArrayList<DayTableItem> toTableItems = new ArrayList<>();

        for (int hour = 8; hour < 17; hour++)
            for (int min = 0; min <= 30; min += 30) {

                if (min < 30) {
                    toTableItems.add(new DayTableItem(hour + ":" + String.format("%02d", min), "", 3));
                    toTableItems.get(toTableItems.size()-1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size()-1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size()-1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size()-1).setValueEndMin(min+29);
                } else {
                    toTableItems.add(new DayTableItem("", "", 3));
                    toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
                }
            }

        for (Appointment app : appointments){
            if (app.getMonth() == monthToday && app.getDay() == dayToday && app.getYear() == yearToday){
                System.out.println(app.getStartHour()+":"+app.getStartMin()+"-"+app.getDoctor());
                itemsToDisplay.add(app);
            }
        }

        // not this one
        itemsToDisplay.sort(Comparator.comparingInt(Appointment::getStartHour).thenComparingInt(Appointment::getStartMinute));

        for (Appointment item: itemsToDisplay) {
            int startHour = item.getStartHour();
            int startMin = item.getStartMinute();
            int endHour;
            int endMin;

            if (startMin == 0)
                startHour = item.getStartHour() * 10;

            int startTime = Integer.parseInt(Integer.toString(startHour) + Integer.toString(startMin));

            if (startHour == item.getEndHour() && startMin == item.getEndMinute()) {
                endHour = item.getEndHour();

                if (item.getEndMinute() == 0)
                    endMin = 29;
                else
                    endMin = 59;
            } else if (item.getEndMinute() == 0) {
                endHour = item.getEndHour() - 1;
                endMin = 59;
            } else if (item.getEndMinute() == 30) {
                endHour = item.getEndHour();
                endMin = 29;
            } else {
                endHour = item.getEndHour();
                endMin = item.getEndMinute();
            }

            int endTime = Integer.parseInt(Integer.toString(endHour) + Integer.toString(endMin));

            for (DayTableItem displayTime: toTableItems) {
                int displayStartTime;

                if (displayTime.getValueStartMin() == 0)
                    displayStartTime = Integer.parseInt(Integer.toString(displayTime.getValueStartHour() * 10) +
                            Integer.toString(displayTime.getValueStartMin()));
                else
                    displayStartTime = Integer.parseInt(Integer.toString(displayTime.getValueStartHour()) +
                            Integer.toString(displayTime.getValueStartMin()));

                int displayEndTime = Integer.parseInt(Integer.toString(displayTime.getValueEndHour()) +
                        Integer.toString(displayTime.getValueEndMin()));

                if (displayStartTime == startTime && displayEndTime == endTime) {
                    displayTime.setPatient(item.getPatient());

                    if (item.getDoctor().equalsIgnoreCase(docName1)) {
                        if (item.getPatient().equalsIgnoreCase(name)) {
                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode(" #78B4BF");
                                displayTime.setColor(c);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#DC654D");
                                displayTime.setColor(c);
                            }
                        }
                        else if (item.getPatient().equalsIgnoreCase("")){
                            java.awt.Color c = java.awt.Color.decode(" #78B4BF");
                            displayTime.setColor(c);
                        }
                    } else if (item.getDoctor().equalsIgnoreCase(docName2)) {
                        if (item.getPatient().equalsIgnoreCase(name)) {
                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode("#98FF98");
                                displayTime.setColor(c);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#FDFD96");
                                displayTime.setColor(c);
                            }
                        }
                        else if (item.getPatient().equalsIgnoreCase("")){
                            java.awt.Color c = java.awt.Color.decode("#98FF98");
                            displayTime.setColor(c);
                        }
                    }

                    break;
                } else if (displayStartTime == startTime) {
                    displayTime.setPatient(item.getPatient());

                    if (item.getDoctor().equalsIgnoreCase(docName1)) {
                        if (item.getPatient().equalsIgnoreCase(name)) {
                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode("#78B4BF");
                                displayTime.setColor(c);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#DC654D");
                                displayTime.setColor(c);
                            }
                        }
                        else if (item.getPatient().equalsIgnoreCase("")){
                            java.awt.Color c = java.awt.Color.decode("#78B4BF");
                            displayTime.setColor(c);
                        }
                    } else if (item.getDoctor().equalsIgnoreCase(docName2)) {
                        if (item.getPatient().equalsIgnoreCase(name)) {
                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode("#98FF98");
                                displayTime.setColor(c);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#FDFD96");
                                displayTime.setColor(c);
                            }
                        }
                        else if (item.getPatient().equalsIgnoreCase("")){
                            java.awt.Color c = java.awt.Color.decode("#98FF98");
                            displayTime.setColor(c);
                        }
                    }

                } else if (displayStartTime >= startTime && endTime >= displayEndTime) {
                    displayTime.setPatient("");

                    if (item.getDoctor().equalsIgnoreCase(docName1)) {
                        if (item.getPatient().equalsIgnoreCase(name)) {
                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode("#78B4BF");
                                displayTime.setColor(c);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#DC654D");
                                displayTime.setColor(c);
                            }
                        }
                        else if (item.getPatient().equalsIgnoreCase("")){
                            java.awt.Color c = java.awt.Color.decode("#78B4BF");
                            displayTime.setColor(c);
                        }
                    } else if (item.getDoctor().equalsIgnoreCase(docName2)) {
                        if (item.getPatient().equalsIgnoreCase(name)) {
                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode("#98FF98");
                                displayTime.setColor(c);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#FDFD96");
                                displayTime.setColor(c);
                            }
                        }
                        else if (item.getPatient().equalsIgnoreCase("")){
                            java.awt.Color c = java.awt.Color.decode("#98FF98");
                            displayTime.setColor(c);
                        }
                    }
                }
            }
        }
        return FXCollections.observableArrayList(toTableItems);
    }

    @FXML
    void freeAppointment() {
        String appointment = appointmentList.getSelectionModel().getSelectedItem();
        String[] parts = appointment.split(" - ");

        String[] startTime = parts[0].split(":");
        String[] endTime = parts[1].split(":");
        String name = parts[2].replaceAll("\\s", "");

        for (Appointment a : appointments)
            if (a.getStartHour() == Integer.parseInt(startTime[0]) && a.getStartMin() == Integer.parseInt(startTime[1]) &&
                    a.getEndHour() == Integer.parseInt(endTime[0]) && a.getEndMin() == Integer.parseInt(endTime[1]) && a.getPatient().equals(name)) {
                deleteAppointment(a.getAppointmentID());
                mc.refreshAll();
                break;
            }

        appointments.sort(Comparator.comparingInt(Appointment::getStartHour));
        appointments = DBController.getAppointments();

        if (monthBox.isSelected()) {
            for (Appointment a :  appointments) {
                if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getPatient().equals(name)) {
                    String startMin, endMin;
                    if (a.getStartMin() == 0)
                        startMin = "00";
                    else
                        startMin = "30";
                    if (a.getEndMin() == 0)
                        endMin = "00";
                    else
                        endMin = "30";
                    appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                            + " - " + a.getPatient() + " - " + a.getDoctor());
                }
            }
        } else if (weekBox.isSelected()) {

        } else {
            for (Appointment a :  appointments) {
                if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && a.getPatient().equals(name)) {
                    String startMin, endMin;
                    if (a.getStartMin() == 0)
                        startMin = "00";
                    else
                        startMin = "30";
                    if (a.getEndMin() == 0)
                        endMin = "00";
                    else
                        endMin = "30";
                    appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                            + " - " + a.getPatient() + " - " + a.getDoctor());
                }
            }
        }
        mc.refreshAll();
    }

    private void deleteAppointment(int ID) {
        appointments.clear();
        dbController.loadAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() == ID) {
                Appointment a = appointment;
                //GINALAW KO TO -CHESIE
                dbController.updateAppointmentPatient(0, "", a.getDoctor(), a.getDay(), a.getMonth(), a.getYear(), a.getStartHour(), a.getStartMin(), a.getEndHour(), a.getEndMin());
                break;
            }
        }
    }

    @FXML
    void displayWeekView() {
        doctor1WeekTag.setText("Doctor " + docName1);
        doctor2WeekTag.setText("Doctor " + docName2);
    }

    @Override
    ObservableList<WeekTableItem> initializeWeekView(Calendar forWeekCalendar) {
        return null;
    }

    @FXML
    void displayAgenda() {
        appointmentList.getItems().clear();
        agendaDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

        appointments.sort(Comparator.comparingInt(Appointment::getStartHour));

        for (Appointment a : appointments)
            if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && a.getPatient().equals(name)) {
                String startMin, endMin;
                if (a.getStartMin() == 0)
                    startMin = "00";
                else
                    startMin = "30";
                if (a.getEndMin() == 0)
                    endMin = "00";
                else
                    endMin = "30";
                appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                        + " - " + a.getPatient() + " - " + a.getDoctor());
            }
    }

    @FXML
    void displayWeeklyAgenda () {
//		dbController.loadAppointments();
//		if (weekBox.isSelected()) {
//			monthBox.setSelected(false);
//
//			if (agendaDoc1Box.isSelected()) {
//
//			} else if (agendaDoc2Box.isSelected()) {
//
//			}
//			// TODO display all appointments for the week
//		} else if (!(weekBox.isSelected() && monthBox.isSelected())) {
//			dbController.loadAppointments();
//			displayEventsToday();
//		}
    }

    @FXML
    void displayMonthlyAgenda() {
        appointmentList.getItems().clear();

        if (monthBox.isSelected()) {
            weekBox.setSelected(false);

            if (agendaDoc1Box.isSelected()) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getPatient().equals(name) && a.getDoctor().equals(docName1)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                                + " - " + a.getPatient() + " - " + a.getDoctor());
                    }
            } else if (agendaDoc2Box.isSelected()) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getPatient().equals(name) && a.getDoctor().equals(docName2)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                                + " - " + a.getPatient() + " - " + a.getDoctor());
                    }
            } else if (!(agendaDoc1Box.isSelected() && agendaDoc2Box.isSelected())) {
                for (Appointment a : appointments)
                    if (a.getMonth() == monthToday && a.getYear() == yearToday && a.getPatient().equals(name)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(convert(a.getMonth()) + " " + a.getDay() + ", " + a.getYear() + " - "
                                + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient() + " - " + a.getDoctor());
                    }
            }
        } else if (!(monthBox.isSelected() && weekBox.isSelected())) {
            for (Appointment a : appointments)
                if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && (a.getDoctor().equals(docName1) || a.getDoctor().equals(docName2))) {
                    String startMin, endMin;
                    if (a.getStartMin() == 0)
                        startMin = "00";
                    else
                        startMin = "30";
                    if (a.getEndMin() == 0)
                        endMin = "00";
                    else
                        endMin = "30";
                    appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                            + " - " + a.getPatient() + " - " + a.getDoctor());
                }
        }
    }

    @FXML
    void doc1Agenda() {
        appointmentList.getItems().clear();

        if (agendaDoc1Box.isSelected()) {

            if (monthBox.isSelected()) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getPatient().equals(name) && a.getDoctor().equals(docName1)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(convert(a.getMonth()) + " " + a.getDay() + ", " + a.getYear() + " - "
                                + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient() + " - " + a.getDoctor());
                    }


            } else if (weekBox.isSelected()) {

            } else if (!(monthBox.isSelected() && weekBox.isSelected())) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && a.getPatient().equals(name) && a.getDoctor().equals(docName1)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(convert(a.getMonth()) + " " + a.getDay() + ", " + a.getYear() + " - "
                                + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient() + " - " + a.getDoctor());
                    }

            }

        } else if (!agendaDoc1Box.isSelected()) {
            if (monthBox.isSelected()) {
                for (Appointment a : appointments)
                    if (a.getMonth() == monthToday && a.getYear() == yearToday && a.getPatient().equals(name)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(convert(a.getMonth()) + " " + a.getDay() + ", " + a.getYear() + " - "
                                + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient() + " - " + a.getDoctor());
                    }
            } else if (weekBox.isSelected()) {

            } else if (!(weekBox.isSelected() && monthBox.isSelected())) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && (a.getDoctor().equals(docName1) || a.getDoctor().equals(docName2))) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                                + " - " + a.getPatient() + " - " + a.getDoctor());
                    }
            }
        }
    }

    @FXML
    void doc2Agenda() {

        appointmentList.getItems().clear();

        if (agendaDoc2Box.isSelected()) {

            if (monthBox.isSelected()) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getPatient().equals(name) && a.getDoctor().equals(docName2)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(convert(a.getMonth()) + " " + a.getDay() + ", " + a.getYear() + " - "
                                + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient() + " - " + a.getDoctor());
                    }


            } else if (weekBox.isSelected()) {

            } else if (!(monthBox.isSelected() && weekBox.isSelected())) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && a.getPatient().equals(name) && a.getDoctor().equals(docName2)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(convert(a.getMonth()) + " " + a.getDay() + ", " + a.getYear() + " - "
                                + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient() + " - " + a.getDoctor());
                    }

            }

        } else if (!agendaDoc2Box.isSelected()) {
            if (monthBox.isSelected()) {
                for (Appointment a : appointments)
                    if (a.getMonth() == monthToday && a.getYear() == yearToday && a.getPatient().equals(name)) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(convert(a.getMonth()) + " " + a.getDay() + ", " + a.getYear() + " - "
                                + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient() + " - " + a.getDoctor());
                    }
            } else if (weekBox.isSelected()) {

            } else if (!(weekBox.isSelected() && monthBox.isSelected())) {
                for (Appointment a : appointments)
                    if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && (a.getDoctor().equals(docName1) || a.getDoctor().equals(docName2))) {
                        String startMin, endMin;
                        if (a.getStartMin() == 0)
                            startMin = "00";
                        else
                            startMin = "30";
                        if (a.getEndMin() == 0)
                            endMin = "00";
                        else
                            endMin = "30";
                        appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin
                                + " - " + a.getPatient() + " - " + a.getDoctor());
                    }
            }
        }

    }

    PatientScreenController(MainController mc) {
            this.mc = mc;
    }

    // TODO filter day view to only doc1 when checkbox is selected
    @FXML
    private void doc1Only () {

    }

    // TODO filter day view to only doc2 when checkbox is selected
    @FXML
    private void doc2Only () {

    }

    @Override
    public void initialize (URL location, ResourceBundle resources){
        profileDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

        dbController = new DBController();

        userTag.setText("Welcome Patient " + name);

        Calendar cal = Calendar.getInstance();
        yearToday = cal.get(GregorianCalendar.YEAR);
        monthToday = cal.get(GregorianCalendar.MONTH) + 1;
        dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);

        yearToday = cal.get(GregorianCalendar.YEAR);
        monthToday = cal.get(GregorianCalendar.MONTH);
        dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);

        initializeBoxes();
        for (int i = 0; i < dbController.getDoctors().size(); i++) {
            System.out.println(dbController.getDoctors().get(i).getName());
            doctorBox.getItems().add(dbController.getDoctors().get(i).getName());
        }

        appointments = DBController.getAppointments();
        profileDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
        refreshCalendar(monthToday, yearToday, dayToday);
        //		checkIfHaveAppointment();
    }

    @FXML
    private void reserveNewAppointment() {
        System.out.println("reserved!");
        appointments.clear();
        dbController.loadAppointments();

        int appointmentID;
        if(appointments.size() ==0)
            appointmentID = appointments.size()+1;
        else{
            int max =appointments.get(0).getAppointmentID();
            for(int i=1; i<appointments.size(); i++){
                if(max < appointments.get(i).getAppointmentID())
                    max = appointments.get(i).getAppointmentID();
            }
            appointmentID = max;
        }

        String patient = name;
        String doctor = doctorBox.getSelectionModel().getSelectedItem();
        int day = datePicker.getValue().getDayOfMonth();
        int month = datePicker.getValue().getMonthValue() - 1;
        int year = datePicker.getValue().getYear();
        int status = 1;

        int startTime = (Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[0]) * 100) +
                (Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[1]));
        int endTime = (Integer.parseInt(endTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[0]) * 100) +
                (Integer.parseInt(endTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[1]));

        int startTimeBook = startTime;
        int endTimeBook = endTime;

        int starthour = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().split(":")[0]);
        int startmin = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().split(":")[1]);
        int endhour;
        int endmin;

        int starthourBook = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().split(":")[0]);
        int startminBook = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().split(":")[1]);
        int endhourBook;
        int endminBook;

        boolean bookIt = true;

        System.out.println(endTime +" > "+ startTime);
        do{
            if(startmin == 30){
                endhour = starthour + 1;
                endmin = 00;
            }
            else {
                endhour = starthour;
                endmin = 30;
            }
            System.out.println("starthour = " +starthour+ " startmin = " +startmin);
            System.out.println("endhour = " +endhour+ " endmin = " +endmin);

            Appointment newApp = new Appointment (appointmentID, patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);

            //check for conflict first
            if (isValidTime(newApp)) {
                System.out.println("valid time");
                bookIt = true;
            } else {
                System.out.println("Invalid appointment slot/ Unavailable slot");
                bookIt = false;
                profilePane.setVisible(true);
                break;
            }

            starthour = endhour;
            startmin = endmin;
            startTime = (starthour * 100) + startmin;
            System.out.println(endTime +" > "+ startTime);
        }while (endTime > startTime);

        if (bookIt){
            while (endTimeBook > startTimeBook){
                if(startminBook == 30){
                    endhourBook = starthourBook + 1;
                    endminBook = 00;
                }
                else {
                    endhourBook = starthourBook;
                    endminBook = 30;
                }

                dbController.updateAppointmentPatient(1, name, doctor, day, month, year, starthourBook, startminBook, endhourBook, endminBook);

                starthourBook = endhourBook;
                startminBook = endminBook;
                startTimeBook = (starthourBook * 100) + startminBook;
            }
        }
        refreshCalendar(monthToday, yearToday, dayToday);
        mc.refreshAll();
    }

    private boolean isValidTime(Appointment appo) { //GINALAW KO TO -CHESIE
        for (int i=0; i<dbController.getAppointments().size(); i++) {
            Appointment a = dbController.getAppointments().get(i);

            //filter per doctor
            System.out.println(appo.getDoctor()+ " vs " +a.getDoctor());
            if (appo.getMonth() == a.getMonth() &&
                    appo.getDay() == a.getDay() &&
                    appo.getYear() == a.getYear() &&
                    appo.getDoctor().equalsIgnoreCase(a.getDoctor())){

                if (a.getStartHour() == appo.getStartHour() &&
                        a.getStartMin() == appo.getStartMin() &&
                        a.getEndHour() == appo.getEndHour() &&
                        a.getEndMin() == appo.getEndMin() &&
                        a.getStatus() == 0){
                    return true;
                }
            }
        }
        return false;
    }

    boolean now(int day) {
        for (Appointment app : appointments)
            if (app.getYear() == yearToday && app.getMonth() == monthToday && app.getDay() == day && app.getPatient().equals(name))
                return true;
        return false;
    }

    boolean eventToday(Appointment a, int day) {
        if (a.getYear() == yearToday)
            if (a.getMonth() == monthToday && a.getPatient().equals(name))
                return a.getDay() == day;
        return false;
    }

    public static void setName (String name){
        PatientScreenController.name = name;
    }

    public static void setDoc1Name (String name){
        docName1 = name;
    }

    public static void setDoc2Name (String name){
        docName2 = name;
    }

    private static String name;
    private static String docName1;
    private static String docName2;
    private MainController mc;
}
