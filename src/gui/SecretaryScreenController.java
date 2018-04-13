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
import model.WeekTableItem;

public class SecretaryScreenController implements Initializable {

	@FXML
	private Label dateLabel;

	@FXML
	private Label secretaryTag;

	@FXML
	private Label dayLabel;

	@FXML
	private Label doc1Tag;

	@FXML
	private Label doc2Tag;

	@FXML
	private Label agendaLabel;

	@FXML
	GridPane miniCalendar;

	@FXML
	GridPane agendaViewGridPane;

	@FXML
	private TableView<DayTableItem> dayTableView;

	@FXML
	private TableColumn<DayTableItem, String> timeColumn;

	@FXML
	private TableColumn<DayTableItem, String> patientColumn;

	@FXML
	private ListView<String> appointmentList;

	@FXML
	private AnchorPane agendaAnchor;

	@FXML
	private ScrollPane agendaScrollPane;



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
		for (int i = 0; i < appointments.size(); i++) {
			System.out.println(ID);
			if (appointments.get(i).getAppointmentID() == ID) {
				Appointment a = appointments.get(i);
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
	private void displayDayView() {
		dayLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);

		doc1Tag.setText("Doctor " + docName1);
		doc2Tag.setText("Doctor " + docName2);

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

			b.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
						if(mouseEvent.getClickCount() == 2){
							deleteAppointment(appointment.getAppointmentID());
							displayAgendaView();
						}
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

