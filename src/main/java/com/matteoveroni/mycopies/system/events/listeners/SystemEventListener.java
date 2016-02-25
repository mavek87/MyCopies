package com.matteoveroni.mycopies.system.events.listeners;

import com.matteoveroni.mycopies.system.events.SystemEvent;

public interface SystemEventListener {

    public void executeEvent(SystemEvent event);
}
