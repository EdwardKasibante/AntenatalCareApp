package com.example.maternalinfolive.Lists;

public class User_List {
    private int id;
    private String fname;
    private String lname;
    private String phone;
    private String address;
    private  String gender;
    private String date;
    private int type;

    public User_List() {
    }

    public User_List(int id, String fname, String lname, String phone, String address, String gender, String date, int type) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.date = date;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
