package com.iastate.se.cymap.activities;

/**
 * @author xiangle wang
 *@date 10/06/2012
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Schedule {
	private ArrayList<Course> list;

	public Schedule() {
		list = new ArrayList<Course>();
	}

	/**
	 * loads schedule
	 */
	public void parseCourse(File file) {
		if (!file.exists()) {
			return;
		}
		String temp = null;
		RandomAccessFile fin = null;
		try {
			fin = new RandomAccessFile(file, "r");
			fin.seek(0);
			fin.seek(0);
			System.out.println("fpiont: " + fin.getFilePointer());

			temp = fin.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (temp != null) {
			System.out.println("Parsing:" + temp);

			Course toAdd = new Course();
			StringTokenizer st1 = new StringTokenizer(temp, "|");
			if (st1.hasMoreTokens()) {
				toAdd.setCourseName(st1.nextToken());
			}
			if (st1.hasMoreTokens()) {
				toAdd.setRoomNum(st1.nextToken());
			}
			if (st1.hasMoreTokens()) {
				toAdd.setBuildingName(st1.nextToken());
			}
			if (st1.hasMoreTokens()) {
				toAdd.setDay(st1.nextToken());
			}
			if (st1.hasMoreTokens()) {
				StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ":");
				if (st2.hasMoreTokens())
					toAdd.setsHour(Integer.parseInt(st2.nextToken()));
				if (st2.hasMoreTokens())
					toAdd.setsMin(Integer.parseInt(st2.nextToken()));
				if (st2.hasMoreTokens())
					toAdd.setsAMPM(st2.nextToken());
			}
			if (st1.hasMoreTokens()) {
				StringTokenizer st3 = new StringTokenizer(st1.nextToken(), ":");
				if (st3.hasMoreTokens())
					toAdd.seteHour(Integer.parseInt(st3.nextToken()));
				if (st3.hasMoreTokens())
					toAdd.seteMin(Integer.parseInt(st3.nextToken()));
				if (st3.hasMoreTokens())
					toAdd.seteAMPM(st3.nextToken());
			}
			if (st1.hasMoreTokens()) {
				StringTokenizer st4 = new StringTokenizer(st1.nextToken(), "/");
				if (st4.hasMoreTokens())
					toAdd.setsYear(Integer.parseInt(st4.nextToken()));
				if (st4.hasMoreTokens())
					toAdd.setsMonth(Integer.parseInt(st4.nextToken()));
				if (st4.hasMoreTokens())
					toAdd.setsDay(Integer.parseInt(st4.nextToken()));
			}
			if (st1.hasMoreTokens()) {
				StringTokenizer st4 = new StringTokenizer(st1.nextToken(), "/");
				if (st4.hasMoreTokens())
					toAdd.seteYear(Integer.parseInt(st4.nextToken()));
				if (st4.hasMoreTokens())
					toAdd.seteMonth(Integer.parseInt(st4.nextToken()));
				if (st4.hasMoreTokens())
					toAdd.seteDay(Integer.parseInt(st4.nextToken()));
			}
			list.add(toAdd);
			try {
				temp = fin.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void sortCourseListByTime() {

		for (int i = 0; i < list.size(); i++) {
			Course temp = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {

				if (list.get(j).compareToByTime(temp.getsHour(),
						temp.getsMin(), temp.getsAMPM()) == 1) {
					temp = list.get(j);
				}
			}
			list.remove(temp);
			list.add(0, temp);
		}

	}

	/**
	 * 
	 * @return the course list
	 */
	public ArrayList<Course> getCourselist() {
		return list;
	}

	/**
	 * removes the course at where the course name is equal to the given
	 * className
	 * 
	 * @param className
	 */
	public void removeCourse(String className) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCourseName().equals(className)) {
				list.remove(i);
			}
		}
	}
}
