package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class DayTableItem {

    private SimpleStringProperty time;
    private SimpleStringProperty patient;
    private SimpleStringProperty doctor;
    private String colorDoctor;

    public String getColorDoctor() {
        return colorDoctor;
    }

    public void setColorDoctor(String colorDoctor) {
        this.colorDoctor = colorDoctor;
    }

    public String getDoctor() {
        return doctor.get();
    }

    private int valueStartHour;
    private int valueEndHour;
    private int valueStartMin;
    private int valueEndMin;
    private int status;

    DayTableItem(String time, String patient, String doctor) {
        this.time = new SimpleStringProperty(time);
        this.patient = new SimpleStringProperty(patient);
        this.doctor = new SimpleStringProperty(doctor);
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

    public void setDoctor(String newDoctor) {
        doctor.set(newDoctor);
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
