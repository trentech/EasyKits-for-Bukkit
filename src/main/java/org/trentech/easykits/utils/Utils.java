package org.trentech.easykits.utils;

public class Utils {

	public static long getTimeInSeconds(String time){
		if(time.equalsIgnoreCase("0")) {
			return 0;
		}
		
		long seconds = 0;
		
		for(String arg : time.split(",")){
			if(arg.matches("(\\d+)[s]$")){
				seconds = Integer.parseInt(arg.replace("s", "")) + seconds;
			}else if(arg.matches("(\\d+)[m]$")){
				seconds = (Integer.parseInt(arg.replace("m", "")) * 60) + seconds;
			}else if(arg.matches("(\\d+)[h]$")){
				seconds = (Integer.parseInt(arg.replace("h", "")) * 3600) + seconds;
			}else if(arg.matches("(\\d+)[d]$")){
				seconds = (Integer.parseInt(arg.replace("d", "")) * 86400) + seconds;
			}else if(arg.matches("(\\d+)[w]$")){
				seconds = (Integer.parseInt(arg.replace("w", "")) * 604800) + seconds;
			}
		}
		return seconds;
	}

	public static String getReadableTime(long timeInSec) {
		long weeks = timeInSec / 604800;
		long wRemainder = timeInSec % 604800;
		long days = wRemainder / 86400;
		long dRemainder = wRemainder % 86400;
		long hours = dRemainder / 3600;
		long hRemainder = dRemainder % 3600;
		long minutes = hRemainder / 60;
		long seconds = hRemainder % 60;
		
		StringBuilder stringBuilder = new StringBuilder();

		if(weeks > 0){
			stringBuilder.append(weeks);

			if(weeks == 1){
				stringBuilder.append(" Week ");
			} else {
				stringBuilder.append(" Weeks ");
			}
		}
		if(days > 0){
			stringBuilder.append(days);

			if(days == 1){
				stringBuilder.append(" Day ");
			} else {
				stringBuilder.append(" Days ");
			}
		}		
		if((hours > 0)){
			stringBuilder.append(hours);

			if(hours == 1){
				stringBuilder.append(" Hour ");
			} else {
				stringBuilder.append(" Hours ");
			}	
		}
		if(minutes > 0){
			stringBuilder.append(minutes);

			if(minutes == 1){
				stringBuilder.append(" Minute ");
			} else {
				stringBuilder.append(" Minutes ");
			}		
		}
		if(seconds > 0){
			stringBuilder.append(seconds);

			if(seconds == 1){
				stringBuilder.append(" Second ");
			} else {
				stringBuilder.append(" Seconds ");
			}		
		}
		return stringBuilder.toString();
	}
}
