package gui;


import database.Appointment;
import database.DBController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class AbstractController {

    @FXML protected Label userTag;

    @FXML protected Label profileDateLabel;

    @FXML protected Label agendaDateLabel;

    @FXML protected Label userDayTag;

    @FXML protected Label dayDateTag;

    @FXML protected CheckBox doctor1WeekTag;

    @FXML protected CheckBox doctor2WeekTag;

    @FXML protected CheckBox agendaDoc1Box;

    @FXML protected CheckBox agendaDoc2Box;

    @FXML protected AnchorPane eventPane;

    @FXML protected GridPane miniCalendar;

    @FXML protected AnchorPane profilePane;

    @FXML protected ComboBox<String> startTimeBox;

    @FXML protected ComboBox<String> endTimeBox;

    @FXML protected DatePicker datePicker;

    @FXML protected CheckBox weekBox;

    @FXML protected CheckBox monthBox;

    @FXML protected CheckBox doc1DayFilter;

    @FXML protected CheckBox doc2DayFilter;

    @FXML protected TableView<DayTableItem> dayTable;

    @FXML protected TableColumn<DayTableItem, String> dayTimeColumn;

    @FXML protected TableColumn<DayTableItem, String> dayPatientColumn;

    @FXML protected TableView<WeekTableItem> weekTable;

    @FXML protected TableColumn<WeekTableItem, String> weekTimeColumn;

    @FXML protected TableColumn<WeekTableItem, String> sundayColumn;

    @FXML protected TableColumn<WeekTableItem, String> mondayColumn;

    @FXML protected TableColumn<WeekTableItem, String> tuesdayColumn;

    @FXML protected TableColumn<WeekTableItem, String> wednesdayColumn;

    @FXML protected TableColumn<WeekTableItem, String> thursdayColumn;

    @FXML protected TableColumn<WeekTableItem, String> fridayColumn;

    @FXML protected TableColumn<WeekTableItem, String> saturdayColumn;

    @FXML protected ListView<String> appointmentList;

    @FXML
    abstract void displayDayView();

    abstract ObservableList<DayTableItem> initializeDayView();

    @FXML
    abstract void displayWeekView();

    abstract ObservableList<WeekTableItem> initializeWeekView(Calendar forWeekCalendar);

    @FXML
    abstract void displayAgenda();

    @FXML
    abstract void displayWeeklyAgenda();

    @FXML
    abstract void displayMonthlyAgenda();

    @FXML
    private void cancel() {
        profilePane.setVisible(true);
    }

    @FXML
    private void nextMonth() {
        if (monthToday == 11) {
            monthToday = 0;
            yearToday += 1;
        } else
            monthToday += 1;

        profileDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
        refreshCalendar(monthToday, yearToday, dayToday);
    }

    @FXML
    private void prevMonth() {
        if (monthToday == 0) {
            monthToday = 11;
            yearToday -= 1;
        } else
            monthToday -= 1;

        refreshCalendar(monthToday, yearToday, dayToday);
        profileDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
    }

    @FXML
    void refreshCalendar(int month, int year, int day) {
        miniCalendar.getChildren().clear();

        profileDateLabel.setText(convert(month) + " " + day + ", " + year);

        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        int nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        int som = cal.get(GregorianCalendar.DAY_OF_WEEK);

        for (int i = 1; i <= nod; i++) {
            int row = (i + som - 2) / 7;
            int column = (i + som - 2) % 7;

            Button button = new Button(String.valueOf(i));
            button.setMaxSize(35, 40);

            GridPane finalTemp = miniCalendar;
            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");
            button.setOnAction((ActionEvent event) -> {
                dayToday = Integer.parseInt(((Button) event.getSource()).getText());
                button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 13px; -fx-background-color: #8FCFDA; -fx-text-fill: #FFFFFF");
                profileDateLabel.setText(convert(month) + " " + dayToday + ", " + year);

                // TODO implementation of appointment slot
                for (Node node : finalTemp.getChildren()) {
                    if (node instanceof Button && Integer.parseInt(((Button) node).getText()) != dayToday) {
                        if (now(Integer.parseInt(((Button) node).getText())))
                            node.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 13px; -fx-background-color: #DC654D; -fx-text-fill: #FFFFFF; -fx-background-radius: 15px");
                        else
                            node.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");
                    }
                }
            });

            for (Appointment app: appointments)
                if(eventToday(app, i)) {
                    button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 13px; -fx-background-color: #DC654D; -fx-text-fill: #FFFFFF; -fx-background-radius: 15px");
                    break;
                }

            miniCalendar.add(button, column, row);
        }
    }

    String convert(int month) {
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

    abstract boolean now(int day);

    abstract boolean eventToday(Appointment a, int day);

    void initializeBoxes() {
        for (int i = 8; i < 17; i++) {
            for (int j = 0; j < 60; j += 30) {
                if (j % 60 == 0) {
                    startTimeBox.getItems().add(i + ":00");
                    endTimeBox.getItems().add(i + ":00");
                }
                else {
                    startTimeBox.getItems().add(i + ":30");
                    endTimeBox.getItems().add(i + ":30");
                }
            }
        }

        startTimeBox.getItems().add("17:00");
        endTimeBox.getItems().add("17:00");
    }

    int yearToday;
    int monthToday;
    int dayToday;
    protected ObservableList<Appointment> appointments;
    DBController dbController;
}
