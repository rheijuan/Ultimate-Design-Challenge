package gui;

import javafx.beans.property.SimpleStringProperty;

public class DayTableItem {

    private SimpleStringProperty time;
    private SimpleStringProperty patient;
    private boolean occupied;
    private int valueStartHour;
    private int valueEndHour;
    private int valueStartMin;
    private int valueEndMin;

    DayTableItem(String time, String patient) {
        this.time = new SimpleStringProperty(time);
        this.patient = new SimpleStringProperty(patient);
    }

    public String getTime() {
        return time.get();
    }

    public String getPatient() {
        return patient.get();
    }

    public void setTime(String newTime) {
        time.set(newTime);
    }

    public void setPatient(String newPatient) {
        patient.set(newPatient);
    }

    public void occupy() {
        this.occupied = true;
    }

    public boolean isOccupied() {
        return occupied;
    }


    public int getValueStartHour() {
        return valueStartHour;
    }

    public void setValueStartHour(int valueStartHour) {
        this.valueStartHour = valueStartHour;
    }

    public int getValueEndHour() {
        return valueEndHour;
    }

    public void setValueEndHour(int valueEndHour) {
        this.valueEndHour = valueEndHour;
    }

    public int getValueStartMin() {
        return valueStartMin;
    }

    public void setValueStartMin(int valueStartMin) {
        this.valueStartMin = valueStartMin;
    }

    public int getValueEndMin() {
        return valueEndMin;
    }

    public void setValueEndMin(int valueEndMin) {
        this.valueEndMin = valueEndMin;
    }
}
