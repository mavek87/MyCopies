package com.matteoveroni.mycopies.system.events.bus;

import com.matteoveroni.mycopies.system.events.SystemEvent;
import com.matteoveroni.mycopies.system.events.listeners.SystemEventListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SystemEventBus {

    private final List<SystemEventListener> listeners = new CopyOnWriteArrayList<>();

    public synchronized void addEventListener(SystemEventListener busListener) {
        if (!listeners.contains(busListener)) {
            listeners.add(busListener);
        }
    }

    public synchronized void removeEventListener(SystemEventListener busListener) {
        if (listeners.contains(busListener)) {
            listeners.remove(busListener);
        }
    }

    public void sendEventOnBus(SystemEvent systemEvent) {
        for (SystemEventListener listener : listeners) {
            listener.executeEvent(systemEvent);
        }
    }

    public void clear() {
        listeners.clear();
    }
}