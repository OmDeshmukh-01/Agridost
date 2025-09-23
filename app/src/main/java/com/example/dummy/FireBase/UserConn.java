package com.example.dummy.FireBase;

public class UserConn {
    public String fullName;
    public String email;
    public String password;

    // Empty constructor (required for Firebase)
    public UserConn() {}

    public UserConn(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}
