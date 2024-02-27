package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class Loans {
    private static Loans instance;
    private transient ObservableList<LoanModel> loanList = FXCollections.observableArrayList();

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

    public ObservableList<LoanModel> getLoanList() {
        return loanList;
    }

    void addLoan(LoanModel loanModel) {
        getLoanList().add(loanModel);
    }

    void removeLoan(UUID uuid) {
        getLoanList().removeIf(loan -> loan.getUuid().equals(uuid));
    }

    void editLoan(UUID uuid, Integer rating, String comment) {
        LoanModel loan = getLoanList().stream().filter(loanModel -> loanModel.getUuid().equals(uuid)).findAny().orElse(null);
        if (loan == null)
            throw new IllegalArgumentException("Loan UUID does not exist");
        if (rating != null) loan.setRating(rating);
        if (comment != null) loan.setComment(comment);
    }

    void saveLoans() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/loans");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getLoanList()));
        objectStream.close();
        fileStream.close();
    }

    void loadLoans() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/loans");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        loanList = FXCollections.observableArrayList((ArrayList<LoanModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
