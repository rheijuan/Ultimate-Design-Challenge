package gui;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import database.Appointment;
import database.DBController;
import database.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SecretaryScreenController extends AbstractController implements Initializable, ControllerParent {

	@FXML private Label doctor1DayTag;

	@FXML private Label doctor2DayTag;

	@FXML private TextField nameTextField;

	@FXML ComboBox<String> doctorBox;
///*
//	@FXML
//	private void reserveNewAppointment() {
//		System.out.println("reserved!");
//
//		int appointmentID;
//		if(appointments.size() ==0)
//			appointmentID = appointments.size()+1;
//		else{
//			int max =appointments.get(0).getAppointmentID();
//			for(int i=1; i<appointments.size(); i++){
//				if(max < appointments.get(i).getAppointmentID())
//					max = appointments.get(i).getAppointmentID();
//			}
//			appointmentID = max;
//		}
//		String patient = nameLabel.getText();
//		String doctor = doctorLabel.getSelectionModel().getSelectedItem().toString();
//
//		int day = datePicker.getValue().getDayOfMonth();
//		int month = datePicker.getValue().getMonthValue();
//		int year = datePicker.getValue().getYear();
//
//		int starthour = Integer.parseInt(starttimeLabel.getText().split(":")[0]);
//		int startmin = Integer.parseInt(starttimeLabel.getText().split(":")[1]);
//		int endhour = Integer.parseInt(endtimeLabel.getText().split(":")[0]);
//		int endmin = Integer.parseInt(endtimeLabel.getText().split(":")[1]);
//
//		int status = 1;
//
//		Appointment newApp = new Appointment(appointmentID, patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);
//
//		//check for conflict first
//		if (isValidTime(newApp) == true) {
//			dbController.createAppointment(patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);
//			newApp.printAppointment();
//			appointments.add(newApp);
//		} else { c
//			System.out.println("Invalid appointment slot");
//		}
//*/
///*
//	public boolean isValidTime(Appointment appo) {
//		for (int i = 0; i < dbController.getAppointments().size(); i++) {
//			Appointment a = dbController.getAppointments().get(i);
//			if (appo.getMonth() == a.getMonth() && appo.getDay() == a.getDay() && appo.getYear() == a.getYear()) {
//
//				LocalTimeRange range1 = new LocalTimeRange(LocalTime.of(appo.getStartHour(), appo.getStartMinute()), LocalTime.of(appo.getEndHour(), appo.getEndMinute()));
//				LocalTimeRange range2 = new LocalTimeRange(LocalTime.of(a.getStartHour(), a.getStartMinute()), LocalTime.of(a.getEndHour(), a.getEndMinute()));
//				if (range1.overlaps(range2))
//					return false;
//			}
//		}
//		return true;
//	}
//	*/


//	@FXML
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
//
//	@FXML
//	private void initWalkIn(ActionEvent event){
//		dbController.loadAppointments();
//
//		doctorBox.getItems().add(docName1);
//		doctorBox.getItems().add(docName2);
//
//		setTime(startTimeComboBox.getItems());
//		startTimeComboBox.getItems().add("17:00");
//
//		setTime(endTimeComboBox.getItems());
//		endTimeComboBox.getItems().add("17:00");
//
//
//		profilePane.setVisible(false);
//		reservePane.setVisible(true);
//	}
//
//	@FXML
//	private void bookWalkIn(ActionEvent event) {
//
//
//		System.out.println("reserved!");
//
//		int appointmentID = appointments.size() + 1;
//		String patient = nameLabel.getText();
//		String doctor = doctorBox.getSelectionModel().getSelectedItem();
//
//		int day = datePicker.getValue().getDayOfMonth();
//		int month = datePicker.getValue().getMonthValue();
//		int year = datePicker.getValue().getYear();
//
//		int starthour = Integer.parseInt(startTimeComboBox.getSelectionModel().getSelectedItem().split(":")[0]);
//		int startmin = Integer.parseInt(startTimeComboBox.getSelectionModel().getSelectedItem().split(":")[1]);
//		int endhour = Integer.parseInt(endTimeComboBox.getSelectionModel().getSelectedItem().split(":")[0]);
//		int endmin = Integer.parseInt(endTimeComboBox.getSelectionModel().getSelectedItem().split(":")[1]);
//
//		int status = 1;
//
//		Appointment newApp = new Appointment(appointmentID, patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);
//
//		//check for conflict first
//		if (isValidTime(newApp)) {
//			dbController.createAppointment(patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);
//			newApp.printAppointment();
//			appointments.add(newApp);
//			profilePane.setVisible(true);
//			reservePane.setVisible(false);
//		} else {
//			System.out.println("Invalid appointment slot");
//		}
//	}
//
//	private boolean isValidTime(Appointment appo) {
//		for (int i = 0; i < dbController.getAppointments().size(); i++) {
//			Appointment a = dbController.getAppointments().get(i);
//			if (appo.getMonth() == a.getMonth() && appo.getDay() == a.getDay() && appo.getYear() == a.getYear()) {
//
//				LocalTimeRange range1 = new LocalTimeRange(LocalTime.of(appo.getStartHour(), appo.getStartMinute()), LocalTime.of(appo.getEndHour(), appo.getEndMinute()));
//				LocalTimeRange range2 = new LocalTimeRange(LocalTime.of(a.getStartHour(), a.getStartMinute()), LocalTime.of(a.getEndHour(), a.getEndMinute()));
//				if (range1.overlaps(range2))
//					return false;
//			}
//		}
//		return true;
//	}
//
//
//	public void deleteAppointment(int ID) {
//		dbController.loadAppointments();
//		for (Appointment appointment : appointments) {
//			System.out.println(ID);
//			if (appointment.getAppointmentID() == ID) {
//				Appointment a = appointment;
//				System.out.println(a.getPatient() + a.getDay() + a.getMonth() + a.getYear() + a.getStartHour() + a.getStartMin() + a.getEndHour() + a.getEndMin());
//				dbController.deleteAppointmentForC(a.getPatient(), a.getDay(), a.getMonth(), a.getYear(), a.getStartHour(), a.getStartMin(), a.getEndHour(), a.getEndMin());
//			}
//		}
//	}\
//
//

	@FXML
	public void displayWeekView() {
		doctor1WeekTag.setText("Doctor " + docName1);
		doctor2WeekTag.setText("Doctor " + docName2);
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

		int month = Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime())) - 1;
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
			if (app.getMonth() == monthToday && app.getDay() == dayToday && app.getYear() == yearToday) {
				if (app.getDoctor().equalsIgnoreCase(docName1) && doctor1WeekTag.isSelected())
					itemsToDisplay.add(app);
				else if (app.getDoctor().equalsIgnoreCase(docName2) && doctor2WeekTag.isSelected())
					itemsToDisplay.add(app);
			}

		int monDate = 0, tueDate = 0, wedDate = 0, thuDate = 0, friDate = 0, satDate = 0, sunDate = 0;

		forWeekCalendar.set(yearToday, month, startDay);

		String compareDay = sdf.format(forWeekCalendar.getTime()).substring(0,3);
		System.out.println(compareDay);

		System.out.println(month);
		System.out.println(Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8)));
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

				forWeekCalendar.add(Calendar.DATE, 1);
				compareDay = sdf.format(forWeekCalendar.getTime()).substring(0, 3);
			} else
				break;
		} while (!compareDay.equalsIgnoreCase("Sat"));

		if (monthToday == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()))-1)
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

					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c, dayOfItem);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c, dayOfItem);
						}
					}

					break;
				} else if (displayStartTime == startTime) {
					displayTime.setEvent(item.getPatient(), dayOfItem);

					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c, dayOfItem);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c, dayOfItem);
						}
					}

				} else if (displayStartTime >= startTime && endTime >= displayEndTime) {
					displayTime.setEvent(" ", dayOfItem);

					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c, dayOfItem);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c, dayOfItem);
						}
					}

				}

				if (displayTime.getTime().equalsIgnoreCase(""))
					continue;
				if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
						displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
					displayTime.setEvent(item.getTitle(), dayOfItem);

					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c, dayOfItem);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c, dayOfItem);
						}
					}



					break;
				} else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
					displayTime.setEvent(item.getTitle(), dayOfItem);
					if (item.getDoctor().equals(docName1)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c, dayOfItem);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0 ) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c, dayOfItem);
						}
						else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c, dayOfItem);
						}
					}

				} else {
					if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour) {
						if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
							displayTime.setEvent(" ", dayOfItem);

							if (item.getDoctor().equals(docName1)) {
								if (item.getStatus() == 0 ) {
									java.awt.Color c = java.awt.Color.decode("#78B4BF");
									displayTime.setColor(c, dayOfItem);
								}
								else if (item.getStatus() == 1) {
									java.awt.Color c = java.awt.Color.decode("#DC654D");
									displayTime.setColor(c, dayOfItem);
								}
							} else if (item.getDoctor().equals(docName2)) {
								if (item.getStatus() == 0 ) {
									java.awt.Color c = java.awt.Color.decode("#98FF98");
									displayTime.setColor(c, dayOfItem);
								}
								else if (item.getStatus() == 1) {
									java.awt.Color c = java.awt.Color.decode("#FDFD96");
									displayTime.setColor(c, dayOfItem);
								}
							}

							break;
						} else {
							displayTime.setEvent(" ", dayOfItem);

							if (item.getDoctor().equals(docName1)) {
								if (item.getStatus() == 0 ) {
									java.awt.Color c = java.awt.Color.decode("#78B4BF");
									displayTime.setColor(c, dayOfItem);
								}
								else if (item.getStatus() == 1) {
									java.awt.Color c = java.awt.Color.decode("#DC654D");
									displayTime.setColor(c, dayOfItem);
								}
							} else if (item.getDoctor().equals(docName2)) {
								if (item.getStatus() == 0 ) {
									java.awt.Color c = java.awt.Color.decode("#98FF98");
									displayTime.setColor(c, dayOfItem);
								}
								else if (item.getStatus() == 1) {
									java.awt.Color c = java.awt.Color.decode("#FDFD96");
									displayTime.setColor(c, dayOfItem);
								}
							}

						}
					}
				}

			}
		}

		return FXCollections.observableArrayList(toTableItems);
	}

	@FXML
	void displayDayView() {
		userDayTag.setText("Secretary " + name);
		dayDateTag.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);
		doctor1DayTag.setText("Doctor " + docName1);
		doctor2DayTag.setText("Doctor " + docName2);

		ObservableList<DayTableItem> data = initializeDayView();
		dayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		dayPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));

		dayPatientColumn.setCellFactory(column -> new TableCell<DayTableItem, String> () {
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

		for (Appointment app : appointments)
			if (app.getMonth() == monthToday && app.getDay() == dayToday && app.getYear() == yearToday) {
				if (app.getDoctor().equalsIgnoreCase(docName1) && doc1DayFilter.isSelected())
					itemsToDisplay.add(app);
				else if (app.getDoctor().equalsIgnoreCase(docName2) && doc2DayFilter.isSelected())
					itemsToDisplay.add(app);
			}

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
						if (item.getStatus() == 0) {
							java.awt.Color c = java.awt.Color.decode("#78B4BF");
							displayTime.setColor(c);
						} else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#DC654D");
							displayTime.setColor(c);
						}
					} else if (item.getDoctor().equals(docName2)) {
						if (item.getStatus() == 0) {
							java.awt.Color c = java.awt.Color.decode("#98FF98");
							displayTime.setColor(c);
						} else if (item.getStatus() == 1) {
							java.awt.Color c = java.awt.Color.decode("#FDFD96");
							displayTime.setColor(c);
						}
					}
				} else if (displayStartTime >= startTime && endTime >= displayEndTime) {
					displayTime.setPatient("");

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

	// TODO filter day view to only doc1 when checkbox is selected
	@FXML
	private void doc1Only() {

	}

	// TODO filter day view to only doc2 when checkbox is selected
	@FXML
	private void doc2Only() {

	}

	// TODO still thinking on how to fix this
	@FXML
	private void reserveAppointment() {

		int appointmentID = appointments.size() + 1;
		String patient = nameTextField.getText();
		String doctor = doctorBox.getSelectionModel().getSelectedItem();

		int day = datePicker.getValue().getDayOfMonth();
		int month = datePicker.getValue().getMonthValue() - 1;
		int year = datePicker.getValue().getYear();

		int starthour = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().split(":")[0]);
		int startmin = Integer.parseInt(startTimeBox.getSelectionModel().getSelectedItem().split(":")[1]);
		int endhour = Integer.parseInt(endTimeBox.getSelectionModel().getSelectedItem().split(":")[0]);
		int endmin = Integer.parseInt(endTimeBox.getSelectionModel().getSelectedItem().split(":")[1]);

		int status = 1;

		Appointment newApp = new Appointment(appointmentID, patient, doctor, day, month, year, starthour, startmin, endhour, endmin, status);

		newApp.printAppointment();

		// Check if the doctor has a valid appointment slot
//		if (getValidApp(newApp)) {
//			dbController.updateAppointmentPatient(patient, doctor, day, month, year, starthour, startmin, endhour, endmin);
//			newApp.printAppointment();
//			profilePane.setVisible(true);
//		} else {
//			// TODO pop up of invalid
//			System.out.println("Invalid appointment slot");
//		}
		mc.refreshAll();
	}

	@FXML
	private void bookWalkIn() {
		profilePane.setVisible(false);
		nameTextField.clear();
		mc.refreshAll();
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

	private boolean getValidApp (Appointment app) {
		for (Appointment curApp : appointments) {
			if (curApp.getStartHour() == app.getStartHour() && curApp.getStartMin() == app.getStartMin() && curApp.getEndHour() == app.getEndHour() && curApp.getEndMin() == app.getEndMin()) {
				if (curApp.getYear() == app.getYear() && curApp.getMonth() == app.getMonth() && curApp.getDay() == app.getDay()) {
					if (curApp.getPatient().trim().equals("")) {
						return true;
					} else {
						System.out.println("FAIL PATIENT");
						curApp.printAppointment();
						return false;
					}

				} else {
					System.out.println("FAIL DATE");
					curApp.printAppointment();
					return false;
				}

			} else {
				System.out.println("FAIL TIME");
				curApp.printAppointment();
				return false;
			}
		}
		System.out.println("FAILURE");
		return false;
	}

	@FXML
	void displayAgenda() {
		appointmentList.getItems().clear();
		agendaDateLabel.setText(convert(monthToday) + " " + dayToday + ", " + yearToday);

		appointments.sort(Comparator.comparingInt(Appointment::getStartHour));

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

	@FXML
	void displayWeeklyAgenda() {
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
					if (a.getYear() == yearToday && a.getMonth() == monthToday && !a.getPatient().equals("") && a.getDoctor().equals(docName1)) {
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
					if (a.getYear() == yearToday && a.getMonth() == monthToday && !a.getPatient().equals("") && a.getDoctor().equals(docName2)) {
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
					if (a.getMonth() == monthToday && a.getYear() == yearToday && !a.getPatient().equals("")) {
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
					if (a.getYear() == yearToday && a.getMonth() == monthToday && !a.getPatient().equals("") && a.getDoctor().equals(docName1)) {
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
					if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && !a.getPatient().equals("") && a.getDoctor().equals(docName1)) {
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
					if (a.getMonth() == monthToday && a.getYear() == yearToday && !a.getPatient().equals("")) {
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
					if (a.getYear() == yearToday && a.getMonth() == monthToday && !a.getPatient().equals("") && a.getDoctor().equals(docName2)) {
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
					if (a.getYear() == yearToday && a.getMonth() == monthToday && a.getDay() == dayToday && !a.getPatient().equals("") && a.getDoctor().equals(docName2)) {
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
					if (a.getMonth() == monthToday && a.getYear() == yearToday && !a.getPatient().equals("")) {
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
			if (app.getYear() == yearToday && app.getMonth() == monthToday && app.getDay() == day && (app.getDoctor().equals(docName1) || app.getDoctor().equals(docName2)))
				return true;
		return false;
	}

	@Override
	boolean eventToday(Appointment a, int day) {
		if (a.getYear() == yearToday)
			if (a.getMonth() == monthToday && (a.getDoctor().equals(docName1) || a.getDoctor().equals(docName2)))
				return a.getDay() == day;
		return false;
	}

	SecretaryScreenController(MainController mc){
		this.mc = mc;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbController = new DBController();
		userTag.setText("Welcome Secretary " + name);
		appointments = DBController.getAppointments();

		Calendar cal = Calendar.getInstance();
		yearToday = cal.get(GregorianCalendar.YEAR);
		monthToday = cal.get(GregorianCalendar.MONTH);
		dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);

		refreshCalendar(monthToday, yearToday, dayToday);

		doctor1DayTag.setText("Doctor " + docName1);
		doctor2DayTag.setText("Doctor " + docName2);

		doctor1WeekTag.setText("Doctor " + docName1);
		doctor2WeekTag.setText("Doctor " + docName2);

		doctorBox.getItems().add(docName1);
		doctorBox.getItems().add(docName2);

		doc1DayFilter.setText(docName1);
		doc2DayFilter.setText(docName2);

		agendaDoc1Box.setText(docName1);
		agendaDoc2Box.setText(docName2);

		initializeBoxes();

		doc1DayFilter.setSelected(true);
		doc2DayFilter.setSelected(true);

		doctor1WeekTag.setSelected(true);
		doctor2WeekTag.setSelected(true);
	}

	public static void setName(String name) { SecretaryScreenController.name = name; }

	public static void setDoc1Name(String name) { docName1 = name; }

	public static void setDoc2Name(String name) { docName2 = name; }

	private static String name;
	private static String docName1;
	private static String docName2;
	ObservableList<User> doctors;
	private MainController mc;
}

