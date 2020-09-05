package Controller;

import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import tpLinkSwitch.*;

import javax.swing.event.ChangeEvent;
import java.util.List;
import java.util.concurrent.*;

public class MainScreenController {



    private ObservableMap<String, DeviceBean> localAddresses;
    private ObservableList<DeviceBean> avaiableDevices;
    private ExecutorService executor;

    @FXML
    TableView mainScreenTable;

    @FXML
    TableColumn colHostName;

    @FXML
    TableColumn colAddress;

    @FXML
    TableColumn colBtn;

    @FXML
    Label statusMsg;


    public MainScreenController() {

        this.avaiableDevices = FXCollections.observableArrayList();
        this.localAddresses = FXCollections.observableHashMap();

        for(int i=0; i<=255;i++) {
            String currentIp = "192.168.178.".concat(Integer.toString(i));
            this.localAddresses.put(currentIp, new DeviceBean("NaN", currentIp));

        }
    }

    @FXML
    public void initialize() {
        statusMsg.setText("");

        colHostName.setCellValueFactory(new PropertyValueFactory<DeviceBean, String>("hostName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<DeviceBean, String>("address"));
        colBtn.setCellFactory((param) -> new toggleCellFactory<DeviceBean, DeviceBean>());
        mainScreenTable.setItems(this.avaiableDevices);

        double tableWidth = mainScreenTable.getPrefWidth();
        colHostName.prefWidthProperty().bind(mainScreenTable.prefWidthProperty().multiply(0.3));
        colAddress.prefWidthProperty().bind(mainScreenTable.prefWidthProperty().multiply(0.4));
        colBtn.prefWidthProperty().bind(mainScreenTable.prefWidthProperty().multiply(0.3));
        this.startDiscovery();


    }

    public void show(Parent root) {
        Stage stage = new Stage();
        stage.setTitle("Main Screen");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/table.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        JMetro jmetro = new JMetro(Style.LIGHT);
        jmetro.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if(!this.executor.equals(null)) {
                System.out.println("Forcing Shutdown");
                this.executor.shutdownNow();

            }
        });
    }

    private class toggleCellFactory<S, T> extends TableCell<S, T> {

        public toggleCellFactory() {

        }
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                ToggleButton toggleBtn = new ToggleButton();
                toggleBtn.setAlignment(Pos.CENTER);
                toggleBtn.setSelected(false);
                toggleBtn.setText("AUS");
                toggleBtn.setMinWidth(70);
                toggleBtn.setOnAction((ActionEvent e) -> {
                    DeviceBean current = (DeviceBean) getTableRow().getItem();
                    if(toggleBtn.isSelected()) {
                        toggleBtn.setText("AN");
                        try {
                            Hs100Api.switchOn(current.getAddress());
                        }
                        catch (Exception exception) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Device not avaiable. Please try again later.");
                            alert.show();
                        }

                    }
                    else {
                        toggleBtn.setText("AUS");
                        Hs100Api.switchOff(current.getAddress());
                    }

                    System.out.println(current.getAddress());
                });
                setGraphic(toggleBtn);
            }
            else {
                setGraphic(null);
            }
        }

    }

    public void startDiscovery() {
            System.out.println("initialize");
            try {
                this.executor = Executors.newFixedThreadPool(32);
                for (int i = 0; i <= 255; i++) {
                    PingTask task = new PingTask(i);
                    task.setOnSucceeded(e -> {
                        DeviceBean deviceInfo = task.getValue();
                        if(!deviceInfo.getHostName().equals("NaN") && this.localAddresses.get(deviceInfo.getAddress()).getHostName().equals("NaN")) {
                            this.addDevice(deviceInfo);
                        }
                        else if(deviceInfo.getHostName().equals("NaN") && !this.localAddresses.get(deviceInfo.getAddress()).getHostName().equals("NaN")) {
                            this.removeDevice(deviceInfo);
                        }
                    });
                    this.executor.execute(task);
                }
                this.executor.shutdown();

                TerminationTask terminationTask = new TerminationTask(executor);
                terminationTask.setOnSucceeded(e -> {
                    this.startDiscovery();
                });
                new Thread(terminationTask).start();
                //Start a Task in order to check for the Termination of previous Tasks
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }

    public synchronized void addDevice(DeviceBean device) {
        this.localAddresses.put(device.getAddress(), device);
        if(!this.existsDevice(device)) {
            this.avaiableDevices.add(device);
        }


    }

    public void removeDevice(DeviceBean device) {
        this.localAddresses.put(device.getAddress(), new DeviceBean("NaN", device.getAddress()));
        for(int i=0; i<this.avaiableDevices.size(); i++) {
            if(device.getAddress().equals(this.avaiableDevices.get(i).getAddress())) {
                this.avaiableDevices.remove(i);
            }
        }
    }

    private boolean existsDevice(DeviceBean device) {
        for(DeviceBean item : this.avaiableDevices) {
            if(item.getAddress().equals(device.getAddress())) {
                return true;
            }
        }
        return false;
    }
}




