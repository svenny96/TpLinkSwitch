package tpLinkSwitch;

import Controller.SetupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
       /* Parent root = FXMLLoader.load(getClass().getResource("setup.fxml")).;
        SetupController controller = new FXML
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/setup.fxml"));
        Parent root = loader.load();
        SetupController setupController = loader.getController();
        setupController.show(root);
        setupController.lookForDevices();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
