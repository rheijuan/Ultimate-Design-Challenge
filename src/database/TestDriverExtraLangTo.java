package database;


public class TestDriverExtraLangTo { //THIS DRIVER WAS USED TO TEST DBController and Database functions
	public static void main(String args[]) {
		DBController db = new DBController();

		//db.loadAppointments();
		/* 
		//db.createAppointment("Sofia M", "Philip Kay", 5, 10, 2018, 9, 00, 9, 30); 
		for (int i=0; i< db.getAppointments().size(); i++) {
			db.getAppointments().get(i).printAppointment();
		}
		*/
		
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
		
		//db.loadPatients();
		//db.createNewPatient("josemari", "123hello", "Jose Mari");
		
		//db.updateAppointmentDate("Sofia M", 9, 11, 2018);
		//db.updateAppointmentDate("Mara H", 9, 11, 2018);
		db.loadUsers();
		db.loadAppointments();
		//db.createNewUser("jamesbay", "madwildluv", "James Bay", "Patient");
		for (int i=0; i<db.getDoctors().size(); i++) {
			db.getDoctors().get(i).printUser();
		}
		
		/*
		for (int i=0; i<db.getAppointments().size(); i++) {
			db.getAppointments().get(i).printAppointment();
		}*/
		
	}
}
