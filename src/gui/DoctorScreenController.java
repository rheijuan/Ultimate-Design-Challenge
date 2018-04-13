package gui;

import database.Appointment;
import database.DBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.net.URL;
import java.util.*;

public class DoctorScreenController implements Initializable {

    @FXML private Label doctorTag;

    @FXML private Label dateLabel;

    @FXML private Label dayLabel;

    @FXML private Label agendaLabel;

    @FXML private Label weekLabel;

    @FXML private GridPane miniCalendar;

    @FXML private ComboBox<String> sTimeHour;

    @FXML private ComboBox<String> sTimeMin;

    @FXML private ComboBox<String> eTimeHour;

    @FXML private ComboBox<String> eTimeMin;

    @FXML private AnchorPane profilePane;

    @FXML private TableColumn<DayTableItem, String> timeColumn;

    @FXML private TableColumn<DayTableItem, String> patientColumn;

    @FXML private TableView<DayTableItem> dayTableView;

    @FXML private ListView<String> appointmentList;

    @FXML
    private void nextMonth() {
        if (monthToday == 11) {
            monthToday = 0;
            yearToday += 1;
        } else
            monthToday += 1;

        dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
        refreshCalendar(monthToday, yearToday, dayToday);
    }

    @FXML
    private void prevMonth() {
        if (monthToday == 0) {
            monthToday = 11;
            yearToday -= 1;
        } else
            monthToday -= 1;

        dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
        refreshCalendar(monthToday, yearToday, dayToday);
    }

    @FXML
    private void setAppointment() {
        profilePane.setVisible(false);

        for (int i = 8; i <= 17; i++) {
            eTimeHour.getItems().add(String.valueOf(i));
            sTimeHour.getItems().add(String.valueOf(i));
        }

        sTimeMin.getItems().add("00");
        sTimeMin.getItems().add("30");
        eTimeMin.getItems().add("00");
        eTimeMin.getItems().add("30");
    }

    @FXML
    private void cancel() {
        profilePane.setVisible(true);
    }

    @FXML
    private void displayDayView() {
        dayLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);

        ObservableList<DayTableItem> data = initializeDayView();
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));

        patientColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String event, boolean empty) {
                super.updateItem(event, empty);

                if (event == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(event);
                    DayTableItem currentItem = getTableView().getItems().get(getIndex());
                    if (currentItem.getColor() != null) {
                        setTextFill(javafx.scene.paint.Color.WHITE);

                        if (currentItem.getColor().equals(Color.decode("#78B4BF")))
                            setStyle("-fx-background-color: #78B4BF");
                        else if (currentItem.getColor().equals(Color.decode("#DC654D")))
                            setStyle("-fx-background-color: #DC654D");

                    } else {
                        setTextFill(javafx.scene.paint.Color.BLACK);
                        setStyle("");
                    }
                }
            }
        });

        dayTableView.setItems(data);
    }

    private ObservableList<DayTableItem> initializeDayView() {
        ArrayList<Appointment> itemsToDisplay = new ArrayList<>();

        ArrayList<DayTableItem> toTableItems = new ArrayList<>();

        for (int hour = 8; hour <= 17; hour++)
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

        for (Appointment app : appointments)
            if (app.getMonth() == monthToday && app.getDay() == daySelected && app.getYear() == yearToday)
                itemsToDisplay.add(app);

        itemsToDisplay.sort(Comparator.comparingInt(Appointment::getStartHour)
                .thenComparingInt(Appointment::getStartMinute));

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

                    if (item.getStatus() == 0) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c);
                    }
                    else if (item.getStatus() == 1) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c);
                    }

                    break;
                } else if (displayStartTime == startTime) {
                    displayTime.setPatient(item.getPatient());

                    if (item.getStatus() == 0) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c);
                    }
                    else if (item.getStatus() == 1) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c);
                    }

                } else if (displayStartTime >= startTime && endTime >= displayEndTime) {
                    displayTime.setPatient(" ");

                    if (item.getStatus() == 0) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c);
                    }
                    else if (item.getStatus() == 1) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c);
                    }
                }
            }
        }
        return FXCollections.observableArrayList(toTableItems);
    }

    @FXML
    private void displayWeekView() {
        weekLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);
        // TODO implement the week view
    }

    @FXML
    private void displayAgendaView() {
        agendaLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);

        ObservableList<String> scheduledAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : appointments)
            if (eventToday(appointment, daySelected))
                if (!appointment.getPatient().equals(""))
                    scheduledAppointments.add(appointment.getStartHour() + ":"
                            + appointment.getStartMin() + "-" + appointment.getEndHour() + ":" + appointment.getEndMin() +
                            " -- " + appointment.getPatient());

        appointmentList.setItems(scheduledAppointments);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbController = new DBController();
        appointments = DBController.getAppointments();

        doctorTag.setText("Welcome Doctor " + docName);

        Calendar cal = Calendar.getInstance();
        yearToday = cal.get(GregorianCalendar.YEAR);
        monthToday = cal.get(GregorianCalendar.MONTH) + 1;
        dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);

        daySelected = dayToday;

        dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
        refreshCalendar(monthToday, yearToday, dayToday);

    }

    private void refreshCalendar(int month, int year, int day) {
        month -= 1;
        miniCalendar.getChildren().clear();

        dateLabel.setText(convert(month) + " " + day + ", " + year);

        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        int nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        int som = cal.get(GregorianCalendar.DAY_OF_WEEK);

        for (int i = 1; i <= nod; i++) {
            int row = (i + som - 2) / 7;
            int column = (i + som - 2) % 7;

            Button button = new Button(String.valueOf(i));
            button.setMaxSize(28, 35);

            GridPane finalTemp = miniCalendar;
            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");
            int finalMonth = month;
            button.setOnAction((ActionEvent event) -> {
                daySelected = Integer.parseInt(((Button) event.getSource()).getText());
                button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color:  #8FCFDA; -fx-text-fill: #FFFFFF");
                dateLabel.setText(convert(finalMonth) + " " + daySelected + ", " + year);

                for (Node node : finalTemp.getChildren()) {
                    if (node instanceof Button && Integer.parseInt(((Button) node).getText()) != daySelected) {
                        node.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");
                    }
                }
            });

            // Highlights the date when an event is on the day
            for (Appointment app: appointments)
                if(eventToday(app, i)) {
                    button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color:  #dc654d; -fx-text-fill: #FFFFFF");
                    break;
                }

            miniCalendar.add(button, column, row);
        }
    }

    private boolean eventToday(Appointment a, int day) {
        if (a.getYear() == yearToday)
            if (a.getMonth() == monthToday)
                return a.getDay() == day;

        return false;
    }

    private String convert(int month) {
        switch (month) {
            case 0: return "January";
            case 1: return "February";
            case 2: return "March";
            case 3: return "April";
            case 4: return "May";
            case 5: return "June";
            case 6: return "July";
            case 7: return "August";
            case 8: return "September";
            case 9: return "October";
            case 10: return "November";
            case 11: return "December";
        }
        return "January";
    }

    public static void setName(String name) {
        docName = name;
    }

    private int yearToday;
    private int monthToday;
    private int dayToday;
    private int daySelected;
    private DBController dbController;
    private ObservableList<Appointment> appointments;
    private static String docName;
}