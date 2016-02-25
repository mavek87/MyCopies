package com.matteoveroni.mycopies.copy;

import com.matteoveroni.mycopies.system.events.bus.SystemEventBus;
import com.matteoveroni.mycopies.system.events.CopyCompletationPercentageChangeEvent;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class CopyPercentageCheckerThread implements Runnable {

    private final SystemEventBus systemEventBus;

    private final File origin;
    private final File destinationToCheck;

    private final long sizeOfOrigin;

    private boolean copyCompletationCheckFlag = false;

    public CopyPercentageCheckerThread(SystemEventBus systemEventBus, File origin, File destinationToCheck) {
        this.systemEventBus = systemEventBus;
        this.origin = origin;
        this.destinationToCheck = destinationToCheck;
        sizeOfOrigin = FileUtils.sizeOfDirectory(origin);
    }

    @Override
    public void run() {
        setCopyCompleted(false);
        while (!isCopyCompleted()) {
            try {
                Thread.sleep(1000);
                double copyCompletationPercentage = calculateCopyPercentageCompletation();
                systemEventBus.sendEventOnBus(new CopyCompletationPercentageChangeEvent(copyCompletationPercentage));
            } catch (InterruptedException ex) {
            }
        }
        systemEventBus.sendEventOnBus(new CopyCompletationPercentageChangeEvent(100));
        System.out.println(Thread.currentThread().getName() + " I\'m dead");
    }

    private double calculateCopyPercentageCompletation() {
        double copyPercentage;
        long sizeOfDestination = FileUtils.sizeOfDirectory(destinationToCheck);
        try {
            copyPercentage = (((double)sizeOfDestination / (double)sizeOfOrigin) * 100);
        } catch (ArithmeticException ex) {
            copyPercentage = 100;
        }
        return copyPercentage;
    }

    private boolean isCopyCompleted() {
        if (FileUtils.sizeOfDirectory(origin) == FileUtils.sizeOfDirectory(destinationToCheck)) {
            setCopyCompleted(true);
        }
        return copyCompletationCheckFlag;
    }

    private void setCopyCompleted(boolean isCompleted) {
        copyCompletationCheckFlag = isCompleted;
    }

}
