package com.example.maternalinfolive.Lists;

public class Info_List {
    private int id;
    private String subject;
    private String body;
    private String image;
    private String date_posted;
    private int post_category_id;
    private String post_category_name;
    private String  poster_type;
    private int poster_id;
    private String poster_fname;
    private String poster_lname;
    private String poster_phone;
    private int Trisemster_id;
    private String Trisemster_name;


    public Info_List() {
    }

    public Info_List(int id, String subject, String body, String image, String date_posted, int post_category_id, String post_category_name, String poster_type, int poster_id, String poster_fname, String poster_lname, String poster_phone, int trisemster_id, String trisemster_name) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.image = image;
        this.date_posted = date_posted;
        this.post_category_id = post_category_id;
        this.post_category_name = post_category_name;
        this.poster_type = poster_type;
        this.poster_id = poster_id;
        this.poster_fname = poster_fname;
        this.poster_lname = poster_lname;
        this.poster_phone = poster_phone;
        this.Trisemster_id = trisemster_id;
        this. Trisemster_name = trisemster_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public int getPost_category_id() {
        return post_category_id;
    }

    public void setPost_category_id(int post_category_id) {
        this.post_category_id = post_category_id;
    }

    public String getPost_category_name() {
        return post_category_name;
    }

    public void setPost_category_name(String post_category_name) {
        this.post_category_name = post_category_name;
    }

    public String getPoster_type() {
        return poster_type;
    }

    public void setPoster_type(String poster_type) {
        this.poster_type = poster_type;
    }

    public int getPoster_id() {
        return poster_id;
    }

    public void setPoster_id(int poster_id) {
        this.poster_id = poster_id;
    }

    public String getPoster_fname() {
        return poster_fname;
    }

    public void setPoster_fname(String poster_fname) {
        this.poster_fname = poster_fname;
    }

    public String getPoster_lname() {
        return poster_lname;
    }

    public void setPoster_lname(String poster_lname) {
        this.poster_lname = poster_lname;
    }

    public String getPoster_phone() {
        return poster_phone;
    }

    public void setPoster_phone(String poster_phone) {
        this.poster_phone = poster_phone;
    }

    public int getTrisemster_id() {
        return Trisemster_id;
    }

    public void setTrisemster_id(int trisemster_id) {
        Trisemster_id = trisemster_id;
    }

    public String getTrisemster_name() {
        return Trisemster_name;
    }

    public void setTrisemster_name(String trisemster_name) {
        Trisemster_name = trisemster_name;
    }
}


