package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PatientScreenController implements Initializable{
		
		@FXML
		private Button reserveButton,cancelButton,recurringButton;
		private Stage stage;
		
		@Override // This method is called by the FXMLLoader when initialization is complete
	    public void initialize(URL location, ResourceBundle resources) {

	    }
		
		@FXML
		 private void openReserve() throws IOException{
	        
	    }

	    @FXML
	    private void openCancelledApp() throws IOException{

	    }
	    @FXML
	    private void openRecurring() throws IOException{

	    }
	}
