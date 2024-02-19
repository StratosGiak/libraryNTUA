package com.stratos.giak.libraryntua;

import javafx.event.Event;
import javafx.event.EventType;

public final class CustomEvents {
    public static final EventType<Event> LINK_REGISTER_EVENT = new EventType<>("register");
    public static final EventType<Event> LINK_LOGIN_EVENT = new EventType<>("login");
}
