package Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tpLinkSwitch.DeviceBean;
import tpLinkSwitch.PingTask;
import tpLinkSwitch.TerminationTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SetupController {

    @FXML
    private AnchorPane setupAnchorPane;

    private List<DeviceBean> devices;

    public SetupController() {
        devices = new ArrayList<DeviceBean>();
    }


    public void lookForDevices() {
        //Start a  Task for each Subaddress
        try {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for (int i = 0; i <= 255; i++) {
                PingTask task = new PingTask(i);
                task.setOnSucceeded(e -> {
                    if (!task.getValue().getHostName().equals("NaN")) {
                        this.addToDevices(task.getValue());
                    }
                });
                executor.execute(task);
            }
            executor.shutdown();
            //Start a Task in order to check for the Termination of previous Tasks
            TerminationTask terminationTask = new TerminationTask(executor);
            terminationTask.setOnSucceeded(e -> {
                this.afterInitialSearch();
            });
            new Thread(terminationTask).start();
            //executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afterInitialSearch() {
        //Show next Stage and send found Devices to MainScreenController
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/mainScreen.fxml"));
            Parent root =loader.load();
            MainScreenController mainScreenController = loader.getController();
            for (DeviceBean item : this.devices) {
                mainScreenController.addDevice(item);
            }
            Stage stageToClose = (Stage) this.setupAnchorPane.getScene().getWindow();
            stageToClose.close();
            mainScreenController.show(root);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addToDevices(DeviceBean device) {
        this.devices.add(device);
    }

    public void show(Parent root) {
        Stage stage = new Stage();
        stage.setTitle("Loading");
        stage.setScene(new Scene(root));
        stage.show();
    }


}
