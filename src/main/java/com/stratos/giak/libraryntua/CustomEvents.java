package com.stratos.giak.libraryntua;

import javafx.event.Event;
import javafx.event.EventType;

import java.util.UUID;

public final class CustomEvents {
    public static final EventType<Event> LINK_REGISTER_EVENT = new EventType<>("register");
    public static final EventType<Event> LINK_LOGIN_EVENT = new EventType<>("login");

    public static final EventType<Event> EXIT_BOOK_EVENT = new EventType<>("exit book creation scene");
    public static final EventType<Event> EXIT_USER_EVENT = new EventType<>("exit user creation scene");

    public static class CreateBookEvent extends Event {
        public static final EventType<CreateBookEvent> CREATE_BOOK_EVENT = new EventType<>("create/edit book");
        private UUID uuid;

        public CreateBookEvent() {
            super(CreateBookEvent.CREATE_BOOK_EVENT);
        }

        public CreateBookEvent(UUID uuid) {
            this();
            this.uuid = uuid;
        }

        public UUID getUUID() {
            return uuid;
        }
    }

    public static class CreateUserEvent extends Event {
        public static final EventType<CreateUserEvent> CREATE_USER_EVENT = new EventType<>("create/edit user");
        private UUID uuid;

        public CreateUserEvent() {
            super(CreateUserEvent.CREATE_USER_EVENT);
        }

        public CreateUserEvent(UUID uuid) {
            this();
            this.uuid = uuid;
        }

        public UUID getUUID() {
            return uuid;
        }
    }

    public static class ViewBookDetailsEvent extends Event {
        public static final EventType<ViewBookDetailsEvent> VIEW_BOOK_DETAILS_EVENT = new EventType<>("view book details");
        private BookModel book;

        public ViewBookDetailsEvent() {
            super(CreateUserEvent.CREATE_USER_EVENT);
        }

        public ViewBookDetailsEvent(BookModel book) {
            this();
            this.book = book;
        }

        public BookModel getBook() {
            return book;
        }
    }
}
