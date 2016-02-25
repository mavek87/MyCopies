package com.matteoveroni.mycopies.copy;

import com.matteoveroni.mycopies.system.events.CopyCompletationPercentageChangeEvent;
import com.matteoveroni.mycopies.system.events.SystemEvent;
import com.matteoveroni.mycopies.system.events.listeners.SystemEventListener;

public class CopyPercentagePrinter implements SystemEventListener{

    @Override
    public void executeEvent(SystemEvent event) {
        if(event instanceof CopyCompletationPercentageChangeEvent){
            System.out.printf("New Percentage: %.0f",((CopyCompletationPercentageChangeEvent)event).getCompletationPercentage());
            System.out.println("%");
        }
    }
    
}
