package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


       Parent root = FXMLLoader.load(getClass().getResource("loginscreen.fxml"));

        primaryStage.setTitle("Calendar Application");
        primaryStage.setScene(new Scene(root, 324, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
