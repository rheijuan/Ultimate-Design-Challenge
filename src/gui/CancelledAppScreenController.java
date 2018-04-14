package gui;

import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class CancelledAppScreenController implements Initializable {

	@FXML private ListView cancelledAppListView;
	@FXML private Button deleteButton,backCButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	@FXML //pag pinindot yung back to main
	private void goBacktoMain(ActionEvent event) throws IOException{

	}
}
