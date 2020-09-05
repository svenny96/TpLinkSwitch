package tpLinkSwitch;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ProgressBean {

    private int progress = 0;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        int prev = this.progress;
        this.progress++;
        //System.out.printf("Old: %s New: %s%n", progress, this.progress);
        propertyChangeSupport.firePropertyChange("progress", prev, this.progress);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
