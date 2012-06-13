package com.linuxbox.enkive.statistics.granularity;

import java.util.Calendar;
import java.util.Date;


public enum Granularity {
	//TODO: Implement week
	HOUR(-1, -1, -1), //any hour (hourly controlled by cronTrigger)
	DAY(0, -1, -1),   //first hour of any day
	WEEK(-1, -1, -1), //firts day of any week
	MONTH(-1, 1, -1); //first day of any month
//-1 means any
	private int hour;
	private int dayOfMonth;
	private int month;

	Granularity(int hour, int dayOfMonth, int month) {
		this.hour = hour;
		this.dayOfMonth = dayOfMonth;
		this.month = month;
	}
	
	public boolean isMatch() {
		final Calendar c = Calendar.getInstance();
		
		if (hour >= 0 && hour != c.get(Calendar.HOUR_OF_DAY)) {
			System.out.print("hour fails: ");
			System.out.println(hour + " vs " + c.get(Calendar.HOUR_OF_DAY));
			return false;
		}

		if (dayOfMonth >= 0 && dayOfMonth != c.get(Calendar.DAY_OF_MONTH)) {
			System.out.print("day fails: ");
			System.out.println(dayOfMonth + " vs "
					+ c.get(Calendar.DAY_OF_MONTH));
			return false;
		}

		if (month >= 0 && month != c.get(Calendar.MONTH)) {
			System.out.print("month fails: ");
			System.out.println(month + " vs " + c.get(Calendar.MONTH));
			return false;
		}

		return true;
	}
	public static void main(String[] args) {
		// Date t = new Date(2011, 1, 13, 17, 4, 32);
		System.out.println("CurrDate: " + new Date());
		System.out.println("HOUR " + HOUR.isMatch());
		System.out.println("DAY " + DAY.isMatch());
		System.out.println("MONTH " + MONTH.isMatch());
	}
}
