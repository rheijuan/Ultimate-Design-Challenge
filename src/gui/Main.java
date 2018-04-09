package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

<<<<<<< HEAD

       Parent root = FXMLLoader.load(getClass().getResource("secretaryscreen.fxml"));
=======
       Parent root = FXMLLoader.load(getClass().getResource("loginscreen.fxml"));
>>>>>>> 4e85d02df335e67ee56b88b0b0710629a55596da

        primaryStage.setTitle("Calendar Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
