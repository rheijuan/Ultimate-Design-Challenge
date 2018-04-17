package model;

import database.Doctor;

public class Observer {
    protected Doctor doc;

    public void setDoctor(String user, String pass, String name) {
        doc = new Doctor(user, pass, name);
    }
}
