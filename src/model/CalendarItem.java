package model;

public class CalendarItem {

	protected int year;
	protected int month;
	protected int day;

	protected int starthour;
	protected int startminute;
	protected int endhour;
	protected int endminute;

	protected String title;
	protected String color;

	protected boolean doneornot;

	public void setDate(int month, int day, int year){
		this.month = month;
		this.day = day;
		this.year = year;
	}

	public void setMonth(int m){
		month = m;
	}

	public void setDay(int d){
		day = d;
	}

	public void setYear(int y){
		year = y;
	}

	public void setStartHour(int h){
		starthour = h;
	}

	public void setStartMinute(int m){
		startminute = m;
	}

	public void setTitle(String t){
		title = t;
	}

	public void setIsDone(boolean d) {
		doneornot = d;
	}

	public int getMonth(){
		return month;
	}

	public int getDay(){
		return day;
	}

	public int getYear(){
		return year;
	}

	public int getStartHour() {
		return starthour;
	}

	public int getStartMinute() {
		return startminute;
	}

	public String getTitle(){
		return title;
	}

	public String getColor(){
		return color;
	}

	public int getEndHour() {
		return endhour;
	}

	public int getEndMinute() {
		return endminute;
	}

	public boolean isDone() {
		return doneornot;
	}
}
