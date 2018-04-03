package database;

import javafx.collections.ObservableList;

public class TestDriverExtraLangToOk { //THIS DRIVER WAS USED TO TEST DBController and Database functions
	public static void main(String args[]) {
		DBController db = new DBController();
		
		db.loadAppointments(); 
		//db.createAppointment("Sarah G", "Philip Kay", 5, 6, 2018, 14, 00); 
		//db.createAppointment("Jon Bon", "Eric White", 5, 9, 2018, 16, 30);
		//db.createAppointment("Mara H", "Eric White", 5, 9, 2018, 18, 30);
		//db.deleteAppointmentForD("Eric White", 2, 5, 2018, 13, 30); 
		
		//ObservableList<Appointment> test = db.filterbyDoctor("Eric White");
		//ObservableList<Appointment> test = db.filterbyMonth(9, 2018);
		
		/*ObservableList<Appointment> test = db.filterbyDay(5, 6, 2018);
		for (int i = 0; i<test.size(); i++) {
			db.getAppointments().get(i).printAppointment();
		}
		System.out.println("SIZE: " + test.size()); 
		*/
		
	}
}
