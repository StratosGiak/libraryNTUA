package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.UUID;

//TODO ADD DOCS
public class UserModel implements Serializable {
    private final UUID uuid;
    private transient SimpleStringProperty username = new SimpleStringProperty();
    private transient SimpleStringProperty password = new SimpleStringProperty();
    private transient SimpleStringProperty nameFirst = new SimpleStringProperty();
    private transient SimpleStringProperty nameLast = new SimpleStringProperty();
    private transient SimpleStringProperty ID = new SimpleStringProperty();
    private transient SimpleStringProperty email = new SimpleStringProperty();
    private transient SimpleObjectProperty<AccessLevel> accessLevel = new SimpleObjectProperty<>();

    //TODO ADD DOCS
    public UserModel(String username, String password, String nameFirst, String nameLast, String ID, String email, AccessLevel accessLevel) {
        this.uuid = UUID.randomUUID();
        this.username.set(username);
        this.password.set(password);
        this.nameFirst.set(nameFirst);
        this.nameLast.set(nameLast);
        this.ID.set(ID);
        this.email.set(email);
        this.accessLevel.set(accessLevel);
    }

    //TODO ADD DOCS
    public ObservableList<LoanModel> getBorrowedList() {
        return FXCollections.observableArrayList(Loans.getInstance().getLoanList().stream().filter(loan -> loan.getUser().equals(this)).toList());
    }

    //TODO ADD DOCS
    public UUID getUUID() {
        return uuid;
    }

    //TODO ADD DOCS
    public SimpleObjectProperty<AccessLevel> accessLevelProperty() {
        return accessLevel;
    }

    //TODO ADD DOCS
    public AccessLevel getAccessLevel() {
        return accessLevel.get();
    }

    //TODO ADD DOCS
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel.set(accessLevel);
    }

    //TODO ADD DOCS
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    //TODO ADD DOCS
    public String getUsername() {
        return username.get();
    }

    //TODO ADD DOCS
    public void setUsername(String username) {
        this.username.set(username);
    }

    //TODO ADD DOCS
    public SimpleStringProperty passwordProperty() {
        return password;
    }

    //TODO ADD DOCS
    public String getPassword() {
        return password.get();
    }

    //TODO ADD DOCS
    public void setPassword(String password) {
        this.password.set(password);
    }

    //TODO ADD DOCS
    public SimpleStringProperty nameFirstProperty() {
        return nameFirst;
    }

    //TODO ADD DOCS
    public String getNameFirst() {
        return nameFirst.get();
    }

    //TODO ADD DOCS
    public void setNameFirst(String nameFirst) {
        this.nameFirst.set(nameFirst);
    }

    //TODO ADD DOCS
    public SimpleStringProperty nameLastProperty() {
        return nameLast;
    }

    //TODO ADD DOCS
    public String getNameLast() {
        return nameLast.get();
    }

    //TODO ADD DOCS
    public void setNameLast(String nameLast) {
        this.nameLast.set(nameLast);
    }

    //TODO ADD DOCS
    public SimpleStringProperty IDProperty() {
        return ID;
    }

    //TODO ADD DOCS
    public String getID() {
        return ID.get();
    }

    //TODO ADD DOCS
    public void setID(String ID) {
        this.ID.set(ID);
    }

    //TODO ADD DOCS
    public SimpleStringProperty emailProperty() {
        return email;
    }

    //TODO ADD DOCS
    public String getEmail() {
        return email.get();
    }

    //TODO ADD DOCS
    public void setEmail(String email) {
        this.email.set(email);
    }

    @Serial
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeUTF(getUsername());
        stream.writeUTF(getPassword());
        stream.writeUTF(getNameFirst());
        stream.writeUTF(getNameLast());
        stream.writeUTF(getID());
        stream.writeUTF(getEmail());
        stream.writeObject(getAccessLevel());
    }

    @Serial
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        username = new SimpleStringProperty(stream.readUTF());
        password = new SimpleStringProperty(stream.readUTF());
        nameFirst = new SimpleStringProperty(stream.readUTF());
        nameLast = new SimpleStringProperty(stream.readUTF());
        ID = new SimpleStringProperty(stream.readUTF());
        email = new SimpleStringProperty(stream.readUTF());
        accessLevel = new SimpleObjectProperty<>((AccessLevel) stream.readObject());
    }

    //TODO ADD DOCS
    public String toString() {
        return String.format("""
                        Username: %s
                        Password: %s
                        Email: %s
                        Name: %s %s
                        ID: %s""",
                this.getUsername(),
                this.getPassword(),
                this.getEmail(),
                this.getNameFirst(),
                this.getNameLast(),
                this.getID());
    }
}
