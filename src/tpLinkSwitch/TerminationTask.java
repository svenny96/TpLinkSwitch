package tpLinkSwitch;

import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class TerminationTask extends Task<Void> {

    private ExecutorService executor;

    public TerminationTask(ExecutorService executor) {
        this.executor = executor;
    }

    @Override public Void call() {
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
