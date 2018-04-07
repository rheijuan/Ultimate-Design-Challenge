package database;
import model.*;
public class Appointment extends CalendarItem {

	private int appointmentID;
	private String patient; 
	private String doctor; 
	
	private int day; 
	private int month; 
	private int year; 
	
	private int starthour; 
	private int startmin;
	private int endhour; 
	private int endmin;
	
	public Appointment(int appointmentID, String patient, String doctor, int day, int month, int year, int starthour, int startmin, int endhour, int endmin) {
		
		this.appointmentID = appointmentID;
		this.patient = patient; 
		this.doctor = doctor; 
		this.day = day; 
		this.month = month; 
		this.year = year;
		this.starthour = starthour; 
		this.startmin = startmin; 
		this.endhour = endhour; 
		this.endmin = endmin;
	}
	
	public void printAppointment() {
		System.out.println("Appointment No: " + appointmentID + ", Patient: " + patient + ", Doctor: " + doctor); 
		System.out.println(" On " + day + "/" + month + "/" + year );
		System.out.println(" From " + starthour + ":" + startmin + ", To " + endhour + ":" + endmin);
	}
	
	public int getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getStartHour() {
		return starthour;
	}

	public void setStartHour(int starthour) {
		this.starthour = starthour;
	}

	public int getStartMin() {
		return startmin;
	}

	public void setStartMin(int startmin) {
		this.startmin = startmin;
	}

	public int getEndHour() {
		return endhour;
	}
	
	public void setEndHour(int endhour) {
		this.endhour = endhour; 
	}

	public int getEndMin() {
		return endmin;
	}
	
	public void setEndMin(int endmin) {
		this.endmin = endmin;
	}

}
