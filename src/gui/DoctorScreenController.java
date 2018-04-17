package gui;

import database.Appointment;
import database.DBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.print.Doc;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class DoctorScreenController extends AbstractController implements Initializable, ControllerParent {

    @FXML
    private CheckBox mondayBox;

    @FXML
    private CheckBox tuesdayBox;

    @FXML
    private CheckBox wednesdayBox;

    @FXML
    private CheckBox thursdayBox;

    @FXML
    private CheckBox fridayBox;

    @FXML
    private CheckBox saturdayBox;

    @FXML
    private CheckBox everyMonthBox;

    @FXML
    private CheckBox everydayBox;

    @FXML
    private Label userWeekTag;

    @FXML
    void displayDayView() {
        userDayTag.setText("Doctor " + name);
        dayDateTag.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

        ObservableList<DayTableItem> data = initializeDayView();
        dayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dayPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));

        dayPatientColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String event, boolean empty) {
                super.updateItem(event, empty);

                if (event == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    DayTableItem currentItem = getTableView().getItems().get(getIndex());

                    if (currentItem.getColor() != null) {
                        setTextFill(javafx.scene.paint.Color.WHITE);

                        if (currentItem.getColor().equals(java.awt.Color.decode("#78B4BF")))
                            setStyle("-fx-background-color: #78B4BF");
                        else if (currentItem.getColor().equals(java.awt.Color.decode("#DC654D"))) {
                            setText(event);
                            setStyle("-fx-background-color: #DC654D");
                        }
                    } else {
                        setTextFill(javafx.scene.paint.Color.BLACK);
                        setStyle("");
                    }
                }
            }
        });
        dayTable.setItems(data);
    }

    ObservableList<DayTableItem> initializeDayView() {
        ArrayList<Appointment> itemsToDisplay = new ArrayList<>();

        ArrayList<DayTableItem> toTableItems = new ArrayList<>();

        for (int hour = 8; hour < 17; hour++)
            for (int min = 0; min <= 30; min += 30) {

                if (min < 30) {
                    toTableItems.add(new DayTableItem(hour + ":" + String.format("%02d", min), "", 3));
                    toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueEndMin(min + 29);
                } else {
                    toTableItems.add(new DayTableItem("", "", 3));
                    toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
                }
            }

        for (Appointment app : appointments)
            if (app.getMonth() == monthToday && app.getDay() == dayToday && app.getYear() == yearToday)
                itemsToDisplay.add(app);

        itemsToDisplay.sort(Comparator.comparingInt(Appointment::getStartHour)
                .thenComparingInt(Appointment::getStartMinute));

        for (Appointment item : itemsToDisplay) {
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

            for (DayTableItem displayTime : toTableItems) {
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
                    setAppointmentColors(item, displayTime);

                    break;
                } else if (displayStartTime == startTime) {
                    displayTime.setPatient(item.getPatient());
                    setAppointmentColors(item, displayTime);

                } else if (displayStartTime >= startTime && endTime >= displayEndTime) {
                    displayTime.setPatient("");
                    setAppointmentColors(item, displayTime);
                }
            }
        }

        return FXCollections.observableArrayList(toTableItems);
    }

    private void setAppointmentColors(Appointment item, DayTableItem displayTime) {
        if (item.getStatus() == 0 && item.getDoctor().equals(name)) {
            Color c = Color.decode("#78B4BF");
            displayTime.setColor(c);
        } else if (item.getStatus() == 1 && item.getDoctor().equals(name)) {
            Color c = Color.decode("#DC654D");
            displayTime.setColor(c);
        }
    }

    @FXML
    void displayWeekView() {
        userWeekTag.setText("Doctor " + name);
        Date dateForWeek = new Date();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
        SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

        cal.set(Calendar.MONTH, monthToday - 1);
        cal.set(Calendar.DATE, dayToday);
        cal.set(Calendar.YEAR, yearToday);

        int startWeekValue = -(cal.get(Calendar.DAY_OF_WEEK) - 1);

        cal.add(Calendar.DATE, startWeekValue);

        ObservableList<WeekTableItem> data = initializeWeekView(cal);
        weekTimeColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("time"));
        sundayColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("sunEvent"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("monEvent"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("tueEvent"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("wedEvent"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("thuEvent"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("friEvent"));
        saturdayColumn.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("satEvent"));

        weekTimeColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String sunEvent, boolean empty) {
                    super.updateItem(sunEvent, empty);

                    if (sunEvent == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());
                        if (currentItem.getSunColor() != null) {
                            setTextFill(javafx.scene.paint.Color.WHITE);

                            if (currentItem.getSunColor().equals(java.awt.Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                            } else if (currentItem.getSunColor().equals(java.awt.Color.decode("#DC654D"))) {
                                setText(sunEvent);
                                setStyle("-fx-background-color: #DC654D");
                            }
//                            } else if (currentItem.getSunColor() == javafx.scene.paint.Color.GREEN) {
//                                setStyle("-fx-background-color: #98FF98");
//                            } else if (currentItem.getSunColor() == javafx.scene.paint.Color.YELLOW) {
//                                setTextFill(javafx.scene.paint.Color.BLACK);
//                                setStyle("-fx-background-color: #FDFD96");
//                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                        }
                    }
                }
            };
        });

        mondayColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String monEvent, boolean empty) {
                    super.updateItem(monEvent, empty);

                    if (monEvent == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getMonColor() != null) {
                            setTextFill(javafx.scene.paint.Color.WHITE);

                            if (currentItem.getSunColor().equals(java.awt.Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                            } else if (currentItem.getSunColor().equals(java.awt.Color.decode("#DC654D"))) {
                                setText(monEvent);
                                setStyle("-fx-background-color: #DC654D");
                            }

//                            if (currentItem.getMonColor() == javafx.scene.paint.Color.CYAN) {
//                                setStyle("-fx-background-color: #78B4BF");
//                            } else if (currentItem.getMonColor() == javafx.scene.paint.Color.ORANGE) {
//                                setStyle("-fx-background-color: #DC654D");
//                            } else if (currentItem.getMonColor() == javafx.scene.paint.Color.GREEN) {
//                                setStyle("-fx-background-color: #98FF98");
//                            } else if (currentItem.getMonColor() == javafx.scene.paint.Color.YELLOW) {
//                                setTextFill(javafx.scene.paint.Color.BLACK);
//                                setStyle("-fx-background-color: #FDFD96");
//                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                            setStyle("");
                        }
                    }
                }
            };
        });

        tuesdayColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String tueEvent, boolean empty) {
                    super.updateItem(tueEvent, empty);

                    if (tueEvent == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getMonColor() != null) {
                            setTextFill(javafx.scene.paint.Color.WHITE);

                            if (currentItem.getSunColor().equals(java.awt.Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                            } else if (currentItem.getSunColor().equals(java.awt.Color.decode("#DC654D"))) {
                                setText(tueEvent);
                                setStyle("-fx-background-color: #DC654D");
                            }

//                            if (currentItem.getTueColor() == javafx.scene.paint.Color.CYAN) {
//                                setStyle("-fx-background-color: #78B4BF");
//                            } else if (currentItem.getTueColor() == javafx.scene.paint.Color.ORANGE) {
//                                setStyle("-fx-background-color: #DC654D");
//                            } else if (currentItem.getTueColor() == javafx.scene.paint.Color.GREEN) {
//                                setStyle("-fx-background-color: #98FF98");
//                            } else if (currentItem.getTueColor() == javafx.scene.paint.Color.YELLOW) {
//                                setTextFill(javafx.scene.paint.Color.BLACK);
//                                setStyle("-fx-background-color: #FDFD96");
//                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                            setStyle("");
                        }
                    }
                }
            };
        });

        wednesdayColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String wedEvent, boolean empty) {
                    super.updateItem(wedEvent, empty);

                    if (wedEvent == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getMonColor() != null) {
                            setTextFill(javafx.scene.paint.Color.WHITE);

                            if (currentItem.getSunColor().equals(java.awt.Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                            } else if (currentItem.getSunColor().equals(java.awt.Color.decode("#DC654D"))) {
                                setText(wedEvent);
                                setStyle("-fx-background-color: #DC654D");
                            }
//                        if (currentItem.getWedColor() != null) {
////                            if (currentItem.getWedColor() == javafx.scene.paint.Color.CYAN) {
////                                setStyle("-fx-background-color: #78B4BF");
////                            } else if (currentItem.getWedColor() == javafx.scene.paint.Color.ORANGE) {
////                                setStyle("-fx-background-color: #DC654D");
////                            } else if (currentItem.getWedColor() == javafx.scene.paint.Color.GREEN) {
////                                setStyle("-fx-background-color: #98FF98");
////                            } else if (currentItem.getWedColor() == javafx.scene.paint.Color.YELLOW) {
////                                setTextFill(javafx.scene.paint.Color.BLACK);
////                                setStyle("-fx-background-color: #FDFD96");
////                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                            setStyle("");
                        }
                    }
                }
            };
        });

        thursdayColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String event, boolean empty) {
                    super.updateItem(event, empty);

                    if (event == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getMonColor() != null) {
                            setTextFill(javafx.scene.paint.Color.WHITE);

                            if (currentItem.getSunColor().equals(java.awt.Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                            } else if (currentItem.getSunColor().equals(java.awt.Color.decode("#DC654D"))) {
                                setText(event);
                                setStyle("-fx-background-color: #DC654D");
                            }

//                            if (currentItem.getThuColor() == javafx.scene.paint.Color.CYAN) {
//                                setStyle("-fx-background-color: #78B4BF");
//                            } else if (currentItem.getThuColor() == javafx.scene.paint.Color.ORANGE) {
//                                setStyle("-fx-background-color: #DC654D");
//                            } else if (currentItem.getThuColor() == javafx.scene.paint.Color.GREEN) {
//                                setStyle("-fx-background-color: #98FF98");
//                            } else if (currentItem.getThuColor() == javafx.scene.paint.Color.YELLOW) {
//                                setTextFill(javafx.scene.paint.Color.BLACK);
//                                setStyle("-fx-background-color: #FDFD96");
//                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                            setStyle("");
                        }
                    }
                }
            };
        });

        fridayColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String event, boolean empty) {
                    super.updateItem(event, empty);

                    if (event == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getMonColor() != null) {
                            setTextFill(javafx.scene.paint.Color.WHITE);

                            if (currentItem.getSunColor().equals(java.awt.Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                            } else if (currentItem.getSunColor().equals(java.awt.Color.decode("#DC654D"))) {
                                setText(event);
                                setStyle("-fx-background-color: #DC654D");
                            }

//                            if (currentItem.getFriColor() == javafx.scene.paint.Color.CYAN) {
//                                setStyle("-fx-background-color: #78B4BF");
//                            } else if (currentItem.getFriColor() == javafx.scene.paint.Color.ORANGE) {
//                                setStyle("-fx-background-color: #DC654D");
//                            } else if (currentItem.getFriColor() == javafx.scene.paint.Color.GREEN) {
//                                setStyle("-fx-background-color: #98FF98");
//                            } else if (currentItem.getFriColor() == javafx.scene.paint.Color.YELLOW) {
//                                setTextFill(javafx.scene.paint.Color.BLACK);
//                                setStyle("-fx-background-color: #FDFD96");
//                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                            setStyle("");
                        }
                    }
                }
            };
        });

        saturdayColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String satEvent, boolean empty) {
                    super.updateItem(satEvent, empty);

                    if (satEvent == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getMonColor() != null) {
                            setTextFill(javafx.scene.paint.Color.WHITE);

                            if (currentItem.getSunColor().equals(java.awt.Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                            } else if (currentItem.getSunColor().equals(java.awt.Color.decode("#DC654D"))) {
                                setText(satEvent);
                                setStyle("-fx-background-color: #DC654D");
                            }

//                            if (currentItem.getSatColor() == javafx.scene.paint.Color.CYAN) {
//                                setStyle("-fx-background-color: #78B4BF");
//                            } else if (currentItem.getSatColor() == javafx.scene.paint.Color.ORANGE) {
//                                setStyle("-fx-background-color: #DC654D");
//                            } else if (currentItem.getSatColor() == javafx.scene.paint.Color.GREEN) {
//                                setStyle("-fx-background-color: #98FF98");
//                            } else if (currentItem.getSatColor() == javafx.scene.paint.Color.YELLOW) {
//                                setTextFill(javafx.scene.paint.Color.BLACK);
//                                setStyle("-fx-background-color: #FDFD96");
//                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                            setStyle("");
                        }
                    }
                }
            };
        });

        weekTable.setItems(data);
    }

    ObservableList<WeekTableItem> initializeWeekView(Calendar forWeekCalendar) {
        ArrayList<Appointment> itemsToDisplay = new ArrayList<>();
        ArrayList<WeekTableItem> toTableItems = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
        SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

        int month = Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime())) - 1;
        int startDay = forWeekCalendar.get(Calendar.DATE);
        String[] date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
        int endDay = forWeekCalendar.get(Calendar.DATE);

        while (!date[0].equalsIgnoreCase("Sat")) {
            forWeekCalendar.add(Calendar.DATE, 1);
            date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
            endDay = Integer.parseInt(date[2]);
        }

        toTableItems.add(new WeekTableItem(""));

        for (int hour = 8; hour <= 17; hour++)
            for (int min = 0; min <= 30; min += 30) {
                if (min < 30) {
                    toTableItems.add(new WeekTableItem(hour + ":" + String.format("%02d", min)));
                    toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueEndMin(min + 29);
                } else {
                    toTableItems.add(new WeekTableItem(""));
                    toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
                }
            }

        for (Appointment item : appointments)
            if (item.getMonth() == month && (item.getDay() >= startDay && item.getDay() <= endDay)
                    && item.getYear() == yearToday)
                itemsToDisplay.add(item);

        int monDate = 0, tueDate = 0, wedDate = 0, thuDate = 0, friDate = 0, satDate = 0, sunDate = 0;

        forWeekCalendar.set(yearToday, month, startDay);
        String compareDay = sdf.format(forWeekCalendar.getTime()).substring(0, 3);

        do {
            if (month == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()))) {
                switch (compareDay.trim()) {
                    case "Sun":
                        sunDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
                        break;
                    case "Mon":
                        monDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
                        break;
                    case "Tue":
                        tueDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
                        break;
                    case "Wed":
                        wedDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
                        break;
                    case "Thu":
                        thuDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
                        break;
                    case "Fri":
                        friDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
                        break;
                    case "Sat":
                        satDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
                        break;
                }

                forWeekCalendar.add(Calendar.DATE, 1);
                compareDay = sdf.format(forWeekCalendar.getTime()).substring(0, 3);
            } else
                break;
        } while (!compareDay.equalsIgnoreCase("Sat"));

        if (monthToday == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime())))
            satDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));

        if (sunDate > 0)
            toTableItems.get(0).setEvent(Integer.toString(sunDate), "Sun");
        if (monDate > 0)
            toTableItems.get(0).setEvent(Integer.toString(monDate), "Mon");
        if (tueDate > 0)
            toTableItems.get(0).setEvent(Integer.toString(tueDate), "Tue");
        if (wedDate > 0)
            toTableItems.get(0).setEvent(Integer.toString(wedDate), "Wed");
        if (thuDate > 0)
            toTableItems.get(0).setEvent(Integer.toString(thuDate), "Thu");
        if (friDate > 0)
            toTableItems.get(0).setEvent(Integer.toString(friDate), "Fri");
        if (satDate > 0)
            toTableItems.get(0).setEvent(Integer.toString(satDate), "Sat");

        for (Appointment item : itemsToDisplay) {
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

            String dayOfItem = null;

            if (sunDate == item.getDay()) {
                dayOfItem = "Sun";
            } else if (monDate == item.getDay()) {
                dayOfItem = "Mon";
            } else if (tueDate == item.getDay()) {
                dayOfItem = "Tue";
            } else if (wedDate == item.getDay()) {
                dayOfItem = "Wed";
            } else if (thuDate == item.getDay()) {
                dayOfItem = "Thu";
            } else if (friDate == item.getDay()) {
                dayOfItem = "Fri";
            } else if (satDate == item.getDay()) {
                dayOfItem = "Sat";
            }

            for (WeekTableItem displayTime : toTableItems) {
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
                    displayTime.setEvent(item.getPatient(), dayOfItem);

                    if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getDoctor().equalsIgnoreCase(name) && item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    }
//                    } else if (item.getDoctor().equalsIgnoreCase(name) ) {
//                        displayTime.setColor(javafx.scene.paint.Color.CYAN, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(name)) {
//                        displayTime.setColor(javafx.scene.paint.Color.GREEN, dayOfItem);
//                    }

                    break;
                } else if (displayStartTime == startTime) {
                    System.out.println(item.getDay());
                    System.out.println(item.getPatient() + " " + dayOfItem);
                    displayTime.setEvent(item.getPatient(), dayOfItem);

                    if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getDoctor().equalsIgnoreCase(name) && item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    }

//                    if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.ORANGE, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.YELLOW, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName)) {
//                        displayTime.setColor(javafx.scene.paint.Color.CYAN, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName)) {
//                        displayTime.setColor(javafx.scene.paint.Color.GREEN, dayOfItem);
//                    }
                } else if (displayStartTime >= startTime && endTime >= displayEndTime) {

                    if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getDoctor().equalsIgnoreCase(name) && item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    }
//                    if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.ORANGE, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.YELLOW, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName) ) {
//                        displayTime.setColor(javafx.scene.paint.Color.CYAN, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName)) {
//                        displayTime.setColor(javafx.scene.paint.Color.GREEN, dayOfItem);
//                    }

                }

                if (displayTime.getTime().equalsIgnoreCase(""))
                    continue;

                if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
                        displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
                    displayTime.setEvent(item.getTitle(), dayOfItem);

                    if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getDoctor().equalsIgnoreCase(name) && item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    }
//                    if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.ORANGE, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.YELLOW, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName) ) {
//                        displayTime.setColor(javafx.scene.paint.Color.CYAN, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName)) {
//                        displayTime.setColor(javafx.scene.paint.Color.GREEN, dayOfItem);
//                    }

                    break;
                } else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
                    displayTime.setEvent(item.getTitle(), dayOfItem);

                    if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getDoctor().equalsIgnoreCase(name) && item.getPatient().equalsIgnoreCase("")) {
                        Color c = Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    }
//                    if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.ORANGE, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                        displayTime.setColor(javafx.scene.paint.Color.YELLOW, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName) ) {
//                        displayTime.setColor(javafx.scene.paint.Color.CYAN, dayOfItem);
//                    } else if (item.getDoctor().equalsIgnoreCase(docName)) {
//                        displayTime.setColor(javafx.scene.paint.Color.GREEN, dayOfItem);
//                    }
                } else {
                    if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour) {
                        if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
                            displayTime.setEvent("", dayOfItem);

                            if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase("")) {
                                Color c = Color.decode("#DC654D");
                                displayTime.setColor(c, dayOfItem);
                            } else if (item.getDoctor().equalsIgnoreCase(name) && item.getPatient().equalsIgnoreCase("")) {
                                Color c = Color.decode("#78B4BF");
                                displayTime.setColor(c, dayOfItem);
                            }
//                            if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                                displayTime.setColor(javafx.scene.paint.Color.ORANGE, dayOfItem);
//                            } else if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                                displayTime.setColor(javafx.scene.paint.Color.YELLOW, dayOfItem);
//                            } else if (item.getDoctor().equalsIgnoreCase(docName) ) {
//                                displayTime.setColor(javafx.scene.paint.Color.CYAN, dayOfItem);
//                            } else if (item.getDoctor().equalsIgnoreCase(docName)) {
//                                displayTime.setColor(javafx.scene.paint.Color.GREEN, dayOfItem);
//                            }

                            break;
                        } else {
                            displayTime.setEvent("", dayOfItem);

                            if (item.getDoctor().equalsIgnoreCase(name) && !item.getPatient().equalsIgnoreCase("")) {
                                Color c = Color.decode("#DC654D");
                                displayTime.setColor(c, dayOfItem);
                            } else if (item.getDoctor().equalsIgnoreCase(name) && item.getPatient().equalsIgnoreCase("")) {
                                Color c = Color.decode("#78B4BF");
                                displayTime.setColor(c, dayOfItem);
                            }
//                            if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                                displayTime.setColor(javafx.scene.paint.Color.ORANGE, dayOfItem);
//                            } else if (item.getDoctor().equalsIgnoreCase(docName) && !item.getPatient().equalsIgnoreCase(" ")) {
//                                displayTime.setColor(javafx.scene.paint.Color.YELLOW, dayOfItem);
//                            } else if (item.getDoctor().equalsIgnoreCase(docName) ) {
//                                displayTime.setColor(javafx.scene.paint.Color.CYAN, dayOfItem);
//                            } else if (item.getDoctor().equalsIgnoreCase(docName)) {
//                                displayTime.setColor(javafx.scene.paint.Color.GREEN, dayOfItem);
//                            }

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
            displayMonthlyEvents();
        } else if (weekBox.isSelected()) {

        } else {
            displayDailyEvents(name);
        }
        mc.refreshAll();
    }

    private void displayDailyEvents(String name) {
        appointmentList.getItems().clear();
        for (Appointment a : appointments)
            if (a.getDoctor().equals(name) && !a.getPatient().equals(""))
                day(a);
    }

    private void displayMonthlyEvents() {
        appointmentList.getItems().clear();
        for (Appointment a : appointments)
            if (a.getMonth() == monthToday && a.getYear() == yearToday && !a.getPatient().equals("") && a.getDoctor().equals(name)) {
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
                        + a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " ** " + a.getPatient());
            }

    }

    private void deleteAppointment(int ID) {
        appointments.clear();
        dbController.loadAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() == ID) {
                // TODO instead of deleting, set the patient to  ""
                dbController.deleteAppointmentForD(appointment.getDoctor(), appointment.getDay(), appointment.getMonth(), appointment.getYear(), appointment.getStartHour(), appointment.getStartMin(), appointment.getEndHour(), appointment.getEndMin());
                mc.refreshAll();
                break;
            }
        }
    }

    @FXML
    void displayAgenda() {

        appointmentList.getItems().clear();
        agendaDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

        appointments.sort(Comparator.comparingInt(Appointment::getStartHour));

        displayDailyEvents(name);
    }

    @FXML
    void displayWeeklyAgenda() {
        if (weekBox.isSelected()) {
            monthBox.setSelected(false);
            // TODO display all appointments for the week
        } else if (!(weekBox.isSelected() && monthBox.isSelected())) {
            appointmentList.getItems().clear();
            displayDailyEvents(name);
        }
    }

    @FXML
    void displayMonthlyAgenda() {
        if (monthBox.isSelected()) {
            appointmentList.getItems().clear();
            weekBox.setSelected(false);
            appointments.sort(Comparator.comparingInt(Appointment::getDay));

            displayMonthlyEvents();
        } else if (!(weekBox.isSelected() && monthBox.isSelected())) {
            appointmentList.getItems().clear();
            for (Appointment a : appointments)
                if (a.getDoctor().equals(name) && !a.getPatient().equals("") && a.getDoctor().equals(name))
                    day(a);
        }
    }

    private void day(Appointment a) {
        if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday) {
            String startMin, endMin;
            if (a.getStartMin() == 0)
                startMin = "00";
            else
                startMin = "30";
            if (a.getEndMin() == 0)
                endMin = "00";
            else
                endMin = "30";
            appointmentList.getItems().add(a.getStartHour() + ":" + startMin + " - " + a.getEndHour() + ":" + endMin + " - " + a.getPatient());
        }
    }

    @FXML
    public void createAppointment() {

        int appointmentID = appointments.size() + 1;
        int day = datePicker.getValue().getDayOfMonth();
        int month = datePicker.getValue().getMonthValue() - 1;
        int year = datePicker.getValue().getYear();

        String[] startTime = startTimeBox.getSelectionModel().getSelectedItem().split(":");
        String[] endTime = endTimeBox.getSelectionModel().getSelectedItem().split(":");

        int starthour = Integer.parseInt(startTime[0]);
        int startmin = Integer.parseInt(startTime[1]);
        int endhour = Integer.parseInt(endTime[0]);
        int endmin = Integer.parseInt(endTime[1]);

        int status = 0;

        Calendar date = Calendar.getInstance();

        int maximum = 0;
        if (monthToday % 2 == 1 && monthToday < 8 && monthToday > 0)
            maximum = 31;
        else if (monthToday % 2 == 0 && monthToday < 8 && monthToday > 0 && monthToday != 2)
            maximum = 30;
        else if (monthToday % 2 == 1 && monthToday > 7 && monthToday < 13)
            maximum = 30;
        else if (monthToday % 2 == 0 && monthToday > 7 && monthToday < 13)
            maximum = 31;

        if (!(mondayBox.isSelected() && tuesdayBox.isSelected() && wednesdayBox.isSelected() && thursdayBox.isSelected() && fridayBox.isSelected() &&
                saturdayBox.isSelected() && everydayBox.isSelected() && everyMonthBox.isSelected())) { // Client booked for only one da
            dbController.loadAppointments();

            for (int i = starthour; i < endhour; i++) {
                for (int j = startmin; i < 60; j += 30) {
                    if (j == 30) {
                        dbController.createAppointment("", name, day, month, year, i, j, i + 1, 0, status);
                        j = 0;
                    } else
                        dbController.createAppointment("", name, day, month, year, i, j, i, 30, status);
                }
            }
        } else {
            for (int i = day; i < maximum; i++) {
                date.set(year, month, i);

                if (mondayBox.isSelected()) { // Client booked appointment every mondays
                    int dayinWeek = i + 2;
                    System.out.println("MONDAY DAY IN A WEEK: " + dayinWeek);

                    dbController.loadAppointments();
                    dbController.createAppointment("", name, dayinWeek, monthToday, yearToday, starthour, startmin, endhour, endmin, status);
                }

                if (tuesdayBox.isSelected()) { // Client booked appointment every tuesdays
                    int dayinWeek = i + 2;
                    System.out.println("TUES DAY IN A WEEK: " + dayinWeek);

                    dbController.loadAppointments();
                    dbController.createAppointment("", name, dayinWeek, monthToday, yearToday, starthour, startmin, endhour, endmin, status);
                }

                if (wednesdayBox.isSelected()) { // Client booked appointment every wednesdays
                    int dayinWeek = i + 2;

                    System.out.println("WED DAY IN A WEEK: " + dayinWeek);
                    dbController.loadAppointments();
                    dbController.createAppointment("", name, dayinWeek, monthToday, yearToday, starthour, startmin, endhour, endmin, status);
                }

                if (thursdayBox.isSelected()) { // Client booked appointment every thursdays
                    int dayinWeek = i + 2;

                    System.out.println("THURS DAY IN A WEEK: " + dayinWeek);

                    dbController.loadAppointments();
                    dbController.createAppointment("", name, dayinWeek, monthToday, yearToday, starthour, startmin, endhour, endmin, status);
                }

                if (fridayBox.isSelected()) { // Client booked appointment every fridays
                    int dayinWeek = i + 2;

                    System.out.println("FRI DAY IN A WEEK: " + dayinWeek);

                    dbController.loadAppointments();
                    dbController.createAppointment("", name, dayinWeek, monthToday, yearToday, starthour, startmin, endhour, endmin, status);
                }

                if (saturdayBox.isSelected()) { // Client booked appointment every saturdays
                    int dayinWeek = i + 2;

                    System.out.println("SAT DAY IN A WEEK: " + dayinWeek);

                    dbController.loadAppointments();
                    dbController.createAppointment("", name, dayinWeek, monthToday, yearToday, starthour, startmin, endhour, endmin, status);
                }

//                if (everyMonthBox.isSelected()) { // Client booked appointment once a month
//
//
//                }

                if (everydayBox.isSelected()) { // Client booked appointment every day **exclude sunday here
                    int dayinWeek = i + 2;

                    dbController.loadAppointments();
                    dbController.createAppointment("", name, dayinWeek, monthToday, yearToday, starthour, startmin, endhour, endmin, status);
                }
            }
        }

        dbController.loadAppointments();
        appointments = DBController.getAppointments();
        profilePane.setVisible(true);
        mc.refreshAll();
        datePicker.getEditor().clear();
        mondayBox.setSelected(false);
        tuesdayBox.setSelected(false);
        wednesdayBox.setSelected(false);
        thursdayBox.setSelected(false);
        fridayBox.setSelected(false);
        saturdayBox.setSelected(false);
        refreshCalendar(monthToday, yearToday, dayToday);
    }

    @FXML
    private void setAppointment () {
        mc.refreshAll();
        profilePane.setVisible(false);
        mondayBox.setSelected(false);
        tuesdayBox.setSelected(false);
        wednesdayBox.setSelected(false);
        thursdayBox.setSelected(false);
        fridayBox.setSelected(false);
        saturdayBox.setSelected(false);
        everydayBox.setSelected(false);
        everyMonthBox.setSelected(false);
        startTimeBox.getSelectionModel().selectFirst();
        endTimeBox.getSelectionModel().selectLast();
    }

    DoctorScreenController(MainController mc){
        this.mc = mc;
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        userTag.setText("Welcome Doctor " + name);
        dbController = new DBController();
        appointments = DBController.getAppointments();

        Calendar cal = Calendar.getInstance();
        yearToday = cal.get(GregorianCalendar.YEAR);
        monthToday = cal.get(GregorianCalendar.MONTH);
        dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);

        refreshCalendar(monthToday, yearToday, dayToday);
        initializeBoxes();
    }

    public static void setName (String name){
        DoctorScreenController.name = name;
    }

    boolean now (int day){
        for (Appointment app : appointments)
            if (app.getYear() == yearToday && app.getMonth() == monthToday && app.getDay() == day && app.getDoctor().equals(name))
                return true;
        return false;
    }

    boolean eventToday (Appointment a,int day){
        if (a.getYear() == yearToday)
            if (a.getMonth() == monthToday && a.getDoctor().equals(name))
                return a.getDay() == day;
        return false;
    }

    private static String name;
    private MainController mc;
}