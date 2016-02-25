package com.matteoveroni.mycopies.system.events;

/**
 *
 * @author Matteo Veroni
 */
public class CopyCompletationPercentageChangeEvent implements SystemEvent {

    private final double completationPercentage;
    
    public CopyCompletationPercentageChangeEvent(double completationPercentage) {
        this.completationPercentage = completationPercentage;
    }

    public double getCompletationPercentage() {
        return completationPercentage;
    }
        
}
