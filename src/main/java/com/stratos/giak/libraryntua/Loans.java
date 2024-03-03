package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

//TODO ADD DOCS
public class Loans {
    private static Loans instance;
    private transient ObservableList<LoanModel> loanList = FXCollections.observableArrayList();

    //TODO ADD DOCS
    public static Loans getInstance() {
        if (instance == null) {
            instance = new Loans();
            try {
                instance.loadLoans();
            } catch (IOException | ClassNotFoundException e) {
                return instance;
            }
        }
        return instance;
    }

    //TODO ADD DOCS
    public ObservableList<LoanModel> getLoanList() {
        return loanList;
    }

    //TODO ADD DOCS
    public void addLoan(LoanModel loanModel) {
        getLoanList().add(loanModel);
        loanModel.getBook().setCopies(loanModel.getBook().getCopies() - 1);
    }

    //TODO ADD DOCS
    public void endLoan(LoanModel loan) {
        BookModel book = loan.getBook();
        book.setCopies(book.getCopies() + 1);
        if (loan.getRating() != 0 || loan.getComment() != null && !loan.getComment().isBlank())
            book.addReview(new ReviewModel(loan.getUser().getUsername(), loan.getRating(), loan.getComment()));
        getLoanList().remove(loan);
    }

    //TODO ADD DOCS
    public void removeAllWithUser(UserModel user) {
        Iterator<LoanModel> iterator = getLoanList().iterator();
        while (iterator.hasNext()) {
            LoanModel loan = iterator.next();
            if (!loan.getUser().equals(user)) continue;
            BookModel book = loan.getBook();
            book.setCopies(book.getCopies() + 1);
            iterator.remove();
        }
    }

    //TODO ADD DOCS
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

    //TODO ADD DOCS
    public void editLoan(LoanModel loan, Integer rating, String comment) {
        if (loan == null)
            throw new IllegalArgumentException("Loan object does not exist");
        if (rating != null) loan.setRating(rating);
        if (comment != null) loan.setComment(comment);
    }

    //TODO ADD DOCS
    public void saveLoans() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/loans");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getLoanList()));
        objectStream.close();
        fileStream.close();
    }

    //TODO ADD DOCS
    public void loadLoans() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/loans");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        loanList = FXCollections.observableArrayList((ArrayList<LoanModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
