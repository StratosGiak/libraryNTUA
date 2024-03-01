package com.stratos.giak.libraryntua;

import javafx.event.Event;
import javafx.event.EventType;

public final class CustomEvents {
    public static final EventType<Event> LINK_REGISTER_EVENT = new EventType<>("register");
    public static final EventType<Event> LINK_LOGIN_EVENT = new EventType<>("login");

    public static final EventType<Event> EXIT_BOOK_EVENT = new EventType<>("exit book creation scene");
    public static final EventType<Event> EXIT_USER_EVENT = new EventType<>("exit user creation scene");
    public static final EventType<Event> LOGOUT_EVENT = new EventType<>("logout current user");

    public static class EditBookEvent extends Event {
        public static final EventType<EditBookEvent> EDIT_BOOK_EVENT = new EventType<>("create/edit book");
        private BookModel book;

        public EditBookEvent() {
            super(EditBookEvent.EDIT_BOOK_EVENT);
        }

        public EditBookEvent(BookModel book) {
            this();
            this.book = book;
        }

        public BookModel getBook() {
            return book;
        }
    }

    public static class EditUserEvent extends Event {
        public static final EventType<EditUserEvent> EDIT_USER_EVENT = new EventType<>("create/edit user");
        private UserModel user;

        public EditUserEvent() {
            super(EditUserEvent.EDIT_USER_EVENT);
        }

        public EditUserEvent(UserModel user) {
            this();
            this.user = user;
        }

        public UserModel getUser() {
            return user;
        }
    }

    public static class ViewBookDetailsEvent extends Event {
        public static final EventType<ViewBookDetailsEvent> VIEW_BOOK_DETAILS_EVENT = new EventType<>("view book details");
        private BookModel book;

        public ViewBookDetailsEvent() {
            super(ViewBookDetailsEvent.VIEW_BOOK_DETAILS_EVENT);
        }

        public ViewBookDetailsEvent(BookModel book) {
            this();
            this.book = book;
        }

        public BookModel getBook() {
            return book;
        }
    }

    public static class ViewLoanDetailsEvent extends Event {
        public static final EventType<ViewLoanDetailsEvent> VIEW_LOAN_DETAILS_EVENT = new EventType<>("view loan details");
        private LoanModel loan;

        public ViewLoanDetailsEvent() {
            super(ViewLoanDetailsEvent.VIEW_LOAN_DETAILS_EVENT);
        }

        public ViewLoanDetailsEvent(LoanModel loan) {
            this();
            this.loan = loan;
        }

        public LoanModel getLoan() {
            return loan;
        }
    }
}
