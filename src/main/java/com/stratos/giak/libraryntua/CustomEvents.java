package com.stratos.giak.libraryntua;

import javafx.event.Event;
import javafx.event.EventType;

public final class CustomEvents {
    public static final EventType<Event> LINK_REGISTER_EVENT = new EventType<>("register");
    public static final EventType<Event> LINK_LOGIN_EVENT = new EventType<>("login");

    public static final EventType<Event> EXIT_BOOK_EVENT = new EventType<>("exit book creation scene");
    public static final EventType<Event> EXIT_USER_EVENT = new EventType<>("exit user creation scene");

    public static class CreateBookEvent extends Event {
        public static final EventType<CreateBookEvent> CREATE_BOOK_EVENT = new EventType<>("create/edit book");
        private BookModel book;

        public CreateBookEvent() {
            super(CreateBookEvent.CREATE_BOOK_EVENT);
        }

        public CreateBookEvent(BookModel book) {
            this();
            this.book = book;
        }

        public BookModel getBook() {
            return book;
        }
    }

    public static class CreateUserEvent extends Event {
        public static final EventType<CreateUserEvent> CREATE_USER_EVENT = new EventType<>("create user");
        private UserModel user;

        public CreateUserEvent() {
            super(CreateUserEvent.CREATE_USER_EVENT);
        }

        public CreateUserEvent(UserModel user) {
            this();
            this.user = user;
        }

        public UserModel getUser() {
            return user;
        }
    }

}
