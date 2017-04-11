package com.johnarias.android.crudtest.model;

/**
 * Created by John on 09/04/2017.
 */
public class Student {
    int id;
    String firstname;
    String email;

    public Student(String firstname, String email) {
        this.firstname = firstname;
        this.email = email;
    }

    public Student(int id, String firstname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
