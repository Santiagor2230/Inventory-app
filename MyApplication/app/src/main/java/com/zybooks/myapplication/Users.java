package com.zybooks.myapplication;

public class Users {
    int id;
    String user_name;
    String user_username;
    String user_pass;

    public Users() {
        super();
    }

    public Users(int i, String name, String username, String password) {
        super();
        this.id = i;
        this.user_name = name;
        this.user_username = username;
        this.user_pass = password;
    }

    // constructor
    public Users(String name, String username, String password) {
        this.user_name = name;
        this.user_username = username;
        this.user_pass = password;
    }

    //setter and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }


    public String getUserUsername() {
        return user_username;
    }

    public void setUserUsername(String username) {
        this.user_username = username;
    }

    public String getUserPass() {
        return user_pass;
    }

    public void setUserPass(String pass) {
        this.user_pass = pass;
    }
}
