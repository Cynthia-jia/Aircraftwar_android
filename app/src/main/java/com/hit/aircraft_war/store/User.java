package com.hit.aircraft_war.store;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private String userEmail;
    private String userPassword;

    public User(String userEmail, String password) {
        this.userEmail = userEmail;
        this.userPassword = password;
    }

    public User() {}

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
