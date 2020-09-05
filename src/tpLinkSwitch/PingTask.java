package tpLinkSwitch;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.*;
import javafx.stage.Stage;
import javafx.event.EventHandler;


public class PingTask extends Task<DeviceBean> {
    private int localAddress;

    public PingTask(int address) {
        this.localAddress = address;
    }

    @Override
    protected DeviceBean call() {
        try {
            InetAddress current = InetAddress.getByName("192.168.178.".concat(Integer.toString(this.localAddress)));
            if (current.isReachable(100)) {
                if (current.getHostName().equals("HS100.fritz.box")) {
                    return new DeviceBean(current.getHostName(),current.getHostAddress());
                }
                return new DeviceBean("NaN",current.getHostAddress());
            }
            else {
                return new DeviceBean("NaN", current.getHostAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}









