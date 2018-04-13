package gui;

import javafx.beans.property.SimpleStringProperty;
import java.awt.Color;

public class DayTableItem {

    public DayTableItem(String time, String patient, int status) {
        this.time = new SimpleStringProperty(time);
        this.patient = new SimpleStringProperty(patient);
        this.status = status;
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getPatient() {
        return patient.get();
    }

    public void setPatient(String patient) {
        this.patient.set(patient);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private SimpleStringProperty time;
    private SimpleStringProperty patient;
    private int status;
    private Color color;
    private int valueStartHour;
    private int valueEndHour;
    private int valueStartMin;
    private int valueEndMin;
}