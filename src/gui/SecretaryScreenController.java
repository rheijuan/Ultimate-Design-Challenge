package gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.Node;
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
    @FXML
	private TableColumn<DayTableItem, String> dayDoctor;

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
	@FXML
	private Button prevWeek;
	@FXML
	private Button nextWeek;


	/*
	private int yearBound;
	public int monthToday;
	public int yearToday;
	*/
	private int selectedDay;
	private int selectedMonth;
	private int selectedYear;

	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
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
		refreshWeekPane(selectedMonth, selectedDay);

		//tabPane.getTabs().add(profileTab, dayViewTab, weekViewTab, agendaViewTab);
	}

	public void nextMonth(ActionEvent e) {
		if (date.getMonthToday() == 11) {
			date.setMonthToday(0);
			date.setYearToday(date.getYearToday() + 1);
		} else
			date.setMonthToday(date.getMonthToday() + 1);

		selectedMonth = date.getMonthToday();
		selectedYear = date.getYearToday();

		refreshCalendar(date.getMonthToday(), date.getYearToday());
		refreshWeekPane(date.getMonthToday(), date.getYearToday());
	}

	public void lastMonth(ActionEvent e) {
		if (date.getMonthToday() == 0) {
			date.setMonthToday(11);
			date.setYearToday(date.getYearToday() - 1);
		} else
			date.setMonthToday(date.getMonthToday() - 1);

		selectedMonth = date.getMonthToday();
		selectedYear = date.getYearToday();

		refreshCalendar(date.getMonthToday(), date.getYearToday());
		refreshWeekPane(date.getMonthToday(), date.getYearToday());
	}


	public void refreshCalendar(int month, int year) {
		int nod, som, i;
		/*
		if (month == 0 && year <= yearBound-10)
            btnPrev.setDisable(true);
		if (month == 11 && year >= yearBound+100)
            btnNext.setDisable(true);
		*/

		dateLabel.setText(months[month] + " " + Integer.toString(year));

		calendarGrid.getChildren().clear();

		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		for (i = 1; i <= nod; i++) {
			int row = new Integer((i + som - 2) / 7);
			int column = (i + som - 2) % 7;
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
						dayViewDateLabel.setText(months[selectedMonth - 1] + " " + selectedDay + ", " + selectedYear);

					} else {
						selectedDay = Integer.parseInt(button.getText());
						selectedMonth = date.getMonthToday() + 1;
						selectedYear = date.getYearToday();
						System.out.println(selectedDay + " " + months[selectedMonth - 1] + " " + selectedYear);
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
		dayDoctor.setCellValueFactory(new PropertyValueFactory<DayTableItem, String>("doctor"));

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

						if (currentItem.getColorDoctor().contains("#dc654d")) {
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: #dc654d");
						} else if (currentItem.getColorDoctor().contains("#000080")) {
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: #000080");
						}

					}
				}
			};
		});

		dayDoctor.setCellFactory(column -> {
			return new TableCell<DayTableItem, String>() {
				@Override
				protected void updateItem(String doctor, boolean empty) {
					super.updateItem(doctor, empty);

					if (doctor == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(doctor);
						DayTableItem currentItem = getTableView().getItems().get(getIndex());

						if (currentItem.getColorDoctor().contains("#dc654d")) {
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: #dc654d");
						} else if (currentItem.getColorDoctor().contains("#000080")) {
							setTextFill(Color.WHITE);
							setStyle("-fx-background-color: #000080");
						}

					}
				}
			};
		});

		dayTable.setItems(data);
	}

	public ObservableList<DayTableItem> initializeDayView(ObservableList<Appointment> appointments) {
		ArrayList<Appointment> items = new ArrayList<Appointment>();

		// Adding.
		for (Appointment a : appointments) {
			items.add(a);
		}

		ArrayList<Appointment> itemsToDisplay = new ArrayList<Appointment>();
		ArrayList<DayTableItem> toTableItems = new ArrayList<>();

		for (int hour = 0; hour <= 23; hour++)
			for (int min = 0; min <= 30; min += 30) {

				if (min < 30) {
					toTableItems.add(new DayTableItem(hour + ":" + String.format("%02d", min), null, null));
					toTableItems.get(toTableItems.size()-1).setValueStartHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueStartMin(min);
					toTableItems.get(toTableItems.size()-1).setValueEndHour(hour);
					toTableItems.get(toTableItems.size()-1).setValueEndMin(min+29);
				} else {
					toTableItems.add(new DayTableItem("", null, null));
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
					displayTime.setDoctor(item.getDoctor());

					if (item.getDoctor().contains("Eric White")) {
						displayTime.setColorDoctor("#dc654d");
					} else {
						displayTime.setColorDoctor("#000080");
					}
				} else if (displayStartTime == startTime) {
					displayTime.setPatient(item.getPatient());
					displayTime.setDoctor(item.getDoctor());

					if (item.getDoctor().contains("Eric White")) {
						displayTime.setColorDoctor("#dc654d");
					} else {
						displayTime.setColorDoctor("#000080");
					}
				} else if (displayStartTime >= startTime && endTime >= displayEndTime) {
					displayTime.setPatient(" ");
					displayTime.setDoctor(" ");

					if (item.getDoctor().contains("Eric White")) {
						displayTime.setColorDoctor("#dc654d");
					} else {
						displayTime.setColorDoctor("#000080");
					}
				}

				if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
						displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
					displayTime.setPatient(item.getPatient());
					displayTime.setDoctor(item.getDoctor());

					if (item.getDoctor().contains("Eric White")) {
						displayTime.setColorDoctor("#dc654d");
					} else {
						displayTime.setColorDoctor("#000080");
					}
				} else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
					displayTime.setPatient(item.getPatient());
					displayTime.setDoctor(item.getDoctor());


					if (item.getDoctor().contains("Eric White")) {
						displayTime.setColorDoctor("#dc654d");
					} else {
						displayTime.setColorDoctor("#000080");
					}
				} else {
					if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour)
						if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
							displayTime.setPatient(" ");
							displayTime.setDoctor(" ");

							if (item.getDoctor().contains("Eric White")) {
								displayTime.setColorDoctor("#dc654d");
							} else {
								displayTime.setColorDoctor("#000080");
							}
						} else {
							displayTime.setPatient(" ");
							displayTime.setDoctor(" ");

							if (item.getDoctor().contains("Eric White")) {
								displayTime.setColorDoctor("#dc654d");
							} else {
								displayTime.setColorDoctor("#000080");
							}
						}
				}

			}
		}
		return FXCollections.observableArrayList(toTableItems);
	}


	public void refreshWeekPane(int month, int year) {
		Date dateForWeek = new Date();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
		SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

		/*
		Node result = null;
		ObservableList<Node> childrens = calendarGrid.getChildren();

		for (Node node : childrens) {
			if (calendarGrid.getRowIndex(node) == row && calendarGrid.getColumnIndex(node) == col) {
				result = node;
				break;
			}
		}

		int month = 0;
		/*
		for (int m = 0; m < months.length; m++) {
			if (monthLabel.getText().compareTo(months[m]) == 0)
				month = m + 1;
		}


		String nodestring = result.toString();
		String num[] = nodestring.split("'");
		int day = Integer.parseInt(num[1]);
				*/

		cal.set(Calendar.MONTH,  selectedMonth);
		cal.set(Calendar.DATE, selectedDay);
		//cal.set(Calendar.YEAR, Integer.parseInt(yearLabel.getText()));

		int startWeekValue = -(cal.get(Calendar.DAY_OF_WEEK) - 1);

		cal.add(Calendar.DATE, startWeekValue);

		int monthCompare = Integer.parseInt(monthDeterminer.format(cal.getTime()));

		do {
			if (monthCompare != selectedMonth) {
				cal.add(Calendar.DATE, 1);
				monthCompare = Integer.parseInt(monthDeterminer.format(cal.getTime()));
			}
		} while (monthCompare != selectedMonth);

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
							if (currentItem.getSunColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else
								setStyle("-fx-background-color: green");
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
							if (currentItem.getMonColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getMonColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
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
							if (currentItem.getTueColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getTueColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
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
							if (currentItem.getWedColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getWedColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
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
							if (currentItem.getThuColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getThuColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
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
							if (currentItem.getFriColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getFriColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
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
							if (currentItem.getSatColor() == Color.BLUE)
								setStyle("-fx-background-color: blue");
							else if (currentItem.getSatColor() == Color.DARKGREY)
								setStyle("-fx-background-color: darkgray");
							else
								setStyle("-fx-background-color: green");
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


	public ObservableList<WeekTableItem> initializeWeekView(Calendar forWeekCalendar) {
		//ArrayList<CalendarItem> items = model.getItems();
		ArrayList<CalendarItem> itemsToDisplay = new ArrayList<CalendarItem>();

		ArrayList<WeekTableItem> toTableItems = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d");
		SimpleDateFormat monthDeterminer = new SimpleDateFormat("M");

		int month = Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()));

		int startDay = forWeekCalendar.get(Calendar.DATE);
		String[] date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
		int endDay = forWeekCalendar.get(Calendar.DATE);

		while (!date[0].equalsIgnoreCase("Sat")) {
			forWeekCalendar.add(Calendar.DATE, 1);
			date = sdf.format(forWeekCalendar.getTime()).split("\\s+");
			endDay = Integer.parseInt(date[2]);
		}

		toTableItems.add(new WeekTableItem(""));

		for (int hour = 0; hour <= 23; hour++)
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
	/*
		for (CalendarItem item : items) {
			if (item.getMonth() == month && (item.getDay() >= startDay && item.getDay() <= endDay)
					&& item.getYear() == Integer.parseInt(yearLabel.getText())) {
				if (eventCheck.isSelected() && item instanceof Event)
					itemsToDisplay.add(item);
				else if (taskCheck.isSelected() && item instanceof Task)
					itemsToDisplay.add(item);
			}
		}
		*/

		int monDate = 0, tueDate = 0, wedDate = 0, thuDate = 0, friDate = 0, satDate = 0, sunDate = 0;

		forWeekCalendar.set(selectedYear, selectedYear - 1, selectedDay - 1);
		String compareDay = sdf.format(forWeekCalendar.getTime()).substring(0, 3);
		System.out.println(compareDay);

		do {
			if (month == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime()))) {
				switch (compareDay) {
					case "Sun":
						sunDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						System.out.println(sunDate);
						break;
					case "Mon":
						monDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						System.out.println(monDate);
						break;
					case "Tue":
						tueDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						System.out.println(tueDate);
						break;
					case "Wed":
						wedDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						System.out.println(wedDate);
						break;
					case "Thu":
						thuDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						System.out.println(thuDate);
						break;
					case "Fri":
						friDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						break;
					case "Sat":
						satDate = Integer.parseInt(sdf.format(forWeekCalendar.getTime()).substring(8));
						System.out.println(satDate);
						break;
				}

				forWeekCalendar.add(Calendar.DATE, 1);
				compareDay = sdf.format(forWeekCalendar.getTime()).substring(0, 3);
			} else
				break;
		} while (!compareDay.equalsIgnoreCase("Sat"));

		if (month == Integer.parseInt(monthDeterminer.format(forWeekCalendar.getTime())))
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

		for (CalendarItem item : itemsToDisplay) {
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
					displayTime.setEvent(item.getTitle(), dayOfItem);



				if (displayTime.getTime().equalsIgnoreCase(""))
					continue;
				if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin &&
						displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {

						displayTime.setEvent(item.getTitle(), dayOfItem);
						/*
						if (item instanceof Event)
							displayTime.setColor(Color.BLUE, dayOfItem);
						else
							displayTime.setColor(Color.GREEN, dayOfItem);

						break;
						*/
				} else if (displayTime.getValueStartHour() == startHour && displayTime.getValueStartMin() == startMin) {
						displayTime.setEvent(item.getTitle(), dayOfItem);
						/*
						if (item instanceof Event)
							displayTime.setColor(Color.BLUE, dayOfItem);
						else
							displayTime.setColor(Color.GREEN, dayOfItem);
							*/
				} else {
					if (displayTime.getValueStartHour() >= startHour && displayTime.getValueEndHour() <= endHour) {
						if (displayTime.getValueEndHour() == endHour && displayTime.getValueEndMin() == endMin) {
								displayTime.setEvent(" ", dayOfItem);
								/*
								if (item instanceof Event)
									displayTime.setColor(Color.BLUE, dayOfItem);
								else
									displayTime.setColor(Color.GREEN, dayOfItem);
								break;
								*/
						} else {
								displayTime.setEvent(" ", dayOfItem);
							/*
								if (item instanceof Event)
									displayTime.setColor(Color.BLUE, dayOfItem);
								else
									displayTime.setColor(Color.GREEN, dayOfItem);
									*/
						}
					}
				}

				}
			}


		}
		return FXCollections.observableArrayList(toTableItems);
	}
}

