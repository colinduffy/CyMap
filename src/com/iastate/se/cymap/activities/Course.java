package com.iastate.se.cymap.activities;

import java.util.Calendar;

/**
 * @author xiangle wang
 * @date 10/06/2012
 */
public class Course {
	private String courseName;
	private String roomNum;
	private String buildingName;
	private String sAMPM;
	private String eAMPM;
	private String day;
	private int sYear, sMonth, sDay;
	private int eYear, eMonth, eDay;
	private int sMin, sHour;
	private int eMin, eHour;
	
	public Course() {
		courseName = "";
		roomNum = "";
		day = "";
		buildingName = "";
		final Calendar c = Calendar.getInstance();
		sYear = eYear = c.get(Calendar.YEAR);
		sMonth = eMonth = c.get(Calendar.MONTH);
		sDay = eDay = c.get(Calendar.DAY_OF_MONTH);
		sMin = eMin = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		
		if(hour > 11){
			sHour = eHour = (hour-12);
			sAMPM = eAMPM = "PM";
		}
		else{
			sHour = eHour = hour;
			sAMPM = eAMPM = "AM";
		}
	}

	public String geteAMPM() {
		return eAMPM;
	}

	public void seteAMPM(String eAMPM) {
		this.eAMPM = eAMPM;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public String getsAMPM() {
		return sAMPM;
	}

	public void setsAMPM(String aMPM) {
		sAMPM = aMPM;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getsYear() {
		return sYear;
	}

	public void setsYear(int sYear) {
		this.sYear = sYear;
	}

	public int getsMonth() {
		return sMonth;
	}

	public void setsMonth(int sMonth) {
		this.sMonth = sMonth;
	}

	public int getsDay() {
		return sDay;
	}

	public void setsDay(int sDay) {
		this.sDay = sDay;
	}

	public int geteYear() {
		return eYear;
	}

	public void seteYear(int eYear) {
		this.eYear = eYear;
	}

	public int geteMonth() {
		return eMonth;
	}

	public void seteMonth(int eMonth) {
		this.eMonth = eMonth;
	}

	public int geteDay() {
		return eDay;
	}

	public void seteDay(int eDay) {
		this.eDay = eDay;
	}

	public int getsMin() {
		return sMin;
	}

	public void setsMin(int sMin) {
		this.sMin = sMin;
	}

	public int getsHour() {
		return sHour;
	}

	public void setsHour(int sHour) {
		this.sHour = sHour;
	}

	public int geteMin() {
		return eMin;
	}

	public void seteMin(int eMin) {
		this.eMin = eMin;
	}

	public int geteHour() {
		return eHour;
	}

	public void seteHour(int eHour) {
		this.eHour = eHour;
	}
	
	public boolean issAM(){
		if(sAMPM.equals("AM")){
			return true;
		}
		return false;
	}
	public boolean iseAM(){
		if(eAMPM.equals("AM")){
			return true;
		}
		return false;
	}
	/**
	 * compare this course start time with another start time
	 * @param hour
	 * @param min
	 * @param ampm
	 * @return
	 */
	public int compareToByTime(int hour, int min, String ampm){
		if(sAMPM.equals("AM") && ampm.equals("PM")){
			return 1;
		}else if(sAMPM.equals("PM") && ampm.equals("AM")){
			return -1;
		}
		if(sHour < hour){
			return 1;
		}else if(sHour > hour){
			return -1;
		}
		if(sMin < min){
			return 1; 
		}else if(sMin > min){
			return -1;
		}
		return 0;
	}
	/**
	 * 	compares end time and start time
	 * @return 
	 */
	public int compareTime() {
		if (eHour < sHour) {
			return -1;
		} else if (eHour > sHour) {
			return 1;
		}
		if (eMin < sMin) {
			return -1;
		} else if (eMin > sMin) {
			return 1;
		}
		return 0;
	}

	/**
	 * compares if two dates
	 * 
	 * @return if same, returns 0. if start is before end date, returns 1 else
	 *         returns -1;
	 */
	public int compareDate() {
		if (eYear < sYear) {
			return -1;
		} else if (eYear > sYear) {
			return 1;
		}
		if (eMonth < sMonth) {
			return -1;
		} else if (eMonth > sMonth) {
			return 1;
		}
		if (eDay < sDay) {
			return -1;
		} else if (eDay > sDay) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		String str = "";
		str += courseName + "|" + roomNum +"|"+ buildingName+"|" + day + "|";
		str += sHour + ":" + sMin +":"+sAMPM + "|" + eHour + ":" + eMin +":"+eAMPM+ "|";
		str += sYear + "/" + sMonth + "/" + sDay + "|";
		str += eYear + "/" + eMonth + "/" + eDay;
		return str;
	}
}
