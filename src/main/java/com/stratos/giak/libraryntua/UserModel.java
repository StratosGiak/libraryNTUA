package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.UUID;

public class UserModel implements Serializable {
    private final UUID uuid;
    private transient SimpleStringProperty username = new SimpleStringProperty();
    private transient SimpleStringProperty password = new SimpleStringProperty();
    private transient SimpleStringProperty nameFirst = new SimpleStringProperty();
    private transient SimpleStringProperty nameLast = new SimpleStringProperty();
    private transient SimpleStringProperty ID = new SimpleStringProperty();
    private transient SimpleStringProperty email = new SimpleStringProperty();
    private transient SimpleObjectProperty<AccessLevel> accessLevel = new SimpleObjectProperty<>();

    public UserModel(UUID uuid, String username, String password, String nameFirst, String nameLast, String ID, String email, AccessLevel accessLevel) {
        this.uuid = uuid;
        this.username.set(username);
        this.password.set(password);
        this.nameFirst.set(nameFirst);
        this.nameLast.set(nameLast);
        this.ID.set(ID);
        this.email.set(email);
        this.accessLevel.set(accessLevel);
    }

    public UserModel(UUID uuid, String username, String password, String nameFirst, String nameLast, String ID, String email) {
        this(uuid, username, password, nameFirst, nameLast, ID, email, AccessLevel.USER);
    }

    public UserModel(String username, String password, String nameFirst, String nameLast, String ID, String email) {
        this(UUID.randomUUID(), username, password, nameFirst, nameLast, ID, email, AccessLevel.USER);
    }

    public UserModel(String username, String password, String nameFirst, String nameLast, String ID, String email, AccessLevel accessLevel) {
        this(UUID.randomUUID(), username, password, nameFirst, nameLast, ID, email, accessLevel);
    }

    public ObservableList<LoanModel> getBorrowedList() {
        return FXCollections.observableArrayList(Loans.getInstance().getLoanList().stream().filter(loan -> loan.getUuidUser().equals(uuid)).toList());
    }

    public UUID getUUID() {
        return uuid;
    }

    public SimpleObjectProperty<AccessLevel> accessLevelProperty() {
        return accessLevel;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel.get();
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel.set(accessLevel);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public SimpleStringProperty nameFirstProperty() {
        return nameFirst;
    }

    public String getNameFirst() {
        return nameFirst.get();
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst.set(nameFirst);
    }

    public SimpleStringProperty nameLastProperty() {
        return nameLast;
    }

    public String getNameLast() {
        return nameLast.get();
    }

    public void setNameLast(String nameLast) {
        this.nameLast.set(nameLast);
    }

    public SimpleStringProperty IDProperty() {
        return ID;
    }

    public String getID() {
        return ID.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

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
