package model;

import java.util.ArrayList;

public class Model {

	private static ArrayList<CalendarItem> items = new ArrayList<CalendarItem>();
	public static String itemSelected;
	public static int row, col;

	/*	public static void addEvent(Event e){
            items.add(e);
        }

        public static void addTask(Task t){
            items.add(t);
        }
    */
	public static void removeItem(CalendarItem i) {
		items.remove(i);
	}

	public static ArrayList<CalendarItem> getItems(){
		return items;
	}

	public static int getItemsSize() {
		return items.size();
	}

	public static int getIndex(CalendarItem i) {
		return items.indexOf(i);
	}

	public void printEvents() {
		for (int i = 0; i<items.size(); i++){
			System.out.println(i + " " + items.get(i).getTitle() + " " + items.get(i).getColor());
			System.out.println("ON: "+ items.get(i).getMonth()+ "/"+  items.get(i).getDay()+ "/" +items.get(i).getYear());
			System.out.println("AT: " + items.get(i).getStartHour() +":"+ items.get(i).getStartMinute()+ " TO " + items.get(i).getEndHour() + ":"+ items.get(i).getEndMinute() );
		}
	}

	public static void setSelected(String s) {
		itemSelected = s;
	}

	public static String getSelected() {
		return itemSelected;
	}

	public static void setRowCol(int r, int c) {
		row = r;
		col = c;
	}

	public static void setRow(int r) {
		row = r;
	}

	public static void setCol(int c) {
		col = c;
	}

	public static int getRow() {
		return row;
	}

	public static int getCol() {
		return col;
	}
}
