package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.utils.Notifications;

public class CMDPrice {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("EasyKits.cmd.price")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 3){
			String kitName = args[1];
			Kit kit = new Kit(kitName);
			if(!kit.exists()) {
				Notifications notify = new Notifications("Kit-Not-Exist", kit.getName(), sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;	
			}

			String price = args[2];
			try{
				Double.parseDouble(price);
			}catch(NumberFormatException e) {
				Notifications notify = new Notifications("Invalid-Number", null, sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			
			kit.setPrice(Double.parseDouble(price));
			Notifications notify = new Notifications("Set-Price", kit.getName(), sender.getName(), Double.parseDouble(price), null, 0);
			sender.sendMessage(notify.getMessage());
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit price <kitname> <price>");
		}
	}

}
