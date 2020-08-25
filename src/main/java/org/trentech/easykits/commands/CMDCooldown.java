package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;
import org.trentech.easykits.utils.Utils;

public class CMDCooldown {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("easykits.cmd.cooldown")){
			Notifications notify = new Notifications("permission-denied");
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length >= 3){
			KitService kitService = KitService.instance();
			
			Optional<Kit> optional = kitService.getKit(args[1]);
			
			if(!optional.isPresent()) {
				Notifications notify = new Notifications("kit-not-exist", args[1]);
				sender.sendMessage(notify.getMessage());
				return;
			}
			Kit kit = optional.get();

			if(!isValid(args[2])) {
				Notifications notify = new Notifications("invalid-argument");
				sender.sendMessage(notify.getMessage());
				sender.sendMessage(ChatColor.YELLOW + "/kit cooldown <kitname> <cooldown>");
				return;
			}

			kit.setCooldown(Utils.getTimeInSeconds(args[2]));
			kitService.save(kit);
			
			Notifications notify = new Notifications("set-cooldown", kit.getName(), sender.getName(), args[2]);
			sender.sendMessage(notify.getMessage());
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit cooldown <kitname> <cooldown>");
		}
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
