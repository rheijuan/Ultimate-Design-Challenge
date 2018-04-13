package gui;

import javafx.beans.property.SimpleStringProperty;

import java.awt.*;

public class WeekTableItem {

    public WeekTableItem(String time) {
        this.time = new SimpleStringProperty(time);
        this.sunEvent = new SimpleStringProperty("");
        this.monEvent = new SimpleStringProperty("");
        this.tueEvent = new SimpleStringProperty("");
        this.wedEvent = new SimpleStringProperty("");
        this.thuEvent = new SimpleStringProperty("");
        this.friEvent = new SimpleStringProperty("");
        this.satEvent = new SimpleStringProperty("");
    }

    public void setEvent(String event, String day) {
        if (day.equalsIgnoreCase("Sun")) {
            setSunEvent(event);
        } else if (day.equalsIgnoreCase("Mon")) {
            setMonEvent(event);
        } else if (day.equalsIgnoreCase("Tue")) {
            setTueEvent(event);
        } else if (day.equalsIgnoreCase("Wed")) {
            setWedEvent(event);
        } else if (day.equalsIgnoreCase("Thu")) {
            setThuEvent(event);
        } else if (day.equalsIgnoreCase("Fri")) {
            setFriEvent(event);
        } else if (day.equalsIgnoreCase("Sat")) {
            setSatEvent(event);
        }
    }

    public void setColor(Color color, String day) {
        if (day.equalsIgnoreCase("Sun")) {
            setSunColor(color);
        } else if (day.equalsIgnoreCase("Mon")) {
            setMonColor(color);
        } else if (day.equalsIgnoreCase("Tue")) {
            setTueColor(color);
        } else if (day.equalsIgnoreCase("Wed")) {
            setWedColor(color);
        } else if (day.equalsIgnoreCase("Thu")) {
            setThuColor(color);
        } else if (day.equalsIgnoreCase("Fri")) {
            setFriColor(color);
        } else if (day.equalsIgnoreCase("Sat")) {
            setSatColor(color);
        }
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getSunEvent() {
        return sunEvent.get();
    }

    public void setSunEvent(String sunEvent) {
        this.sunEvent.set(sunEvent);
    }

    public String getMonEvent() {
        return monEvent.get();
    }

    public void setMonEvent(String monEvent) {
        this.monEvent.set(monEvent);
    }

    public String getTueEvent() {
        return tueEvent.get();
    }

    public void setTueEvent(String tueEvent) {
        this.tueEvent.set(tueEvent);
    }

    public String getWedEvent() {
        return wedEvent.get();
    }

    public void setWedEvent(String wedEvent) {
        this.wedEvent.set(wedEvent);
    }

    public String getThuEvent() {
        return thuEvent.get();
    }

    public void setThuEvent(String thuEvent) {
        this.thuEvent.set(thuEvent);
    }

    public String getFriEvent() {
        return friEvent.get();
    }

    public void setFriEvent(String friEvent) {
        this.friEvent.set(friEvent);
    }

    public String getSatEvent() {
        return satEvent.get();
    }

    public void setSatEvent(String satEvent) {
        this.satEvent.set(satEvent);
    }

    public Color getSunColor() {
        return sunColor;
    }

    public void setSunColor(Color sunColor) {
        this.sunColor = sunColor;
    }

    public Color getMonColor() {
        return monColor;
    }

    public void setMonColor(Color monColor) {
        this.monColor = monColor;
    }

    public Color getTueColor() {
        return tueColor;
    }

    public void setTueColor(Color tueColor) {
        this.tueColor = tueColor;
    }

    public Color getWedColor() {
        return wedColor;
    }

    public void setWedColor(Color wedColor) {
        this.wedColor = wedColor;
    }

    public Color getThuColor() {
        return thuColor;
    }

    public void setThuColor(Color thuColor) {
        this.thuColor = thuColor;
    }

    public Color getFriColor() {
        return friColor;
    }

    public void setFriColor(Color friColor) {
        this.friColor = friColor;
    }

    public Color getSatColor() {
        return satColor;
    }

    public void setSatColor(Color satColor) {
        this.satColor = satColor;
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

    private SimpleStringProperty time;
    private SimpleStringProperty sunEvent;
    private SimpleStringProperty monEvent;
    private SimpleStringProperty tueEvent;
    private SimpleStringProperty wedEvent;
    private SimpleStringProperty thuEvent;
    private SimpleStringProperty friEvent;
    private SimpleStringProperty satEvent;
    private Color sunColor;
    private Color monColor;
    private Color tueColor;
    private Color wedColor;
    private Color thuColor;
    private Color friColor;
    private Color satColor;
    private int valueStartHour;
    private int valueEndHour;
    private int valueStartMin;
    private int valueEndMin;
}
