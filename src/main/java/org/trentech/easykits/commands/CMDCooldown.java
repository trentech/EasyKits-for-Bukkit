package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.utils.Notifications;

public class CMDCooldown {

	public static void execute(CommandSender sender, String[] args) {

		if(!sender.hasPermission("EasyKits.cmd.cooldown")){
			Notifications notify = new Notifications("Permission-Denied", null, null, 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length >= 3){
	        StringBuilder str = new StringBuilder(args[2]);
	        for (String arg : args){
	        	if(!(arg.equals(args[0]) || arg.equals(args[1]) || arg.equals(args[2]))){
		        	str.append(" ").append(arg);
	        	}
	        }
			Kit kit = new Kit(args[1]);
			if(!kit.exists()) {
				Notifications notify = new Notifications("Kit-Not-Exist", args[1], sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}

			String[] newArgs = str.toString().split(" ");

			if(!isValid(newArgs)) {
				Notifications notify = new Notifications("Invalid-Argument", args[1], sender.getName(), 0, str.toString(), 0);
				sender.sendMessage(notify.getMessage());
				sender.sendMessage(ChatColor.YELLOW + "/kit cooldown <kitname> <cooldown>");
				return;
			}

			kit.setCooldown(str.toString());
			Notifications notify = new Notifications("Set-Cooldown", kit.getName(), sender.getName(), 0, str.toString(), 0);
			sender.sendMessage(notify.getMessage());
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit cooldown <kitname> <cooldown>");
		}
	}	
	
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
	
	private static boolean isValid(String time){
		if(time.equalsIgnoreCase("0")) {
			return true;
		}
		
		int loop = 0;
		
		for(String arg : time.split(",")) {
			if(arg.matches("(\\d+)[w]$") && loop == 0) {
				
			}else if(arg.matches("(\\d+)[d]$") && (loop == 0 || loop == 1)) {
				
			}else if(arg.matches("(\\d+)[h]$") && (loop == 0 || loop == 1 || loop == 2)) {
				
			}else if(arg.matches("(\\d+)[m]$") && (loop == 0 || loop == 1 || loop == 2 || loop == 3)) {
				
			}else if(arg.matches("(\\d+)[s]$") && (loop == 0 || loop == 1 || loop == 2 || loop == 3 || loop == 4)) {
				
			}else{
				return false;
			}
			loop++;
		}
		return true;
	}

}
