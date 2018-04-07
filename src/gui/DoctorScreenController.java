package gui;

import database.Appointment;
import database.DBController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class DoctorScreenController implements Initializable {

    @FXML private Label dateLabel,dayLabel,weekLabel,agendaLabel;
    @FXML private GridPane miniCalendar;

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
    private void displayDateView() {
        dayLabel.setText(convert(monthToday) + " " + daySelected + ", " + yearToday);
    }

    @FXML
    private void displayWeekView() {
        weekLabel.setText(convert(monthToday) + " " + yearToday);
    }

    @FXML
    private void displayAgendaView() {
        agendaLabel.setText(convert(monthToday) + " " + daySelected + ", " + yearToday);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appointments = DBController.getAppointments();
        Calendar cal = Calendar.getInstance();

        yearToday = cal.get(GregorianCalendar.YEAR);
        monthToday = cal.get(GregorianCalendar.MONTH);
        dayToday = cal.get(GregorianCalendar.DAY_OF_WEEK);

        daySelected = dayToday;

        refreshCalendar(monthToday, yearToday, dayToday);
    }

    private void refreshCalendar(int month, int year, int day) {
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
            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");
            button.setOnAction(event -> {
                daySelected = Integer.parseInt(((Button) event.getSource()).getText());
                button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color:  #dc654d; -fx-text-fill: #FFFFFF");
                dateLabel.setText(convert(month) + " " + daySelected + ", " + year);
                for (Node node : miniCalendar.getChildren()) {
                    if (node instanceof Button && Integer.parseInt(((Button) node).getText()) != daySelected)
                        node.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");
                }
            });

            miniCalendar.add(button, column, row);
        }
    }

    private String convert(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "January";
    }

    private int yearToday;
    private int monthToday;
    private int dayToday;
    private int daySelected;
    private ObservableList<Appointment> appointments;
}