package database;

public class Appointment {

	int appointmentID;
	String patient; 
	String doctor; 
	
	int day; 
	int month; 
	int year; 
	
	int starthour; 
	int startmin;
	int endhour; 
	int endmin;
	
	public Appointment(int appointmentID, String patient, String doctor, int day, int month, int year, int starthour, int startmin) {

		this.appointmentID = appointmentID;
		this.patient = patient; 
		this.doctor = doctor; 
		this.day = day; 
		this.month = month; 
		this.year = year;
		this.starthour = starthour; 
		this.startmin = startmin; 
		
		//endhour and endmin are set here automatically (30 mins) 
		if (startmin == 0) {				//example 4:00
			endhour = starthour;			//end: 4:30
			endmin = startmin + 30;
		}
		else if (startmin == 30) {		    //example 5:30
			endhour = starthour + 1;		//end: 6:00
			endmin = 0;
		}
	}
	
	public void printAppointment() {
		System.out.println("Appointment No: " + appointmentID + ",Patient: " + patient + ", Doctor: " + doctor  + ", On " + month + "/" 
				+ day + "/" + year + ", At " + starthour + ":" + startmin);
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

	public int getEndMin() {
		return endmin;
	}
	
}
