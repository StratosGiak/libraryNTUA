package com.stratos.giak.libraryntua;

import javafx.event.Event;
import javafx.event.EventType;

//TODO ADD DOCS
public final class CustomEvents {
    //TODO ADD DOCS
    public static final EventType<Event> LINK_REGISTER_EVENT = new EventType<>("register");
    //TODO ADD DOCS
    public static final EventType<Event> LINK_LOGIN_EVENT = new EventType<>("login");
    //TODO ADD DOCS
    public static final EventType<Event> EXIT_BOOK_EVENT = new EventType<>("exit book creation scene");
    //TODO ADD DOCS
    public static final EventType<Event> EXIT_USER_EVENT = new EventType<>("exit user creation scene");
    //TODO ADD DOCS
    public static final EventType<Event> EXIT_LOAN_EVENT = new EventType<>("exit loan details screen");

    //TODO ADD DOCS
    public static class EditBookEvent extends Event {
        //TODO ADD DOCS
        public static final EventType<EditBookEvent> EDIT_BOOK_EVENT = new EventType<>("create/edit book");
        private BookModel book;

        //TODO ADD DOCS
        public EditBookEvent() {
            super(EditBookEvent.EDIT_BOOK_EVENT);
        }

        //TODO ADD DOCS
        public EditBookEvent(BookModel book) {
            this();
            this.book = book;
        }

        //TODO ADD DOCS
        public BookModel getBook() {
            return book;
        }
    }

    //TODO ADD DOCS
    public static class EditUserEvent extends Event {
        //TODO ADD DOCS
        public static final EventType<EditUserEvent> EDIT_USER_EVENT = new EventType<>("create/edit user");
        private UserModel user;

        //TODO ADD DOCS
        public EditUserEvent() {
            super(EditUserEvent.EDIT_USER_EVENT);
        }

        //TODO ADD DOCS
        public EditUserEvent(UserModel user) {
            this();
            this.user = user;
        }

        //TODO ADD DOCS
        public UserModel getUser() {
            return user;
        }
    }

    //TODO ADD DOCS
    public static class ViewBookDetailsEvent extends Event {
        //TODO ADD DOCS
        public static final EventType<ViewBookDetailsEvent> VIEW_BOOK_DETAILS_EVENT = new EventType<>("view book details");
        private BookModel book;

        //TODO ADD DOCS
        public ViewBookDetailsEvent() {
            super(ViewBookDetailsEvent.VIEW_BOOK_DETAILS_EVENT);
        }

        //TODO ADD DOCS
        public ViewBookDetailsEvent(BookModel book) {
            this();
            this.book = book;
        }

        //TODO ADD DOCS
        public BookModel getBook() {
            return book;
        }
    }

    //TODO ADD DOCS
    public static class ViewLoanDetailsEvent extends Event {
        //TODO ADD DOCS
        public static final EventType<ViewLoanDetailsEvent> VIEW_LOAN_DETAILS_EVENT = new EventType<>("view loan details");
        private LoanModel loan;

        //TODO ADD DOCS
        public ViewLoanDetailsEvent() {
            super(ViewLoanDetailsEvent.VIEW_LOAN_DETAILS_EVENT);
        }

        //TODO ADD DOCS
        public ViewLoanDetailsEvent(LoanModel loan) {
            this();
            this.loan = loan;
        }

        //TODO ADD DOCS
        public LoanModel getLoan() {
            return loan;
        }
    }
}
