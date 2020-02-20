package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitUser;
import org.trentech.easykits.utils.Notifications;

public class CMDGive {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("EasyKits.cmd.give")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 3){
			Player reciever = Main.getPlugin().getServer().getPlayerExact(args[2]);

			if(reciever == null || !reciever.isOnline()) {
				Notifications notify = new Notifications("No-Player", null, args[2], 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}

			String kitName = args[1];
			Kit kit = new Kit(kitName);
			if(!kit.exists()) {
				Notifications notify = new Notifications("Kit-Not-Exist", kit.getName(), args[2], 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			
			KitUser kitUser = new KitUser(reciever, kit);
			try {
				kitUser.applyKit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Notifications notify = new Notifications("Kit-Received", kit.getName(), sender.getName(), kit.getPrice(), null, 0);
			reciever.sendMessage(notify.getMessage());
			notify = new Notifications("Kit-Sent", kit.getName(), reciever.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit give <kitname> <player>");
		}
	}

}
