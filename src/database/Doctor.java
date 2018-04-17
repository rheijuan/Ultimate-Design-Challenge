package database;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class Doctor extends User{

    //private ArrayList<Appointment> slots;
    private DBController db;

    public Doctor(String username, String password, String name) {
        super(username,password,name,"doctor");

        db = new DBController();
    }

	/*public ArrayList<Appointment> getSlots() {
		return slots;
	}

	public void setSlots(ArrayList<Appointment> slots) {
		this.slots = slots;
	}*/

    public void addSlot (LocalDate ld, int starthour, int startmin, int endhour, int endmin, boolean mon, boolean tue, boolean wed,
                         boolean thu, boolean fri, boolean sat, boolean mth){
        //Appointment apt = new Appointment(0, null, name, ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear(), starthour, startmin, endhour, endmin,0);
        System.out.println("debug");
        //slots.add(apt);


        if(checkOverlap(starthour,startmin,endhour,endmin) == null)
            db.createAppointment(null, this.getName(), ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear(), starthour, startmin, endhour, endmin,0);
        else System.out.println("OVERLAP");
    }

    public Appointment checkOverlap(int starthour, int startmin, int endhour, int endmin) {
        ObservableList<Appointment> slots = db.filterbyDoctor(this.getName());

        int start = starthour*100 + startmin;
        int end = endhour*100 + endmin;

        for(Appointment apt: slots) {

            int aptS = apt.getStartHour()*100 + apt.getStartMin();
            int aptE = apt.getEndHour()*100 + apt.getEndHour();

            if(start <= aptE && start > aptS || end < aptE && end >= aptS ||
                    aptS <= end && aptS > start || aptE < end && aptE >= start) {
                return apt;
            }
        }

        return null;
    }
}
