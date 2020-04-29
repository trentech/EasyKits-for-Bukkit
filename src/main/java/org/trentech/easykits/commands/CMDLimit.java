package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDLimit {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("easykits.cmd.limit")){
			Notifications notify = new Notifications("permission-denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 3){
			Optional<Kit> optional = KitService.instance().getKit(args[1]);
			
			if(!optional.isPresent()) {
				Notifications notify = new Notifications("kit-not-exist", args[1], sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			Kit kit = optional.get();

			String limit = args[2];
			try{
				Integer.parseInt(limit);
			}catch(NumberFormatException e) {
				Notifications notify = new Notifications("invalid-number", null, sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			
			kit.setLimit(Integer.parseInt(limit));
			Notifications notify = new Notifications("set-kit-limit", kit.getName(), sender.getName(), 0, null, Integer.parseInt(limit));
			sender.sendMessage(notify.getMessage());
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit limit <kitname> <limit>");
		}
	}

}