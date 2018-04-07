package gui;

import database.Appointment;
import database.DBController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import database.Appointment;

import java.awt.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class ReserveScreenController implements Initializable {
	
	@FXML private TextField nameLabel,starttimeLabel,endtimeLabel;
	@FXML private DatePicker dateLabel;
	@FXML private ComboBox doctorLabel;
	@FXML private Button reserveButton, cancelButton;
	@FXML private Label errorLabel;
	
	@FXML private CheckBox monCheck,tuesCheck,wedCheck,thursCheck,friCheck,satCheck,weekCheck,monthCheck;
	@FXML private Button applyButton,backRButton;
	
	//NOTE: SAME LANG YUNG GINAGAWA NG CANCEL BUTTON AT BACKRBUTTON
	
	 @Override
	    public void initialize(URL location, ResourceBundle resources) {
	      
	    }
	 @FXML //pag pinindot na yung reserve button
	 private void reserveSlot(ActionEvent event) throws IOException{
		 if(nameLabel.getText().isEmpty() || starttimeLabel.getText().isEmpty() || endtimeLabel.getText().isEmpty())//di ko alam kung pano mababasa yung sa date picker haha
			   errorLabel.setText("Make sure all fields have values");
		   else {

				  String title = nameLabel.getText();
				  
		/*		  String dateInString = dateLabel.getText();
				  String dateVars[] = dateInString.split("/");
				  int month = Integer.parseInt(dateVars[0]);
				  int day = Integer.parseInt(dateVars[1]);
				  int year = Integer.parseInt(dateVars[2]);
		*/		  
				  String starttime = starttimeLabel.getText();
				  String sTimeVars[] = starttime.split(":");
				  int starthour = Integer.parseInt(sTimeVars[0]);
				  int startminute = Integer.parseInt(sTimeVars[1]);
				  
				  String endttime = endtimeLabel.getText();
				  String eTimeVars[] = endttime.split(":");
				  int endhour = Integer.parseInt(eTimeVars[0]);
				  int endminute = Integer.parseInt(eTimeVars[1]);
	   }
	 }
	 @FXML //pag pinindot yung cancel button
	 private void goBack(ActionEvent event) throws IOException{
		 
	 }
	 @FXML //pag pinindot yung back to main
	 private void goBacktoMain(ActionEvent event) throws IOException{
		 
	 }
	 @FXML //pag pinindot yung apply button
	 private void applyRecurring(ActionEvent event) throws IOException{
		 
	 }
	
	
}
