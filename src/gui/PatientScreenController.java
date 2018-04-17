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

public class PatientScreenController implements Initializable, ControllerParent{

	@FXML private Label dateLabel;

	@FXML private Label patientTag;

	@FXML private Label dayLabel;

	@FXML private Label doc1Tag;

	@FXML private Label doc2Tag;

	@FXML private Label agendaLabel;

	@FXML private GridPane miniCalendar;

	@FXML private AnchorPane profilePane;

	@FXML private CheckBox d1Box;

	@FXML private CheckBox d2Box;

	@FXML private ComboBox<String> doctorBox;

	@FXML private ComboBox<String> sTime;

	@FXML private ComboBox<String> eTime;

	@FXML private TableView<DayTableItem> dayTableView;

	@FXML private TableColumn<DayTableItem, String> timeColumn;

	@FXML private TableColumn<DayTableItem, String> patientColumn;

	@FXML private ListView<String> appointmentList;

	@FXML private DatePicker datePicker;

	@FXML private TextField nameLabel;

	@FXML private ScrollPane agendaScrollPane;

	@FXML private GridPane agendaViewGridPane;

	@FXML private AnchorPane agendaViewPane;

	//	@FXML
	//	public void checkIfHaveAppointment() {
	//		ObservableList<Appointment> users = DBController.getAppointments();
	//
	//		for (int i = 0; i < users.size(); i++) {
	//			if (users.get(i).getPatient().equalsIgnoreCase(patName) &&
	//					(users.get(i).getYear() == yearNow && users.get(i).getDay() == dayNow && users.get(i).getMonth() == monthNow)) {
	//				Notify();
	//			}
	//
	//		}
	//	}

	//	private void Notify() {
	//		final Stage stage = new Stage();
	//		stage.initModality(Modality.APPLICATION_MODAL);
	//		Parent viewParent;
	//		try {
	//			viewParent = FXMLLoader.load(getClass().getResource("popUp.fxml"));
	//			Scene sc = new Scene(viewParent);
	//
	//			stage.setScene(sc);
	//			stage.show();
	//
	//		} catch(IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//	}


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


	@FXML
	private void reserveSlot() {
		profilePane.setVisible(false);

		doctorBox.getItems().clear();
		sTime.getItems().clear();
		eTime.getItems().clear();
		doctorBox.getItems().add(docName1);
		doctorBox.getItems().add(docName2);

		for (int i = 8; i < 17; i++) {
			for (int j = 0; j < 60; j += 30) {
				if (j % 60 == 0) {
					sTime.getItems().add(i + ":00");
					//eTime.getItems().add(i + ":00");
					//                    j = 0;
				}
				else{
					sTime.getItems().add(i + ":30");
					//eTime.getItems().add(i + ":30");

				}
			}
		}
		/*
		for (int i = 8; i <= 17; i++) {
			eTimeHour.getItems().add(String.valueOf(i));
			sTimeHour.getItems().add(String.valueOf(i));
		}

		sTimeMin.getItems().add("00");
		sTimeMin.getItems().add("30");
		eTimeMin.getItems().add("00");
		eTimeMin.getItems().add("30");
		 */
		mc.refreshAll();
	}
	// get this funtion as action performed of To box
	@FXML
	private void setTo(){
		eTime.getItems().clear();
		String startTime = sTime.getSelectionModel().getSelectedItem().toString();
		if(startTime.split(":")[1].equalsIgnoreCase("30")){
			for (int i = Integer.parseInt(startTime.split(":")[0])+1; i < 17; i++) {
				for (int j = 0; j < 60; j += 30) {
					if (j % 60 == 0) {
						eTime.getItems().add(i + ":00");
					}
					else{
						eTime.getItems().add(i + ":30");

					}
				}
			}
		}
		else{
			eTime.getItems().add(Integer.parseInt(startTime.split(":")[0])+":30");
			for (int i = Integer.parseInt(startTime.split(":")[0])+1; i < 17; i++) {
				for (int j = 0; j < 60; j += 30) {
					if (j % 60 == 0) {
						eTime.getItems().add(i + ":00");
					}
					else{
						eTime.getItems().add(i + ":30");

					}
				}
			}
		}
		eTime.getItems().add("17:00");
		mc.refreshAll();


	}

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

		dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
		refreshCalendar(monthToday, yearToday, dayToday);
		//		checkIfHaveAppointment();
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
		//		checkIfHaveAppointment();
	}

	@FXML
	public void displayDayView() {
		dayLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);

		doc1Tag.setText("Doctor " + docName1);
		doc2Tag.setText("Doctor " + docName2);

		ObservableList<DayTableItem> data = initializeDayView();
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));

		patientColumn.setCellFactory(column -> new TableCell<DayTableItem, String> (){
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

		dayTableView.setItems(data);
	}

	@FXML
	public void displayAgendaView() {
		//dbController.loadAppointments();
		agendaViewPane.setVisible(true);
		//agendaLabel.setText(convert(monthToday - 1) + " " + daySelected + ", " + yearToday);

		//ObservableList<String> scheduledAppointments = FXCollections.observableArrayList();
		agendaViewGridPane.getChildren().clear();
		appointments.clear();
		dbController.loadAppointments();

		int ctr =0;
		for (int i =0; i < appointments.size(); i++) {
			Appointment appointment = appointments.get(i);

			if(appointment.getPatient().equalsIgnoreCase(patName)){

				Button b = new Button(appointment.getMonth() + "/" + appointment.getDay() + "/" + appointment.getYear() + " ~ " + appointment.getStartHour() + ":"
						+ appointment.getStartMin() + "-" + appointment.getEndHour() + ":" + appointment.getEndMin() +
						" -- " + appointment.getPatient() + " ** Dr." + appointment.getDoctor());
				agendaViewGridPane.setHalignment(b, HPos.CENTER);

				b.setStyle("-fx-background-color: transparent");

				b.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
							if(mouseEvent.getClickCount() == 2){
								deleteAppointment(appointment.getAppointmentID()); // create function
								displayAgendaView();
							}
						}
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
				});
				agendaViewGridPane.add(b, 0, ctr);
				ctr++;
			}
		}
	}

	@FXML
	private void reserveNewAppointment(){ //GINALAW KO TO -CHESIE
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

		String patient = patName;
		String doctor = doctorBox.getSelectionModel().getSelectedItem().toString();
		int day = datePicker.getValue().getDayOfMonth();
		int month = datePicker.getValue().getMonthValue();
		int year = datePicker.getValue().getYear();
		int status = 1;

		int startTime = (Integer.parseInt(sTime.getSelectionModel().getSelectedItem().toString().split(":")[0]) * 100) +
				(Integer.parseInt(sTime.getSelectionModel().getSelectedItem().toString().split(":")[1]));
		int endTime = (Integer.parseInt(eTime.getSelectionModel().getSelectedItem().toString().split(":")[0]) * 100) +
				(Integer.parseInt(eTime.getSelectionModel().getSelectedItem().toString().split(":")[1]));

		int startTimeBook = startTime;
		int endTimeBook = endTime;

		int starthour = Integer.parseInt(sTime.getSelectionModel().getSelectedItem().toString().split(":")[0]);
		int startmin = Integer.parseInt(sTime.getSelectionModel().getSelectedItem().toString().split(":")[1]);
		int endhour;
		int endmin;

		int starthourBook = Integer.parseInt(sTime.getSelectionModel().getSelectedItem().toString().split(":")[0]);
		int startminBook = Integer.parseInt(sTime.getSelectionModel().getSelectedItem().toString().split(":")[1]);
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
			if (isValidTime(newApp) == true) {
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

				dbController.updateAppointmentPatient(1, patName, doctor, day, month, year, starthourBook, startminBook, endhourBook, endminBook);

				starthourBook = endhourBook;
				startminBook = endminBook;
				startTimeBook = (starthourBook * 100) + startminBook;
			}
		}
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

	private ObservableList<DayTableItem> initializeDayView() {
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
			if (app.getMonth() == monthToday && app.getDay() == daySelected && app.getYear() == yearToday){
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
						if (item.getPatient().equalsIgnoreCase(patName)) {
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
						if (item.getPatient().equalsIgnoreCase(patName)) {
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
						if (item.getPatient().equalsIgnoreCase(patName)) {
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
						if (item.getPatient().equalsIgnoreCase(patName)) {
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
					displayTime.setPatient(" ");

					if (item.getDoctor().equalsIgnoreCase(docName1)) {
						if (item.getPatient().equalsIgnoreCase(patName)) {
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
						if (item.getPatient().equalsIgnoreCase(patName)) {
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

	// TODO filter day view to only doc1 when checkbox is selected
	@FXML
	private void doc1Only() {

	}

	// TODO filter day view to only doc2 when checkbox is selected
	@FXML
	private void doc2Only() {

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
		// TODO display agenda for today
	}

	@FXML
	void displayWeeklyAgenda() {
		if (weekBox.isSelected()) {
			monthBox.setSelected(false);
			// TODO display all appointments for the week
		} else if (!(weekBox.isSelected() && monthBox.isSelected())) {
			// TODO display all agenda for today
		}
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

	@Override
	boolean now(int day) {
		for (Appointment app : appointments)
			if (app.getYear() == yearToday && app.getMonth() == monthToday && app.getDay() == day && app.getPatient().equals(name))
				return true;
		return false;
	}

	@Override
	boolean eventToday(Appointment a, int day) {
		if (a.getYear() == yearToday)
			if (a.getMonth() == monthToday && a.getPatient().equals(name))
				return a.getDay() == day;
		return false;
	}

	@FXML
	private void reserveAppointment() {
		mc.refreshAll();//refresh all
		// TODO reserve appointment
	}

	@FXML
	private void setAppointment() {
		mc.refreshAll();
	}

	PatientScreenController(MainController mc){
	public PatientScreenController(MainController mc){
		this.mc = mc;


	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

		dbController = new DBController();

		patientTag.setText("Welcome Patient " + patName);

		Calendar cal = Calendar.getInstance();
		yearToday = cal.get(GregorianCalendar.YEAR);
		monthToday = cal.get(GregorianCalendar.MONTH) + 1;
		dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);

		yearNow = cal.get(GregorianCalendar.YEAR);
		monthNow = cal.get(GregorianCalendar.MONTH) + 1;
		dayNow = cal.get(GregorianCalendar.DAY_OF_MONTH);

		daySelected = dayToday;

		for (int i=0; i<dbController.getDoctors().size(); i++) {
			System.out.println(dbController.getDoctors().get(i).getName());
			doctorBox.getItems().add(dbController.getDoctors().get(i).getName());
		}

		appointments = DBController.getAppointments();
		dateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
		refreshCalendar(monthToday, yearToday, dayToday);
		//		checkIfHaveAppointment();
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
					if(app.getPatient().equalsIgnoreCase(patName)){
						button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color:  #dc654d; -fx-text-fill: #FFFFFF");
						break;
					}
				}

			miniCalendar.add(button, column, row);
		}
	}

	private boolean now(int day) {
		for (Appointment app : appointments) {
			if(app.getPatient().equalsIgnoreCase(patName)){
				if (app.getYear() == yearToday && app.getMonth() == monthToday && app.getDay() == day)
					return true;
			}
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

	public static void setName(String name) { patName = name; }

	public static void setDoc1Name(String name) { docName1 = name; }

	public static void setDoc2Name(String name) { docName2 = name; }

	private int yearToday;
	private int monthToday;
	private int dayToday;
	private int daySelected;
	private DBController dbController;
	private ObservableList<Appointment> appointments;
	private static String patName;
	private static String docName1;
	private static String docName2;
	private int yearNow;
	private int monthNow;
	private int dayNow;
	private MainController mc;
}
