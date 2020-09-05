package tpLinkSwitch;

import javafx.beans.property.SimpleStringProperty;

public class DeviceBean {
    private final SimpleStringProperty hostName;
    private final SimpleStringProperty address;

    public DeviceBean(String fHostname, String fAddress) {
        hostName = new SimpleStringProperty(fHostname);
        address = new SimpleStringProperty(fAddress);
    }

    public String getAddress() {
        return address.get();
    }

    public String getHostName() {
        return hostName.get();
    }

    public SimpleStringProperty hostNameProperty() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName.set(hostName);
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }




}
