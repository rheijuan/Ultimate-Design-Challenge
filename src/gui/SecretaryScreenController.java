package gui;

import java.util.GregorianCalendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class SecretaryScreenController {
	@FXML
	public GridPane miniCalendar;
	@FXML
	public Button btnNext;
	@FXML
	public Button btnPrev;
	@FXML
	public Label monthLabel;
	@FXML
	public Label yearLabel;

	private int yearBound;
	public int monthToday;
	public int yearToday;

	public void initialize() {
		monthToday = 2;
		yearToday = 2017;

		refreshCalendar(monthToday, yearToday);

	}


	public void nextMonth(ActionEvent e) {
		if (monthToday == 11)
		{
			monthToday = 1;
			yearToday += 1;
		}
		else
		{
			monthToday += 1;
		}
		refreshCalendar(monthToday, yearToday);
	}
	public void lastMonth(ActionEvent e){
		if (monthToday == 1)
		{
			monthToday = 11;
			yearToday -= 1;
		}
		else
		{
			monthToday -= 1;
		}
		refreshCalendar(monthToday, yearToday);
	}


	public void refreshCalendar(int month, int year){
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som, i;

		if (month == 0 && year <= yearBound-10)
			btnPrev.setDisable(true);
		if (month == 11 && year >= yearBound+100)
			btnNext.setDisable(true);

		System.out.println(month);
		System.out.println(year);

		monthLabel.setText(months[month - 1]);
		yearLabel.setText(Integer.toString(year));


		miniCalendar.getChildren().clear();


		GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		for (i = 1; i <= nod; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			final int var = i;


			Button button = new Button(Integer.toString(i));
			button.setMinSize(32, 25);
			button.setStyle("-fx-background-color: transparent");
			
			
			button.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					button.setStyle("-fx-border-color: red");

				}

			});
			button.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					button.setStyle("-fx-background-color: transparent");
				}

			});
			
			miniCalendar.add(button, column, row);

		}

	}

}