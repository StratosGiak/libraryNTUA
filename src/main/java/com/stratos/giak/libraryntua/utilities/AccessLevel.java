package com.stratos.giak.libraryntua.utilities;

/**
 * Access level of registered users.
 */
public enum AccessLevel {
    /**
     * Administrator privileges.
     * Can add, modify and delete books and users, and can end loans.
     */
    ADMIN,
    /**
     * User privileges.
     * Can view books and request loans.
     */
    USER
}
