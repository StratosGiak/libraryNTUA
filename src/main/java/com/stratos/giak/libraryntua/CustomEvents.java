package com.stratos.giak.libraryntua;

import javafx.event.Event;
import javafx.event.EventType;

public final class CustomEvents {
    public static final EventType<Event> LINK_REGISTER_EVENT = new EventType<>("register");
    public static final EventType<Event> LINK_LOGIN_EVENT = new EventType<>("login");
    public static final EventType<Event> EXIT_BOOK_EVENT = new EventType<>("exit book creation scene");
    public static final EventType<Event> EXIT_USER_EVENT = new EventType<>("exit user creation scene");
}
