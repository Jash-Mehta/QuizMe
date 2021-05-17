package com.example.quizme;
//public class User {
//    private String name,email,pass,phoneNo;
//    private long coins = 25;
//
//    public long getCoins() {
//        return coins;
//    }
//
//    public void setCoins(long coins) {
//        this.coins = coins;
//    }
//
//    public User() {
//    }
//
//    public User(String name, String email, String pass, String phoneNo) {
//        this.name = name;
//        this.email = email;
//        this.pass = pass;
//        this.phoneNo = phoneNo;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPass() {
//        return pass;
//    }
//
//    public void setPass(String pass) {
//        this.pass = pass;
//    }
//
//    public String getPhoneNo() {
//        return phoneNo;
//    }
//
//    public void setPhoneNo(String phoneNo) {
//        this.phoneNo = phoneNo;
//    }
//}
public class User {
    private String name, email, pass, profile, phoneno;
    private long coins = 0;

    public User() {
    }

    public User(String name, String email, String pass, String referCode) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phoneno = referCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getReferCode() {
        return phoneno;
    }

    public void setReferCode(String referCode) {
        this.phoneno = referCode;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}