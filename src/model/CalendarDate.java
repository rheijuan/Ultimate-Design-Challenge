package model;
import java.util.GregorianCalendar;

public class CalendarDate {

	    private static int yearBound, monthBound, dayBound, yearToday, monthToday, dayToday;

	    public CalendarDate(){
			GregorianCalendar cal = new GregorianCalendar();
			dayBound = cal.get(GregorianCalendar.DAY_OF_MONTH);
			monthBound = cal.get(GregorianCalendar.MONTH);
			yearBound = cal.get(GregorianCalendar.YEAR);
			dayToday = dayBound;
			monthToday = monthBound; 
			yearToday = yearBound;	
	    }
	    
		public void setDayBound(int dayBound) {
			this.dayBound = dayBound;
		}

		public void setMonthBound(int monthBound) {
			this.monthBound = monthBound;
		}

		public void setYearBound(int yearBound) {
			this.yearBound = yearBound;
		}

		public void setMonthToday(int monthToday) {
			this.monthToday = monthToday;
		}

		public void setYearToday(int yearToday) {
			this.yearToday = yearToday;
		}

		public int getYearBound() {
			return yearBound;
		}

		public int getMonthBound() {
			return monthBound;
		}

		public int getDayBound() {
			return dayBound;
		}

		public int getDayToday() {
			return dayToday;
		}
		
		public int getYearToday() {
			return yearToday;
		}
		
		public int getMonthToday() {
			return monthToday;
		}
		
		public int getMaxDay() {
			GregorianCalendar cal = new GregorianCalendar(yearBound, monthBound, 1);
			return cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		}
}
