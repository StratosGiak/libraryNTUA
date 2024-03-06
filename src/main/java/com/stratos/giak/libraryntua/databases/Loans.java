package com.stratos.giak.libraryntua.databases;

import com.stratos.giak.libraryntua.models.BookModel;
import com.stratos.giak.libraryntua.models.LoanModel;
import com.stratos.giak.libraryntua.models.ReviewModel;
import com.stratos.giak.libraryntua.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Loans {
    private static Loans instance;
    private transient ObservableList<LoanModel> loanList = FXCollections.observableArrayList();

    private Loans() {
    }

    /**
     * Returns a singleton instance of the Loans database object.
     * The Loans object manages all operations that concern the open book loans.
     *
     * @return a Loans instance
     * @see LoanModel
     */
    public static Loans getInstance() {
        if (instance == null) {
            instance = new Loans();
            try {
                instance.loadLoans();
            } catch (IOException ignored) {
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Serialized book file is not of type List<LoanModel>");
            }
        }
        return instance;
    }

    /**
     * @return an observable list of all loans, represented by {@link LoanModel} objects
     */
    public ObservableList<LoanModel> getLoanList() {
        return loanList;
    }

    /**
     * Gets an observable list containing all the loans belonging to the given user.
     */
    public ObservableList<LoanModel> getLoanListByUser(UserModel user) {
        return FXCollections.observableArrayList(Loans.getInstance().getLoanList().filtered(loan -> loan.getUser().equals(user)));
    }

    /**
     * Adds the given loan to the list of loans.
     * Also decrements the amount of copies of the corresponding book.
     *
     * @param loan the loan to be added
     */
    public void addLoan(LoanModel loan) {
        getLoanList().add(loan);
        loan.getBook().setCopies(loan.getBook().getCopies() - 1);
    }

    /**
     * Ends the given loan if it is still open.
     * Also submits the user's review if it is not empty,
     * and increments the amount of copies of the corresponding book
     *
     * @param loan the loan to be removed
     */
    public void endLoan(LoanModel loan) {
        BookModel book = loan.getBook();
        book.setCopies(book.getCopies() + 1);
        if (loan.getRating() != 0 || loan.getComment() != null && !loan.getComment().isBlank())
            book.addReview(new ReviewModel(loan.getUser().getUsername(), loan.getRating(), loan.getComment()));
        getLoanList().remove(loan);
    }

    /**
     * Ends all loans that belong to the given user.
     * Used when a user is deleted via {@link Users#removeUser}
     *
     * @param user the user whose loans will be removed
     */
    public void removeAllWithUser(UserModel user) {
        Iterator<LoanModel> iterator = getLoanList().iterator();
        while (iterator.hasNext()) {
            LoanModel loan = iterator.next();
            if (!loan.getUser().equals(user)) continue;
            BookModel book = loan.getBook();
            book.setCopies(book.getCopies() + 1);
            if (loan.getRating() != 0 || loan.getComment() != null && !loan.getComment().isBlank())
                book.addReview(new ReviewModel(loan.getUser().getUsername(), loan.getRating(), loan.getComment()));
            iterator.remove();
        }
    }

    /**
     * Ends all loans that involve the given book.
     * Used when a book is deleted via {@link Books#removeBook}
     *
     * @param book the book whose loans will be removed
     */
    public void removeAllWithBook(BookModel book) {
        Iterator<LoanModel> iterator = getLoanList().iterator();
        while (iterator.hasNext()) {
            LoanModel loan = iterator.next();
            if (!loan.getBook().equals(book)) continue;
            BookModel bk = loan.getBook();
            bk.setCopies(bk.getCopies() + 1);
            iterator.remove();
        }
    }

    /**
     * Serializes the list of loans.
     * Saves the list of loans as a file named "loans",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException if there is any error writing to the file
     */
    public void saveLoans() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/loans");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getLoanList()));
        objectStream.close();
        fileStream.close();
    }

    /**
     * Deserializes the list of loans.
     * Loads the list of loans from a file named "loans",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException            if there is any error reading the file
     * @throws ClassNotFoundException if the serialized object is not a list of {@link LoanModel}
     */
    public void loadLoans() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/loans");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        loanList = FXCollections.observableArrayList((ArrayList<LoanModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
