package gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import database.Appointment;
import database.DBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import gui.secretaryscreen*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.CalendarItem;
import model.WeekTableItem;


public class SecretaryScreenController {
	@FXML
	public GridPane calendarGrid;
	@FXML
	public Button btnNext;
	@FXML
	public Button btnPrev;
	@FXML
	public Label dateLabel;

	// For Day View
    @FXML
    private TableView<DayTableItem> dayTable;
    @FXML
    private TableColumn<DayTableItem, String> dayTime;
    @FXML
    private TableColumn<DayTableItem, String> dayEvent;

    // For Week View
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

	@FXML
	public Label dayViewDateLabel;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab profileTab;
	@FXML
	private Tab dayViewTab;
	@FXML
	private Tab weekViewTab;
	@FXML
	private Tab agendaViewTab;


	/*
	private int yearBound;
	public int monthToday;
	public int yearToday;
	*/
	private int selectedDay;
	private int selectedMonth;
	private int selectedYear;

	String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private CalendarDate date = new CalendarDate();
	private DBController db;

	public void initialize() {
		//GregorianCalendar cal = new GregorianCalendar();
		//DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		db = new DBController();
		db.loadAppointments();

		selectedMonth = date.getMonthBound() + 1;
		selectedDay = date.getDayBound();
		selectedYear = date.getYearBound();

		refreshCalendar(date.getMonthBound(), date.getYearBound());
		refreshDayPane(selectedMonth, selectedDay, selectedYear);

		//tabPane.getTabs().add(profileTab, dayViewTab, weekViewTab, agendaViewTab);
	}

	public void nextMonth(ActionEvent e) {
		if (date.getMonthToday() == 11) {
			date.setMonthToday(0);
			date.setYearToday(date.getYearToday() + 1);
		} else
			date.setMonthToday(date.getMonthToday() + 1);

		refreshCalendar(date.getMonthToday(), date.getYearToday());
}
	public void lastMonth(ActionEvent e){
		if (date.getMonthToday() == 0) {
			date.setMonthToday(11);
			date.setYearToday(date.getYearToday() - 1);
		} else
			date.setMonthToday(date.getMonthToday() - 1);

		refreshCalendar(date.getMonthToday(), date.getYearToday());
}


	public void refreshCalendar(int month, int year) {
		int nod, som, i;
		/*
		if (month == 0 && year <= yearBound-10)
            btnPrev.setDisable(true);
		if (month == 11 && year >= yearBound+100)
            btnNext.setDisable(true);
		*/

		dateLabel.setText(months[month] +" "+ Integer.toString(year));

		calendarGrid.getChildren().clear();

		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		for (i = 1; i <= nod; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			final int var = i;


			Button button = new Button(Integer.toString(i));
			button.setMinSize(32, 25);
			button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");


			button.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
		            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: orange; -fx-text-fill: #FFFFFF");

				}

			});
			button.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
		            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");

				}

			});

			button.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {


					if (event.getClickCount() == 2) {
						tabPane.getSelectionModel().select(dayViewTab);
						selectedDay = Integer.parseInt(button.getText());
						selectedMonth = date.getMonthToday() + 1;
						selectedYear = date.getYearToday();
						refreshDayPane(selectedMonth, selectedDay, selectedYear);
						dayViewDateLabel.setText(months[selectedMonth-1] +" "+ selectedDay +", "+selectedYear);

					}
					else {
						selectedDay = Integer.parseInt(button.getText());
						selectedMonth = date.getMonthToday() + 1;
						selectedYear = date.getYearToday();
						System.out.println(selectedDay + " " + months[selectedMonth-1] + " " + selectedYear);
					}

				}

			});

			calendarGrid.add(button, column, row);
		}

	}

	// Start working here.

	public void refreshDayPane(int month, int day, int year) {
		ObservableList<DayTableItem> data = initializeDayView(db.getAppointments());
		dayTime.setCellValueFactory(new PropertyValueFactory<DayTableItem, String>("time"));
		dayEvent.setCellValueFactory(new PropertyValueFactory<DayTableItem, String>("patient"));


		dayEvent.setCellFactory(column -> {
			return new TableCell<DayTableItem, String>() {
				@Override
				protected void updateItem(String patient, boolean empty) {
					super.updateItem(patient, empty);

					if (patient == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(patient);
						DayTableItem currentItem = getTableView().getItems().get(getIndex());

						/*
						if (currentItem.getColor() != null) {
							setTextFill(Color.WHITE);
							if (currentItem.getColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
						} else {

							setTextFill(Color.BLACK);
							setStyle("");
						}
						*/
					}
				}
			};
		});

		dayTable.setItems(data);
	}

	public ObservableList<DayTableItem> initializeDayView(ObservableList<Appointment> appointments) {
		ArrayList<Appointment> items = new ArrayList<Appointment>();

		// Adding.
		for (Appointment a: appointments) {
			items.add(a);
		}

		ArrayList<Appointment> itemsToDisplay = new ArrayList<Appointment>();
		ArrayList<DayTableItem> toTableItems = new ArrayList<>();

		for (int hour = 0; hour <= 23; hour++)
			for (int min = 0; min <= 30; min+=30) {

				if (min < 30) {
					toTableItems.add(new DayTableItem(hour + ":" + String.format("%02d", min), ""));
					toTableItems.get(toTableItems.size()-1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueStartMin(min);
					toTableItems.get(toTableItems.size()-1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueEndMin(min+29);
				} else {
					toTableItems.add(new DayTableItem("", ""));
					toTableItems.get(toTableItems.size() - 1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueStartMin(min);
					toTableItems.get(toTableItems.size() - 1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size() - 1).setValueEndMin(59);
				}
			}

		for (Appointment appointment : items) {
			if (appointment.getMonth() == selectedMonth && appointment.getDay() == selectedDay && appointment.getYear() == selectedYear) {
				System.out.println(selectedMonth + " " + selectedDay + " " + selectedYear);
				itemsToDisplay.add(appointment);
			}
		}

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
					displayTime.setPatient(item.getPatient());

					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE);
					else if (item instanceof Task && item.isDone() == false)
						displayTime.setColor(Color.GREEN);
					else
						displayTime.setColor(Color.DARKGREY);

					break;
					*/
				} else if (displayStartTime == startTime) {
					displayTime.setPatient(item.getPatient());
					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE);
					else if (item instanceof Task && item.isDone() == false)
						displayTime.setColor(Color.GREEN);
					else
						displayTime.setColor(Color.DARKGREY);
					*/
				} else if (displayStartTime >= startTime && endTime >= displayEndTime) {
					displayTime.setPatient(" ");
					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE);
					else if (item instanceof Task && item.isDone() == false)
						displayTime.setColor(Color.GREEN);
					else
						displayTime.setColor(Color.DARKGREY);
					*/
				}

				if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
						displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
					displayTime.setPatient(item.getPatient());

					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE);
					else
						displayTime.setColor(Color.GREEN);

					break;
					*/
				} else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
					displayTime.setPatient(item.getTitle());

					/*
					if (item instanceof Event)
						displayTime.setColor(Color.BLUE);
					else
						displayTime.setColor(Color.GREEN);
					*/
				} else {
					if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour)
						if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
							displayTime.setPatient(" ");

							/*
							if (item instanceof Event)
								displayTime.setColor(Color.BLUE);
							else
								displayTime.setColor(Color.GREEN);
							break;
							*/
						} else {
							displayTime.setPatient(" ");

							/*
							if (item instanceof Event)
								displayTime.setColor(Color.BLUE);
							else
								displayTime.setColor(Color.GREEN);
							*/
						}
				}

			}
		}
		return FXCollections.observableArrayList(toTableItems);
	}

}