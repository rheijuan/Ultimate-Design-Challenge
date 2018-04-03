package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBController {

    private ResultSet rs = null; 
    private Connection con = null; 
    private PreparedStatement pst = null; 
   
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList(); 

	/* --- getAppointments() 
	 *  Returns the ObservableList of all appointments 
	 *  (Call this after calling loadAppointments get the updated ObservableList)
	 */
    public static ObservableList<Appointment> getAppointments() { 
        return appointments;
    } 
    
	/* --- loadAppointments() 
	 * Loads or refreshes all appointments from the database and load to an ObservableList appointments.
	 */
	public void loadAppointments() 
    {
		 try {
	            con = ConnectDB.getConnection();
	        } catch (ClassNotFoundException ex) {
	            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (SQLException ex) {
	            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
	        }
		
        try{
	        pst = con.prepareStatement("SELECT * FROM clinic_tool.appointments");
	        rs = pst.executeQuery();
	        while (rs.next())
	        	appointments.add(new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10)));
	        				       //(FORMAT: appointmentID, patient name,doctor name,day,month,year,starthour,startminute, endhour, endminute)
        } catch (SQLException ex){
          Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
		System.out.println("ALL DATA FROM DATABASE READ AND ADDED TO OBSERVABLE LIST");
    }

	/* --- createAppointment(client, doctor, day, month, year, starthour, startminute) 
	 * Creates an appointment for a client to the chosen doctor given date and time details (start time only
	 * since end time is always 30 minutes from the start time)
	 */
	 public void createAppointment(String patient, String doctor, int day, int month, int year, int starthour, int startmin, int endhour, int endmin) {
		 
		Random rand = new Random();
		int id = rand.nextInt(500) + 1;
		Appointment newApp = new Appointment(id, patient, doctor, day, month, year, starthour, startmin, endhour, endmin);
   	    appointments.add(newApp);
		 
        String sql = "INSERT INTO clinic_tool.appointments VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    try{    
	    		pst = con.prepareStatement(sql);
	           
	    		pst.setInt(1,  newApp.getAppointmentID());
	           	pst.setString(2, newApp.getPatient());
				pst.setString(3, newApp.getDoctor());
				pst.setInt(4, newApp.getDay());
				pst.setInt(5, newApp.getMonth());
				pst.setInt(6, newApp.getYear());
				pst.setInt(7, newApp.getStartHour());
				pst.setInt(8, newApp.getStartMin());
				pst.setInt(9, newApp.getEndHour());
				pst.setInt(10, newApp.getEndMin());
	                
	            int i = pst.executeUpdate();
	            if (i==1)
	                System.out.println("NEW APPOINTMENT ADDED TO DATABASE & OBSERVABLELIST");
	                
	    }  catch (SQLException ex){
	       Logger.getLogger(Appointment.class.getName()).log(Level.SEVERE, null, ex);
	    } 
	 }
	 
	 /* --- deleteAppointmentForC()  
	  * When an appointment for a specific time is to be cancelled by a client
	  */
	 public void deleteAppointmentForC(String patient, int day, int month, int year, int starthour, int startmin, int endhour, int endmin) {
	        
		 for (int i=0; i<appointments.size(); i++) {
			Appointment a = appointments.get(i);
		 	if (patient  == a.getPatient() && day == a.getDay() && month == a.getMonth() && year == a.getYear()
		 			&& starthour == a.getStartHour() && startmin == a.getStartMin() && endhour == a.getEndHour() 
		 			&& endmin == a.getEndMin()) {
		 		System.out.println("REACHED");
		 		appointments.remove(a);
		 	}
		 }
		String sql = "DELETE FROM clinic_tool.appointments WHERE Patient = ? AND Day = ? AND Month = ? AND "
				+ "Year = ? AND StartHour = ? AND StartMinute = ? AND EndHour = ? AND EndMinute = ?";
	    try {
		     pst = con.prepareStatement(sql); 
		     
	         pst.setString(1, patient);
			 pst.setInt(2, day);
			 pst.setInt(3, month);
			 pst.setInt(4, year);
			 pst.setInt(5, starthour);
			 pst.setInt(6, startmin);
			 pst.setInt(7, endhour);
			 pst.setInt(8, endmin);
			 
		     int i = pst.executeUpdate();
	         if (i==1)
	              System.out.println("DATA DELETED SUCCESSFULLY FROM DATABASE & OBSERVABLELIST");
	     } catch (SQLException ex) {
		   Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
		 } 
	 }
	 /* --- deleteAppointmentForD() 
	  * When an appointment for a specific time is to be cancelled or forced to be free by a doctor 
	  */
	 public void deleteAppointmentForD(String doctor, int day, int month, int year, int starthour, int startmin, int endhour, int endmin) {
	        
		 for (int i=0; i<appointments.size(); i++) {
			Appointment a = appointments.get(i);
		 	if (doctor  == a.getDoctor() && day == a.getDay() && month == a.getMonth() && year == a.getYear()
		 			&& starthour == a.getStartHour() && startmin == a.getStartMin() && endhour == a.getEndHour() 
		 			&& endmin == a.getEndMin()){
		 		System.out.println("REACHED");
		 		appointments.remove(a);
		 	}
		 }
		String sql = "DELETE FROM clinic_tool.appointments WHERE Doctor = ? AND Day = ? AND Month = ? AND "
				+ "Year = ? AND StartHour = ? AND StartMinute = ? AND EndHour = ? AND EndMinute = ?";
	    try {
		     pst = con.prepareStatement(sql); 
		     
	         pst.setString(1, doctor);
			 pst.setInt(2, day);
			 pst.setInt(3, month);
			 pst.setInt(4, year);
			 pst.setInt(5, starthour);
			 pst.setInt(6, startmin); 
			 pst.setInt(7, endhour);
			 pst.setInt(8, endmin);

		     int i = pst.executeUpdate();
	         if (i==1)
	              System.out.println("DATA DELETED SUCCESSFULLY FROM DATABASE & OBSERVABLELIST");
	     } catch (SQLException ex) {
		   Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
		 } 
	}
	 
	 /* --- filterbyDoctor() 
      * Returns a newly created ObservableList (of appointments) which corresponds to all appointments 
      * of only one specific doctor.
	 */
	 public ObservableList<Appointment> filterbyDoctor(String doctorName){
		 ObservableList<Appointment> bydoctor = FXCollections.observableArrayList(); 
	     
		 try {
	    	  pst = con.prepareStatement("SELECT AppointmentID, Patient, Doctor, Day, Month, Year, StartHour, StartMinute , EndHour, EndMinute"
	    	  		+ "FROM clinic_tool.appointments WHERE Doctor = '" + doctorName + "'");
	    	  rs = pst.executeQuery();
	    	  while (rs.next()){
	    		  bydoctor.add(new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10)));
	    	  }
	    	  return bydoctor;
	     } catch (SQLException ex) {
	          Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
	     } 
		 return null;
	 }
	 
	 /* --- filterbyMonth()  
      * Returns a newly created ObservableList (of appointments) which corresponds to all appointments 
      * taken / to be taken place on a specific month.
	 */
	 public ObservableList<Appointment> filterbyMonth(int month, int year) 
	 {
		 ObservableList<Appointment> byMonth = FXCollections.observableArrayList(); 
	     
		 try {
	    	  pst = con.prepareStatement("SELECT AppointmentID, Patient, Doctor, Day, Month, Year, StartHour, StartMinute, EndHour, EndMinute "
	    	  		+ "FROM clinic_tool.appointments WHERE Month = " + month + " AND Year = " + year); 
	    	  rs = pst.executeQuery();
	    	  while (rs.next()){
	    		  byMonth.add(new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10)));
	    	  }
	    	  return byMonth;
	     } catch (SQLException ex) {
	          Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
	     } 
		 return null;
	 }
	 
	 /* --- filterbyDay()  
     *  Returns a newly created ObservableList (of appointments) which corresponds to all appointments 
     *  taken / to be taken place on a specific month.
	 */
	 public ObservableList<Appointment> filterbyDay(int day, int month, int year) 
	 {
		 ObservableList<Appointment> byDay = FXCollections.observableArrayList(); 
	     
		 try {
	    	  pst = con.prepareStatement("SELECT AppointmentID, Patient, Doctor, Day, Month, Year, StartHour, StartMinute, EndHour, EndMinute "
	    	  		+ "FROM clinic_tool.appointments WHERE Day = " + day + " AND Month = " + month + " AND Year = " + year);  
	    	  rs = pst.executeQuery();
	    	  while (rs.next()){
	    		  byDay.add(new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10)));
		      }
	    	  return byDay;
	     } catch (SQLException ex) {
	          Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
	     } 
		 return null;
	 }
	 
	 /* --- filterByTimeInterval()  
	  *  Returns a newly created ObservableList (of appointments) which corresponds to all appointments 
	  *  taken / to be taken place within a specific time interval.
	  */
	 
	 /* --- updateAppointments()  
	  * When details for an appointment is modified
	  * TO BE ADDED SOON HAHAHAHHA
	 */
	 
	 //PROBABLY MORE METHODS WILL BE ADDED
}
	    
	  