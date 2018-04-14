package gui;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import database.Appointment;
import database.DBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import gui.secretaryscreen*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.CalendarItem;

public class SecretaryScreenController implements Initializable {

	@FXML private Label dateLabel;

	@FXML private Label secretaryTag;

	@FXML private Label dayLabel;

	@FXML private Label doc1Tag;

	@FXML private Label doc2Tag;

	@FXML private Label agendaLabel;

	@FXML private GridPane miniCalendar;

	@FXML private GridPane agendaViewGridPane;

	@FXML
	private TableView<DayTableItem> dayTableView;

	@FXML
	private TableColumn<DayTableItem, String> timeColumn;

	@FXML
	private TableColumn<DayTableItem, String> patientColumn;

	@FXML private ListView<String> appointmentList;

	@FXML private AnchorPane agendaAnchor;

	@FXML private ScrollPane agendaScrollPane;

	@FXML
	private TableView<WeekTableItem> weekTable;
	@FXML
	private TableColumn<WeekTableItem, String> weekTime;
	@FXML
	private TableColumn<WeekTableItem, String> weekSunEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekMonEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekTueEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekWedEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekThuEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekFriEvent;
	@FXML
	private TableColumn<WeekTableItem, String> weekSatEvent;



	/*
	@FXML
	private void reserveNewAppointment() {
		System.out.println("reserved!");

		int appointmentID = appointments.size() + 1;
		String patient = nameLabel.getText();
		String doctor = doctorLabel.getSelectionModel().getSelectedItem().toString();

		int day = datePicker.getValue().getDayOfMonth();
		int month = datePicker.getValue().getMonthValue();
		int year = datePicker.getValue().getYear();

		int starthour = Integer.parseInt(starttimeLabel.getText().split(":")[0]);
		int startmin = Integer.parseInt(starttimeLabel.getText().split(":")[1]);
		int endhour = Integer.parseInt(endtimeLabel.getText().split(":")[0]);
		int endmin = Integer.parseInt(endtimeLabel.getText().split(":")[1]);

		int status = 1;

		Appointment newApp = new Appointment(appointmentID, patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);

		//check for conflict first
		if (isValidTime(newApp) == true) {
			dbController.createAppointment(patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);
			newApp.printAppointment();
			appointments.add(newApp);
		} else {
			System.out.println("Invalid appointment slot");
		}

	}
*/
	public boolean isValidTime(Appointment appo) {
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

	public void cancelAppointment(int ID) {
		//dbController
	}

	public void deleteAppointment(int ID) {
		dbController.loadAppointments();
		for (Appointment appointment : appointments) {
			System.out.println(ID);
			if (appointment.getAppointmentID() == ID) {
				Appointment a = appointment;
				System.out.println(a.getPatient() + a.getDay() + a.getMonth() + a.getYear() + a.getStartHour() + a.getStartMin() + a.getEndHour() + a.getEndMin());
				dbController.deleteAppointmentForC(a.getPatient(), a.getDay(), a.getMonth(), a.getYear(), a.getStartHour(), a.getStartMin(), a.getEndHour(), a.getEndMin());
			}
		}
	}

	@FXML
	private void nextMonth() {
		if (monthToday == 11) {
			monthToday = 0;
			yearToday += 1;
		} else
			monthToday += 1;

		dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
		refreshCalendar(monthToday, yearToday, dayToday);
		displayDayView();
		displayWeekView();
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
		displayDayView();
		displayWeekView();
	}

	@FXML
	private void displayDayView() {
		dayLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);

		doc1Tag.setText("Doctor " + docName1);
		doc2Tag.setText("Doctor " + docName2);

		ObservableList<DayTableItem> data = initializeDayView();
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));

		patientColumn.setCellFactory(column -> new TableCell<DayTableItem, String> () {
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
						setTextFill(javafx.scene.paint.Color.BLACK);
						setStyle("");
					}
				}
			}
		});

		dayTableView.setItems(data);
	}

	@FXML
	private void displayWeekView() {
		Date dateForWeek = new Date();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
		SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

		cal.set(Calendar.MONTH, monthToday-1);
		System.out.println(monthToday);
		cal.set(Calendar.DATE, daySelected);
		System.out.println(daySelected);
		cal.set(Calendar.YEAR, yearToday);
		System.out.println(yearToday);

		System.out.println(cal.getTime());

		int startWeekValue = -(cal.get(Calendar.DAY_OF_WEEK) - 1);

		cal.add(Calendar.DATE, startWeekValue);

		ObservableList<WeekTableItem> data = initializeWeekView(cal);
		weekTime.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("time"));
		weekSunEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("sunEvent"));
		weekMonEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("monEvent"));
		weekTueEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("tueEvent"));
		weekWedEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("wedEvent"));
		weekThuEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("thuEvent"));
		weekFriEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("friEvent"));
		weekSatEvent.setCellValueFactory(new PropertyValueFactory<WeekTableItem, String>("satEvent"));

		weekSunEvent.setCellFactory(column -> {
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
							setTextFill(Color.WHITE);
							/*
							if (currentItem.getSunColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else
								setStyle("-fx-background-color: green");
								*/
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});

		weekMonEvent.setCellFactory(column -> {
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
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: darkgray");
							/*
							if (currentItem.getMonColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getMonColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
								*/
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});

		weekTueEvent.setCellFactory(column -> {
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
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: darkgray");
							/*
							if (currentItem.getTueColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getTueColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
							*/
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});

		weekWedEvent.setCellFactory(column -> {
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
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: darkgray");
							/*
							if (currentItem.getWedColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getWedColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
							*/
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});

		weekThuEvent.setCellFactory(column -> {
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
							setTextFill(Color.WHITE);
							/*
							if (currentItem.getThuColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getThuColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
								*/
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});

		weekFriEvent.setCellFactory(column -> {
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
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: darkgray");
							/*
							if (currentItem.getFriColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getFriColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
								*/
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});

		weekSatEvent.setCellFactory(column -> {
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
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: darkgray");
							/*
							if (currentItem.getSatColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getSatColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
								*/
						} else {
							setTextFill(Color.BLACK);
							setStyle("");
						}
					}
				}
			};
		});
		weekTable.setItems(data);
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

					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c);
						}
					}

					break;
				} else if (displayStartTime == startTime) {
					displayTime.setPatient(item.getPatient());

					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c);
						}
					}

				} else if (displayStartTime >= startTime && endTime >= displayEndTime) {
					displayTime.setPatient(" ");

					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c);
						}
					}
				}
			}
		}
		return FXCollections.observableArrayList(toTableItems);
	}


	public ObservableList<WeekTableItem> initializeWeekView(Calendar forWeekCalendar) {
		ArrayList<Appointment> itemsToDisplay = new ArrayList<>();
		ArrayList<WeekTableItem> toTableItems = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
		SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

		int month = Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()));
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

		for (int hour = 0; hour <= 23; hour++)
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

		for (Appointment item : appointments) {
			if (item.getMonth() == month && (item.getDay() >= startDay && item.getDay() <= endDay)
					&& item.getYear() == yearToday) {
				System.out.println(item.getDay() + " " + item.getMonth());
					itemsToDisplay.add(item);
			}
		}

		int monDate = 0, tueDate = 0, wedDate = 0, thuDate = 0, friDate = 0, satDate = 0, sunDate = 0;

		forWeekCalendar.set(yearToday, month-1, startDay);

		String compareDay = sdf.format(forWeekCalendar.getTime()).substring(0,3);
		System.out.println(compareDay);

		System.out.println(month);
		System.out.println(Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8)));
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

		for (Appointment item: itemsToDisplay) {
			int startHour = item.getStartHour();
			int startMin = item.getStartMinute();
			int endHour;
			int endMin;

			if (startMin == 0)
				startHour = item.getStartHour() * 10;

			int startTime = Integer.parseInt(Integer.toString(startHour) + Integer.toString(startMin));

			// First if is for the
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

					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE, dayOfItem);
					else if (item instanceof Task && item.isDone() == false)
						displayTime.setColor(Color.GREEN, dayOfItem);
					else
						displayTime.setColor(Color.DARKGREY, dayOfItem);
						*/
					break;
				} else if (displayStartTime == startTime) {
					displayTime.setEvent(item.getPatient(), dayOfItem);

					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE, dayOfItem);
					else if (item instanceof Task && item.isDone() == false)
						displayTime.setColor(Color.GREEN, dayOfItem);
					else
						displayTime.setColor(Color.DARKGREY, dayOfItem);
						*/
				} else if (displayStartTime >= startTime && endTime >= displayEndTime) {
					displayTime.setEvent(" ", dayOfItem);
					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE, dayOfItem);
					else if (item instanceof Task && item.isDone() == false)
						displayTime.setColor(Color.GREEN, dayOfItem);
					else
						displayTime.setColor(Color.DARKGREY, dayOfItem);
						*/
				}

				/*
				if (displayTime.getTime().equalsIgnoreCase(""))
					continue;
				if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
						displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {

						displayTime.setEvent(item.getTitle(), dayOfItem);

						if (item instanceof Event)
							displayTime.setColor(Color.BLUE, dayOfItem);
						else
							displayTime.setColor(Color.GREEN, dayOfItem);

						break;
				} else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
						displayTime.setEvent(item.getTitle(), dayOfItem);

						if (item instanceof Event)
							displayTime.setColor(Color.BLUE, dayOfItem);
						else
							displayTime.setColor(Color.GREEN, dayOfItem);
				} else {
					if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour) {
						if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
								displayTime.setEvent(" ", dayOfItem);

								if (item instanceof Event)
									displayTime.setColor(Color.BLUE, dayOfItem);
								else
									displayTime.setColor(Color.GREEN, dayOfItem);
								break;
						} else {
								displayTime.setEvent(" ", dayOfItem);

								if (item instanceof Event)
									displayTime.setColor(Color.BLUE, dayOfItem);
								else
									displayTime.setColor(Color.GREEN, dayOfItem);
						}
					}
				}
				*/
			}
		}

		return FXCollections.observableArrayList(toTableItems);
	}





	@FXML
	private void displayAgendaView() {
		agendaLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);

		//ObservableList<String> scheduledAppointments = FXCollections.observableArrayList();
		agendaViewGridPane.getChildren().clear();
		appointments.clear();
		dbController.loadAppointments();


		for (int i =0; i < appointments.size(); i++) {
			Appointment appointment = appointments.get(i);
			Button b = new Button(appointment.getStartHour() + ":"
						+ appointment.getStartMin() + "-" + appointment.getEndHour() + ":" + appointment.getEndMin() +
						" -- " + appointment.getPatient() + " ** " + appointment.getDoctor());


			agendaViewGridPane.setHalignment(b, HPos.CENTER);

			b.setStyle("-fx-background-color: transparent");

			b.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        deleteAppointment(appointment.getAppointmentID());
                        displayAgendaView();
                    }
                }
            });

			agendaViewGridPane.add(b, 0,i);
	}
}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

		dbController = new DBController();
		appointments = DBController.getAppointments();

		secretaryTag.setText("Welcome Secretary " + secName);

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
						if (now(Integer.parseInt(((Button) node).getText())))
							node.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: #DC654D; -fx-text-fill: #FFFFFF");
						else
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

	private boolean now(int day) {
		for (Appointment app : appointments) {
			if (app.getYear() == yearToday && app.getMonth() == monthToday && app.getDay() == day)
				return true;
		}

		return false;
	}

	private boolean eventToday(Appointment a, int day) {
		if (a.getYear() == yearToday)
			if (a.getMonth() == monthToday)
				return a.getDay() == day;

		return false;
	}

	private String convert(int month) {
		System.out.println(month);
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
			case -1: return "December";
		}
		return "January";
	}

	public static void setName(String name) { secName = name; }

	public static void setDoc1Name(String name) { docName1 = name; }

	public static void setDoc2Name(String name) { docName2 = name; }

	private int yearToday;
	private int monthToday;
	private int dayToday;
	private int daySelected;
	private DBController dbController;
	private ObservableList<Appointment> appointments;
	private static String secName;
	private static String docName1;
	private static String docName2;
}

