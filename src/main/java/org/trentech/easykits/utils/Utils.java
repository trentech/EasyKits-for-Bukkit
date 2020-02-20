package org.trentech.easykits.utils;

public class Utils {

	public static int getTimeInSeconds(String time){
		String[] times = time.split(" ");
		int seconds = 0;
		for(String t : times){
			if(t.matches("(\\d+)[s]$")){
				seconds = Integer.parseInt(t.replace("s", "")) + seconds;
			}else if(t.matches("(\\d+)[m]$")){
				seconds = (Integer.parseInt(t.replace("m", "")) * 60) + seconds;
			}else if(t.matches("(\\d+)[h]$")){
				seconds = (Integer.parseInt(t.replace("h", "")) * 3600) + seconds;
			}else if(t.matches("(\\d+)[d]$")){
				seconds = (Integer.parseInt(t.replace("d", "")) * 86400) + seconds;
			}else if(t.matches("(\\d+)[w]$")){
				seconds = (Integer.parseInt(t.replace("w", "")) * 604800) + seconds;
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
	
//	public static UUID getUUID(String playerName){
//		UUID uuid = null;
//		HashMap<UUID, String> players = Main.getPlayers();
//		Set<Map.Entry<UUID, String>> keys = players.entrySet();
//		for(Entry<UUID, String> key : keys){
//			if(key.getValue().equalsIgnoreCase(playerName)){
//				uuid = key.getKey();
//				break;
//			}
//		}
//		return uuid;
//	}
 
}
