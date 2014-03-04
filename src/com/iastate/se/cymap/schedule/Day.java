package com.iastate.se.cymap.schedule;

public enum Day {
	SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(
			7), EMPTY(0);

	private final int id;

	Day(int id) {
		this.id = id;
	}

	int getById(Day day) {
		return day.id;
	}
}
