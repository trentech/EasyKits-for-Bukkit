package org.trentech.easykits.kits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KitUsage {

	protected String kitName;
	private int timesUsed;
	private Date date;

	public KitUsage(String kitName, int timesUsed, Date date) {
		this.kitName = kitName;
		this.timesUsed = timesUsed;
		this.date = date;
	}
	
	public KitUsage(String kitName) {
		this.kitName = kitName;
		this.timesUsed = 0;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2000-01-01 12:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public int getTimesUsed() {
		return timesUsed;
	}

	public void setTimesUsed(int timesUsed) {
		this.timesUsed = timesUsed;
	}
}
