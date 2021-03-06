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
import java.time.LocalDate;
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
        dbController.loadAppointments();
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
    public void displayWeekView() {
        dbController.loadAppointments();
        userWeekTag.setText("Doctor " + name);

        Date dateForWeek = new Date();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
        SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

        cal.set(Calendar.MONTH, monthToday);
        System.out.println(monthToday);
        cal.set(Calendar.DATE, dayToday);
        System.out.println(dayToday);
        cal.set(Calendar.YEAR, yearToday);
        System.out.println(yearToday);

        System.out.println(cal.getTime());

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

        sundayColumn.setCellFactory(column -> {
            return new TableCell<WeekTableItem, String>() {
                @Override
                protected void updateItem(String sunEvent, boolean empty) {
                    super.updateItem(sunEvent, empty);

                    if (sunEvent == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(sunEvent);
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());
                        if (currentItem.getSunColor() != null) {

                            if (currentItem.getSunColor().equals(Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getSunColor().equals(Color.decode("#DC654D"))) {
                                setStyle("-fx-background-color: #DC654D");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getSunColor().equals(Color.decode("#98FF98"))) {
                                setStyle("-fx-background-color: #98FF98");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                            else if (currentItem.getSunColor().equals(Color.decode("#FDFD96"))) {
                                setStyle("-fx-background-color: #FDFD96");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }

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
                        setText(monEvent);
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getMonColor() != null) {

                            if (currentItem.getMonColor().equals(Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getMonColor().equals(Color.decode("#DC654D"))) {
                                setStyle("-fx-background-color: #DC654D");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getMonColor().equals(Color.decode("#98FF98"))) {
                                setStyle("-fx-background-color: #98FF98");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                            else if (currentItem.getMonColor().equals(Color.decode("#FDFD96"))) {
                                setStyle("-fx-background-color: #FDFD96");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
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
                        setText(tueEvent);
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());
                        if (currentItem.getTueColor() != null) {

                            if (currentItem.getTueColor().equals(Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getTueColor().equals(Color.decode("#DC654D"))) {
                                setStyle("-fx-background-color: #DC654D");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getTueColor().equals(Color.decode("#98FF98"))) {
                                setStyle("-fx-background-color: #98FF98");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                            else if (currentItem.getTueColor().equals(Color.decode("#FDFD96"))) {
                                setStyle("-fx-background-color: #FDFD96");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
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
                        setText(wedEvent);
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());

                        if (currentItem.getWedColor() != null) {
                            if (currentItem.getWedColor().equals(Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getWedColor().equals(Color.decode("#DC654D"))) {
                                setStyle("-fx-background-color: #DC654D");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getWedColor().equals(Color.decode("#98FF98"))) {
                                setStyle("-fx-background-color: #98FF98");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                            else if (currentItem.getWedColor().equals(Color.decode("#FDFD96"))) {
                                setStyle("-fx-background-color: #FDFD96");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
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
                        setText(event);
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());
                        if (currentItem.getThuColor() != null) {
                            if (currentItem.getThuColor().equals(Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getThuColor().equals(Color.decode("#DC654D"))) {
                                setStyle("-fx-background-color: #DC654D");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getThuColor().equals(Color.decode("#98FF98"))) {
                                setStyle("-fx-background-color: #98FF98");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                            else if (currentItem.getThuColor().equals(Color.decode("#FDFD96"))) {
                                setStyle("-fx-background-color: #FDFD96");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
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
                        setText(event);
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());
                        if (currentItem.getFriColor() != null) {
                            if (currentItem.getFriColor().equals(Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getFriColor().equals(Color.decode("#DC654D"))) {
                                setStyle("-fx-background-color: #DC654D");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getFriColor().equals(Color.decode("#98FF98"))) {
                                setStyle("-fx-background-color: #98FF98");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                            else if (currentItem.getFriColor().equals(Color.decode("#FDFD96"))) {
                                setStyle("-fx-background-color: #FDFD96");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
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
                        setText(satEvent);
                        WeekTableItem currentItem = getTableView().getItems().get(getIndex());
                        if (currentItem.getSatColor() != null) {
                            if (currentItem.getSatColor().equals(Color.decode("#78B4BF"))) {
                                setStyle("-fx-background-color: #78B4BF");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getSatColor().equals(Color.decode("#DC654D"))) {
                                setStyle("-fx-background-color: #DC654D");
                                setTextFill(javafx.scene.paint.Color.WHITE);
                            }
                            else if (currentItem.getSatColor().equals(Color.decode("#98FF98"))) {
                                setStyle("-fx-background-color: #98FF98");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                            else if (currentItem.getSatColor().equals(Color.decode("#FDFD96"))) {
                                setStyle("-fx-background-color: #FDFD96");
                                setTextFill(javafx.scene.paint.Color.BLACK);
                            }

                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                        }
                    }
                }
            };
        });
        weekTable.setItems(data);
    }

    public ObservableList<WeekTableItem> initializeWeekView(Calendar forWeekCalendar) {
        ArrayList<Appointment> itemsToDisplay = new ArrayList<>();
        ArrayList<WeekTableItem> toTableItems = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
        SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

        int month = monthToday;
        int startMonth = forWeekCalendar.get(Calendar.MONTH);
        int startDay = forWeekCalendar.get(Calendar.DATE);
        String[] date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
        int endDay = forWeekCalendar.get(Calendar.DATE);

        System.out.println("Week View: " + month + " " + startDay + " " + endDay);

        while (!date[0].equalsIgnoreCase("Sat")) {
            forWeekCalendar.add(Calendar.DATE, 1);
            date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
            endDay = Integer.parseInt(date[2]);
        }

        toTableItems.add(new WeekTableItem(""));

        for (int hour = 8; hour <= 17; hour++)
            for (int min = 0; min <= 30; min+=30) {

                if (min < 30) {
                    toTableItems.add(new WeekTableItem(hour + ":" + String.format("%02d", min)));
                    toTableItems.get(toTableItems.size()-1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size()-1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size()-1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size()-1).setValueEndMin(min+29);
                } else {
                    toTableItems.add(new WeekTableItem(""));
                    toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
                    toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
                    toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
                }
            }

        for (Appointment app : appointments)
            if (app.getMonth() == monthToday && (app.getDay() >= startDay && app.getDay() <= endDay) && app.getYear() == yearToday) {
                System.out.println(app.getDoctor());
                if (app.getDoctor().equalsIgnoreCase(name))
                    itemsToDisplay.add(app);
            }

        int monDate = 0, tueDate = 0, wedDate = 0, thuDate = 0, friDate = 0, satDate = 0, sunDate = 0;

        System.out.println(forWeekCalendar.getTime());

        forWeekCalendar.set(yearToday, startMonth, startDay);

        String compareDay = sdf.format(forWeekCalendar.getTime()).substring(0,3);
        System.out.println(compareDay);

        System.out.println(Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8)));
        System.out.println(forWeekCalendar.getTime());
        System.out.println(month);
        System.out.println( (Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()))-1)   );
        do {
            if (month == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()))-1) {
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

            }

            forWeekCalendar.add(Calendar.DATE, 1);
            compareDay = sdf.format(forWeekCalendar.getTime()).substring(0, 3);
        } while (!compareDay.equalsIgnoreCase("Sat"));

        if (month == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()))-1)
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

            String dayOfItem = null;
            System.out.println(item.getDay());
            System.out.println(sunDate);

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

            for (WeekTableItem displayTime: toTableItems) {
                int displayStartTime = 0;

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

                    if (item.getStatus() == 0) {
                        java.awt.Color c = java.awt.Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getStatus() == 1) {
                        java.awt.Color c = java.awt.Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    }

                    break;
                } else if (displayStartTime == startTime) {
                    displayTime.setEvent(item.getPatient(), dayOfItem);

                    if (item.getStatus() == 0) {
                        java.awt.Color c = java.awt.Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getStatus() == 1) {
                        java.awt.Color c = java.awt.Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    }

                } else if (displayStartTime >= startTime && endTime >= displayEndTime) {
                    displayTime.setEvent(" ", dayOfItem);

                    if (item.getStatus() == 0) {
                        java.awt.Color c = java.awt.Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getStatus() == 1) {
                        java.awt.Color c = java.awt.Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    }

                }

                if (displayTime.getTime().equalsIgnoreCase(""))
                    continue;
                if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
                        displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
                    displayTime.setEvent(item.getTitle(), dayOfItem);

                    if (item.getStatus() == 0) {
                        java.awt.Color c = java.awt.Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getStatus() == 1) {
                        java.awt.Color c = java.awt.Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    }

                    break;
                } else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
                    displayTime.setEvent(item.getTitle(), dayOfItem);

                    if (item.getStatus() == 0) {
                        java.awt.Color c = java.awt.Color.decode("#78B4BF");
                        displayTime.setColor(c, dayOfItem);
                    } else if (item.getStatus() == 1) {
                        java.awt.Color c = java.awt.Color.decode("#DC654D");
                        displayTime.setColor(c, dayOfItem);
                    }

                } else {
                    if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour) {
                        if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
                            displayTime.setEvent(" ", dayOfItem);

                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode("#78B4BF");
                                displayTime.setColor(c, dayOfItem);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#DC654D");
                                displayTime.setColor(c, dayOfItem);
                            }

                            break;
                        } else {
                            displayTime.setEvent(" ", dayOfItem);

                            if (item.getStatus() == 0) {
                                java.awt.Color c = java.awt.Color.decode("#78B4BF");
                                displayTime.setColor(c, dayOfItem);
                            } else if (item.getStatus() == 1) {
                                java.awt.Color c = java.awt.Color.decode("#DC654D");
                                displayTime.setColor(c, dayOfItem);
                            }

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

        for (Appointment a : appointments)
            if (a.getStartHour() == Integer.parseInt(startTime[0]) && a.getStartMin() == Integer.parseInt(startTime[1]) &&
                    a.getEndHour() == Integer.parseInt(endTime[0]) && a.getEndMin() == Integer.parseInt(endTime[1]) && a.getPatient().equals("")) {
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
            if (a.getDoctor().equals(name) && a.getPatient().equals(""))
                day(a);
    }

    private void displayMonthlyEvents() {
        appointmentList.getItems().clear();
        for (Appointment a : appointments)
            if (a.getMonth() == monthToday && a.getYear() == yearToday && a.getPatient().equals("") && a.getDoctor().equals(name)) {
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
                Appointment a = appointment;
                //GINALAW KO TO -CHESIE
                dbController.deleteAppointmentForD(  name, a.getDay(), a.getMonth(), a.getYear(), a.getStartHour(), a.getStartMin(), a.getEndHour(), a.getEndMin());
                displayDailyEvents(name);
                break;
            }
        }
    }

    @FXML
    void displayAgenda() {
        appointments.clear();
        dbController.loadAppointments();
        appointmentList.getItems().clear();
        agendaDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

        appointments.sort(Comparator.comparingInt(Appointment::getStartHour));
        appointmentList.getItems().clear();

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
        appointments.clear();
        dbController.loadAppointments();
        if (monthBox.isSelected()) {
            appointmentList.getItems().clear();
            weekBox.setSelected(false);
            appointments.sort(Comparator.comparingInt(Appointment::getDay));

            displayMonthlyEvents();
        } else if (!(weekBox.isSelected() && monthBox.isSelected())) {
            appointmentList.getItems().clear();
            for (Appointment a : appointments)
                if (a.getDoctor().equals(name) && a.getPatient().equals("") && a.getDoctor().equals(name))
                    day(a);
        }
    }

    private void day(Appointment a) {
        if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && a.getPatient().equals("")) {
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
    public void createAppointment () {


            int startTime = (Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[0]) * 100) +
                    (Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[1]));
            int endTime = (Integer.parseInt(endTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[0]) * 100) +
                    (Integer.parseInt(endTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[1]));

            int startTimeBook = startTime;
            int endTimeBook = endTime;

            int starthour = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[0]);
            int startmin = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().toString().split(":")[1]);
            int endhour;
            int endmin;

            do {
                if(startmin == 30){
                    endhour = starthour + 1;
                    endmin = 00;
                }
                else {
                    endhour = starthour;
                    endmin = 30;
                }
                int status = 0;
//---------------------------------------This is ung wala pa ung recent nagawa mo.-------------------------------------
                Calendar date = Calendar.getInstance();
                int hours = date.get(Calendar.HOUR_OF_DAY);
                int minutes = date.get(Calendar.MINUTE);

                if (datePicker.getValue() != null) { // Client booked for only one day
                        int day = datePicker.getValue().getDayOfMonth();
                        int month = datePicker.getValue().getMonthValue();
                        int year = datePicker.getValue().getYear();
                    if(todayAfter(dayToday,monthToday, yearToday, day, month, year).equals("after") ||todayAfter(dayToday,monthToday, yearToday, day, month, year).equals("today") && ( hours < starthour || hours == starthour && minutes < startmin)) { //only allowed to add this time if time has not passed

                        dbController.loadAppointments();
                        dbController.createAppointment("", name, day, month-1, year, starthour, startmin, endhour, endmin, status);
                    }
                }

                else{
                    int currDay = dayToday;
                    int currMonth = monthToday ;

                    if(hours > 17 || hours == 17 && minutes > 0) {
                        currDay++;
                    }
//--------------------------------------This is ung added mo na sabi mo copy paste---------------------------------
	     	/* Calendar date = Calendar.getInstance();

	         if (datePicker.getValue() != null) { // Client booked for only one day

		        	 int day = datePicker.getValue().getDayOfMonth();
		         	 int month = datePicker.getValue().getMonthValue();
		         	 int year = datePicker.getValue().getYear();
		             dbController.loadAppointments();
		             dbController.createAppointment("", name, day, month, year, starthour, startmin, endhour, endmin, status);
	        	 }

	         }

	         // This is the recurring part, it aint finished yet.
	      else{
	     	 int currDay = dayToday;
	     	 int currMonth = monthToday + 1;

	     	 */
//---------------------------------------------------------------------------------------

                    if (everyMonthBox.isSelected()) { // Client booked appointment once a month
                        for(int i = monthToday; i < 12; i++) {
                            dbController.loadAppointments();
                            dbController.createAppointment("", name, currDay, i-1, yearToday, starthour, startmin, endhour, endmin, status);
                        }

                    }

                    for (int i = currDay; i <= date.getActualMaximum(date.DAY_OF_MONTH); i++) {
                        //date.set(year, month, i);
                        LocalDate ld = LocalDate.of(yearToday, currMonth + 1, i);

                        if (mondayBox.isSelected()) { // Client booked appointment every mondays
                            if(ld.getDayOfWeek().getValue() == 1) { //creates appointment only when say is monday
                                dbController.loadAppointments();
                                dbController.createAppointment("", name, i, currMonth, yearToday, starthour, startmin, endhour, endmin, status);
                                System.out.println("MONDAY DAY IN A WEEK: " + i);
                            }

                        }  if (tuesdayBox.isSelected()) { // Client booked appointment every tuesdays
                            if(ld.getDayOfWeek().getValue() == 2) { //creates appointment only when say is tuesday
                                dbController.loadAppointments();
                                dbController.createAppointment("", name, i, currMonth, yearToday, starthour, startmin, endhour, endmin, status);
                                System.out.println("TUESDAY DAY IN A WEEK: " + i);
                            }

                        }  if (wednesdayBox.isSelected()) { // Client booked appointment every wednesdays
                            if(ld.getDayOfWeek().getValue() == 3) { //creates appointment only when say is wednesday
                                dbController.loadAppointments();
                                dbController.createAppointment("", name, i, currMonth, yearToday, starthour, startmin, endhour, endmin, status);
                                System.out.println("WEDNESDAY DAY IN A WEEK: " + i);
                            }

                        }  if (thursdayBox.isSelected()) { // Client booked appointment every thursdays
                            if(ld.getDayOfWeek().getValue() == 4) { //creates appointment only when say is thursday
                                dbController.loadAppointments();
                                dbController.createAppointment("", name, i, currMonth, yearToday, starthour, startmin, endhour, endmin, status);
                                System.out.println("THURSDAY DAY IN A WEEK: " + i);
                            }

                        }  if (fridayBox.isSelected()) { // Client booked appointment every fridays
                            if(ld.getDayOfWeek().getValue() == 5) { //creates appointment only when say is friday
                                dbController.loadAppointments();
                                dbController.createAppointment("", name, i, currMonth, yearToday, starthour, startmin, endhour, endmin, status);
                                System.out.println("FRIDAY DAY IN A WEEK: " + i);
                            }

                        }  if (saturdayBox.isSelected()) { // Client booked appointment every saturdays
                            if(ld.getDayOfWeek().getValue() == 6) { //creates appointment only when say is saturday
                                dbController.loadAppointments();
                                dbController.createAppointment("", name, i, currMonth, yearToday, starthour, startmin, endhour, endmin, status);
                                System.out.println("SATURDAY DAY IN A WEEK: " + i);
                            }
                        }

                        else if (everydayBox.isSelected()) { // Client booked appointment every day **exclude sunday here
                            if(ld.getDayOfWeek().getValue() !=7) { //creates appointment only when say is not a sunday
                                dbController.loadAppointments();
                                dbController.createAppointment("", name, i, currMonth, yearToday, starthour, startmin, endhour, endmin, status);
                            }
                        }
                    }
                }
                starthour = endhour;
                startmin = endmin;
                startTime = (starthour * 100) + startmin;

            }while((endTime > startTime));
            datePicker.getEditor().clear();
            mondayBox.setSelected(false);
            tuesdayBox.setSelected(false);
            wednesdayBox.setSelected(false);
            thursdayBox.setSelected(false);
            fridayBox.setSelected(false);
            saturdayBox.setSelected(false);
            everydayBox.setSelected(false);
            mc.refreshAll();
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


    private String todayAfter(int todayDay, int todayMonth, int todayYear, int otherDay, int otherMonth, int otherYear){
        if(todayYear < otherYear){
            return "after";
        }
        else {
            if(todayMonth < otherMonth){
                return "after";
            }
            else {
                if(todayDay < otherDay)
                    return "after";
                else return "today";
            }
        }
    }
    private static String name;
    private MainController mc;
}