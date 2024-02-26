package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class Loans {
    private static Loans instance;
    private transient ObservableMap<UUID, LoanModel> loanMap = FXCollections.observableHashMap();
    private transient ObservableList<LoanModel> loanList;

    public static Loans getInstance() {
        if (instance == null) {
            instance = new Loans();
            try {
                instance.loadLoans();
            } catch (IOException | ClassNotFoundException e) {
                return instance;
            } finally {
                instance.loanList = FXCollections.observableArrayList(instance.loanMap.values());
                instance.loanMap.addListener((MapChangeListener<UUID, LoanModel>) change -> {
                    if (change.wasAdded()) {
                        instance.loanList.add(change.getValueAdded());
                    }
                    if (change.wasRemoved()) {
                        instance.loanList.remove(change.getValueRemoved());
                    }
                });
            }
        }
        return instance;
    }

    public ObservableMap<UUID, LoanModel> getLoanMap() {
        return loanMap;
    }

    public ObservableList<LoanModel> getLoanList() {
        return loanList;
    }

    void addLoan(LoanModel loanModel) {
        getLoanMap().putIfAbsent(loanModel.getUuid(), loanModel);
        Users.getInstance().getUserByUUID(loanModel.getUuidUser()).getBorrowedList().add(loanModel.getUuid());
    }

    void removeLoan(UUID uuid) {
        LoanModel loanModel = getLoanMap().remove(uuid);
        if (loanModel == null) return;
        Users.getInstance().getUserByUUID(loanModel.getUuidUser()).getBorrowedList().removeIf(borrowUUID -> borrowUUID.equals(uuid));
    }

    void editLoan(UUID uuid, LoanModel loanModel) {
        if (getLoanMap().replace(uuid, loanModel) == null)
            throw new IllegalArgumentException("Book UUID does not exist");
    }

    void saveLoans() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/loans");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new HashMap<>(getLoanMap()));
        objectStream.close();
        fileStream.close();
    }

    void loadLoans() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/loans");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        loanMap = FXCollections.observableMap((HashMap<UUID, LoanModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
