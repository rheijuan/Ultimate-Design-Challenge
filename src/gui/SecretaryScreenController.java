package gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import gui.secretaryscreen*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class SecretaryScreenController {
	@FXML
	public GridPane calendarGrid;
	@FXML
	public Button btnNext;
	@FXML
	public Button btnPrev;
	@FXML
	public Label dateLabel;

	private int yearBound;
	public int monthToday;
	public int yearToday;

	String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private CalendarDate date = new CalendarDate();

	public void initialize() {
		GregorianCalendar cal = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		refreshCalendar(date.getMonthBound(), date.getYearBound());
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


	public void refreshCalendar(int month, int year){
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

			button.setStyle("-fx-background-color: transparent");
			button.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
		            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-border-color: blue");

				}

			});
			button.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
		            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");

				}

			});
			calendarGrid.add(button, column, row);

		}

	}

}