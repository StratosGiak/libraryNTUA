package com.stratos.giak.libraryntua.models;

import com.stratos.giak.libraryntua.utilities.AccessLevel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

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

    /**
     * Constructs an object representing a user with the given account info.
     * <p>
     * No restrictions are imposed on any of the fields except that they cannot be blank.
     *
     * @param username    the username of the user
     * @param password    the password of the user
     * @param nameFirst   the nameFirst of the user
     * @param nameLast    the nameLast of the user
     * @param ID          the ID of the user
     * @param email       the email of the user
     * @param accessLevel the access level of the user
     * @throws IllegalArgumentException if any field is null or blank
     * @see AccessLevel
     */
    public UserModel(String username, String password, String nameFirst, String nameLast, String ID, String email, AccessLevel accessLevel) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                nameFirst == null || nameFirst.isBlank() ||
                nameLast == null || nameLast.isBlank() ||
                ID == null || ID.isBlank() ||
                email == null || email.isBlank() ||
                accessLevel == null
        ) throw new IllegalArgumentException("Invalid user account details");
        this.uuid = UUID.randomUUID();
        this.username.set(username);
        this.password.set(password);
        this.nameFirst.set(nameFirst);
        this.nameLast.set(nameLast);
        this.ID.set(ID);
        this.email.set(email);
        this.accessLevel.set(accessLevel);
    }

    /**
     * Gets the UUID of the user. Only used for (de)serialization.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * The property describing the access level of the user
     *
     * @see #getAccessLevel
     * @see #setAccessLevel(AccessLevel)
     */
    public SimpleObjectProperty<AccessLevel> accessLevelProperty() {
        return accessLevel;
    }

    /**
     * Gets the value of the access level property of the user
     */
    public AccessLevel getAccessLevel() {
        return accessLevel.get();
    }

    /**
     * Sets the value of the access level property of the user.
     *
     * @throws IllegalArgumentException if the given value is null
     */
    public void setAccessLevel(AccessLevel accessLevel) {
        if (accessLevel == null) throw new IllegalArgumentException("Access level cannot be null");
        this.accessLevel.set(accessLevel);
    }

    /**
     * The property describing the username of the user
     *
     * @see #getUsername
     * @see #setUsername(String)
     */
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    /**
     * Gets the value of the username property of the user
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Sets the value of the username property of the user.
     *
     * @throws IllegalArgumentException if the given value is blank
     */
    public void setUsername(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be blank");
        this.username.set(username);
    }

    /**
     * The property describing the password of the user
     *
     * @see #getPassword
     * @see #setPassword(String)
     */
    public SimpleStringProperty passwordProperty() {
        return password;
    }

    /**
     * Gets the value of the password property of the user
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Sets the value of the password property of the user.
     *
     * @throws IllegalArgumentException if the given value is blank
     */
    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be blank");
        this.password.set(password);
    }

    /**
     * The property describing the first name of the user
     *
     * @see #getNameFirst
     * @see #setNameFirst(String)
     */
    public SimpleStringProperty nameFirstProperty() {
        return nameFirst;
    }

    /**
     * Gets the value of the first name property of the user
     */
    public String getNameFirst() {
        return nameFirst.get();
    }

    /**
     * Sets the value of the first name property of the user.
     *
     * @throws IllegalArgumentException if the given value is blank
     */
    public void setNameFirst(String nameFirst) {
        if (nameFirst == null || nameFirst.isBlank()) throw new IllegalArgumentException("First name cannot be blank");
        this.nameFirst.set(nameFirst);
    }

    /**
     * The property describing the last name of the user
     *
     * @see #getNameLast
     * @see #setNameLast(String)
     */
    public SimpleStringProperty nameLastProperty() {
        return nameLast;
    }

    /**
     * Gets the value of the last name property of the user
     */
    public String getNameLast() {
        return nameLast.get();
    }

    /**
     * Sets the value of the last name property of the user.
     *
     * @throws IllegalArgumentException if the given value is blank
     */
    public void setNameLast(String nameLast) {
        if (nameLast == null || nameLast.isBlank()) throw new IllegalArgumentException("Last name cannot be blank");
        this.nameLast.set(nameLast);
    }

    /**
     * The property describing the ID of the user
     *
     * @see #getID
     * @see #setID(String)
     */
    public SimpleStringProperty IDProperty() {
        return ID;
    }

    /**
     * Gets the value of the ID property of the user
     */
    public String getID() {
        return ID.get();
    }

    /**
     * Sets the value of the ID property of the user.
     *
     * @throws IllegalArgumentException if the given value is blank
     */
    public void setID(String ID) {
        if (ID == null || ID.isBlank()) throw new IllegalArgumentException("ID cannot be blank");
        this.ID.set(ID);
    }

    /**
     * The property describing the email of the user
     *
     * @see #getEmail
     * @see #setEmail(String)
     */
    public SimpleStringProperty emailProperty() {
        return email;
    }

    /**
     * Gets the value of the email property of the user
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Sets the value of the email property of the user.
     *
     * @throws IllegalArgumentException if the given value is blank
     */
    public void setEmail(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be blank");
        this.email.set(email);
    }

    /**
     * Edits all the account details of the user.
     * Skips modifying any details that are invalid
     * <p>
     * This is a convenience method to avoid calling all the setters each time a user is edited.
     */
    public void editUser(String username, String password, String nameFirst, String nameLast, String ID, String email, AccessLevel accessLevel) {
        if (username != null && !username.isBlank()) setUsername(username);
        if (password != null && !password.isBlank()) setPassword(password);
        if (nameFirst != null && !nameFirst.isBlank()) setNameFirst(nameFirst);
        if (nameLast != null && !nameLast.isBlank()) setNameLast(nameLast);
        if (ID != null && !ID.isBlank()) setID(ID);
        if (email != null && !email.isBlank()) setEmail(email);
        if (accessLevel != null) setAccessLevel(accessLevel);
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
