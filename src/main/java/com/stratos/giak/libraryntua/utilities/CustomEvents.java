package com.stratos.giak.libraryntua.utilities;

import com.stratos.giak.libraryntua.models.BookModel;
import com.stratos.giak.libraryntua.models.LoanModel;
import com.stratos.giak.libraryntua.models.UserModel;
import javafx.event.Event;
import javafx.event.EventType;

public final class CustomEvents {
    /**
     * Event type signalling the press of the register hyperlink.
     */
    public static final EventType<Event> LINK_REGISTER_EVENT = new EventType<>("register");
    /**
     * Event type signalling the press of the login hyperlink.
     */
    public static final EventType<Event> LINK_LOGIN_EVENT = new EventType<>("login");
    /**
     * Event type signalling the exit from a book creation scene.
     */
    public static final EventType<Event> EXIT_BOOK_EVENT = new EventType<>("exit book creation scene");
    /**
     * Event type signalling the exit from a user creation scene.
     */
    public static final EventType<Event> EXIT_USER_EVENT = new EventType<>("exit user creation scene");
    /**
     * Event type signalling the exit from a loan details scene.
     */
    public static final EventType<Event> EXIT_LOAN_EVENT = new EventType<>("exit loan details screen");

    /**
     * Event signalling a book related action.
     */
    public static class BookEvent extends Event {
        /**
         * Event type signalling intent to edit a book.
         */
        public static final EventType<BookEvent> EDIT_BOOK_EVENT = new EventType<>("create/edit book");
        /**
         * Event type signalling intent to view the details of a book.
         */
        public static final EventType<BookEvent> VIEW_BOOK_DETAILS_EVENT = new EventType<>("view book details");
        private final BookModel book;

        /**
         * Constructs a book event of given type, referring to a given book.
         */
        public BookEvent(EventType<BookEvent> eventType, BookModel book) {
            super(eventType);
            this.book = book;
        }

        /**
         * Gets the book referenced by the event.
         */
        public BookModel getBook() {
            return book;
        }
    }

    /**
     * Event signalling a user related action.
     */
    public static class UserEvent extends Event {
        /**
         * Event type signalling intent to edit a user.
         */
        public static final EventType<UserEvent> EDIT_USER_EVENT = new EventType<>("create/edit user");
        private final UserModel user;

        /**
         * Constructs a user event referring to a given user.
         */
        public UserEvent(UserModel user) {
            super(UserEvent.EDIT_USER_EVENT);
            this.user = user;
        }

        /**
         * Gets the user referenced by the event.
         */
        public UserModel getUser() {
            return user;
        }
    }

    /**
     * Event signalling a loan related action.
     */
    public static class LoanEvent extends Event {
        /**
         * Event type signalling intent to view the details of a loan.
         */
        public static final EventType<LoanEvent> VIEW_LOAN_DETAILS_EVENT = new EventType<>("view loan details");
        private final LoanModel loan;

        /**
         * Constructs a loan event referring to a given loan.
         */
        public LoanEvent(LoanModel loan) {
            super(LoanEvent.VIEW_LOAN_DETAILS_EVENT);
            this.loan = loan;
        }

        /**
         * Gets the loan referenced by the event.
         */
        public LoanModel getLoan() {
            return loan;
        }
    }
}
