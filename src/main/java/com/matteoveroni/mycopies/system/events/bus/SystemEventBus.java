package com.matteoveroni.mycopies.system.events.bus;

import com.matteoveroni.mycopies.system.events.SystemEvent;
import com.matteoveroni.mycopies.system.events.listeners.SystemEventListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SystemEventBus {

    private final List<SystemEventListener> listeners = new CopyOnWriteArrayList<>();

    public synchronized void addEventListener(SystemEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public synchronized void removeEventListener(SystemEventListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
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