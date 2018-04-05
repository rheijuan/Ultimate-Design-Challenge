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
    private static ObservableList<User> users = FXCollections.observableArrayList(); 

	/* --- getAppointments() 
	 *  Returns the ObservableList of all appointments 
	 *  (Call this after calling loadAppointments get the updated ObservableList)
	 */
    public static ObservableList<Appointment> getAppointments() { 
        return appointments;
    } 
    /* --- getUsers() 
	 *  Returns the ObservableList of all patients ( with their login details ) 
	 */
    public static ObservableList<User> getUsers() { 
        return users;
    }
    
    /* --- getPatients()
     *  Returns the the ObservableList of all patients in the database that is registered to the system.
     */
    public static ObservableList<User> getPatients() {
		 ObservableList<User> patientsOnly = FXCollections.observableArrayList();

        for (User user : users) {
            if (user.getRole().equals("Patient")) {
                patientsOnly.add(user);
            }
        }
		 return patientsOnly;
    }

    /* --- getDoctors()
     *  Returns an ObservableList of all doctors in the database that is registered to the system. 
     */
    public static ObservableList<User> getDoctors() {
	   	 ObservableList<User> doctorsOnly = FXCollections.observableArrayList();

        for (User user : users) {
            if (user.getRole().equals("Doctor")) {
                doctorsOnly.add(user);
            }
        }
		 return doctorsOnly;
    }
    
	/* --- loadAppointments() 
	 * Loads or refreshes all appointments from the database and load to an ObservableList appointments.
	 */
	public void loadAppointments() {
		 try {
	            con = ConnectDB.getConnection();
	        } catch (ClassNotFoundException | SQLException ex) {
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

	/* --- loadUsers() 
	 * Loads or refreshes all users logged from the database and load to an ObservableList users.
	 */
	public void loadUsers() {
		 try {
	            con = ConnectDB.getConnection();
	        } catch (ClassNotFoundException | SQLException ex) {
	            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
	        }

		try{
	        pst = con.prepareStatement("SELECT * FROM clinic_tool.users");
	        rs = pst.executeQuery();
	        while (rs.next())
	        	users.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
	        				       //(FORMAT: username, password, name, role (patient, secretary, doc)
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

	 /* --- createNewUser
	  * Creates a new user data 
	  */
	 public void createNewUser(String username, String password, String name, String role) {
			 
		User newUser = new User(username, password, name, role);
	    users.add(newUser);
		 
	    String sql = "INSERT INTO clinic_tool.users VALUES (?, ?, ?, ?)";
	    try{    
	    		pst = con.prepareStatement(sql);
		           
	    		pst.setString(1, newUser.getUsername());
	           	pst.setString(2, newUser.getPassword());
				pst.setString(3, newUser.getName());
				pst.setString(4, newUser.getRole());
				
	            int i = pst.executeUpdate();
	            if (i==1)
	                System.out.println("NEW USER ADDED TO DATABASE & OBSERVABLELIST");
		                
	    }  catch (SQLException ex){
		       Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
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

	 /* --- updateAppointmentDate()  
	  * When details for an appointment date was modified (parameters contains the name of the patient, new date, 
	  * same time)
	 */
	  public void updateAppointmentDate(String patientname, int day, int month, int year) {
		    int refID = 0;

          for (Appointment appointment : appointments) {
              if (patientname.equals(appointment.getPatient()))
                  refID = appointment.getAppointmentID();
          }
	    
	        String sql = "UPDATE clinic_tool.appointments SET Day = ?, Month = ?, Year = ? WHERE AppointmentID = ?";
	        try{    
	        	pst = con.prepareStatement(sql);
	                	        
	            pst.setInt(1, day);
				pst.setInt(2, month);
				pst.setInt(3, year);
				pst.setInt(4, refID);
	                
	            int i = pst.executeUpdate();
	            if (i==1)
	               System.out.println("DATA FOR APPOINTMENT DATE UPDATED!!!");
	                
	        } catch (SQLException ex){
	          Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
	        } 
	  }
	  
	  /* --- updateAppointmentTime()  
	   * When details for an appointment time was modified (parameters contains the name of the patient, new time)
	   */
	  public void updateAppointmentTime(String patientname, int starthr, int startmin, int endhr, int endmin) {
		    int refID = 0;

          for (Appointment appointment : appointments) {
              if (patientname.equals(appointment.getPatient()))
                  refID = appointment.getAppointmentID();
          }
		    
	        String sql = "UPDATE clinic_tool.appointments SET StartHour = ?, StartMinute = ?, "
	        		+ "EndHour = ?, EndMinute = ? WHERE Patient = ?";
	        try{    
	        	pst = con.prepareStatement(sql);
	                	        
	            pst.setInt(1, starthr);
				pst.setInt(2, startmin);
				pst.setInt(3, endhr);
				pst.setInt(4, endmin);
				pst.setInt(5, refID);
	                
	            int i = pst.executeUpdate();
	            if (i==1)
	               System.out.println("DATA FOR APPOINTMENT TIME UPDATED!!!");
	                
	        } catch (SQLException ex){
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
	 
}
	    
	  