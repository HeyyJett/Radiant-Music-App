package com.cognixia.radiant.DAO;

public class User {

    private int user_id;
    private String username;
    private String password;
    private String permission;

    // Might delete
    public User(int user_id, String username, String password){
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.permission = "USER";
    }

    public User(int user_id, String username, String password, String permission){
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.permission = permission;
    }

    public User(String username, String password, String permission){
        this.username = username;
        this.password = password;
        this.permission = permission;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.permission = "USER";
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User[" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", permission='" + permission + '\'' +
                ']';
    }
}
