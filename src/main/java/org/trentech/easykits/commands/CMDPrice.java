package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDPrice {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("EasyKits.cmd.price")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 3){
			Optional<Kit> optional = KitService.instance().getKit(args[1]);
			
			if(!optional.isPresent()) {
				Notifications notify = new Notifications("Kit-Not-Exist", args[1], sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			Kit kit = optional.get();

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
