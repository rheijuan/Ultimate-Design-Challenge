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

	public void initialize() {
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date d = new Date();
        
		//refreshCalendar();

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


		dateLabel.setText(months[month - 1] +" "+ Integer.toString(year));
		

		calendarGrid.getChildren().clear();


		GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		for (i = 1; i <= nod; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			final int var = i;
	

			Button button = new Button(Integer.toString(i));
			button.setMinSize(32, 25);
<<<<<<< HEAD
            button.setStyle("-fx-font-family: 'Avenir 85 Heavy'; -fx-font-size: 10px; -fx-background-color: transparent; -fx-text-fill: #FFFFFF");

			
			
=======
			button.setStyle("-fx-background-color: transparent");
>>>>>>> 4e85d02df335e67ee56b88b0b0710629a55596da
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