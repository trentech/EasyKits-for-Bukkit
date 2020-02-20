package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitUser;
import org.trentech.easykits.utils.Notifications;

public class CMDReset {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("EasyKits.cmd.reset")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 4){
			Player player = Main.getPlugin().getServer().getPlayerExact(args[3]);

			if(player == null || !player.isOnline()){
				Notifications notify = new Notifications("No-Player", null, args[2], 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}

			String property = args[1];
			String kitName = args[2];

			Kit kit = new Kit(kitName);
			if(!kit.exists()) {
				Notifications notify = new Notifications("Kit-Not-Exist", kit.getName(), sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			
			KitUser kitUser = new KitUser(player, kit);
			if(property.equalsIgnoreCase("cooldown")) {
				kitUser.setDateObtained("2000-1-1 12:00:00");
				Notifications notify = new Notifications("Set-Cooldown", kitName, player.getName(), 0, "NONE", 0);
				sender.sendMessage(notify.getMessage());
				player.sendMessage(notify.getMessage());
			}else if(property.equalsIgnoreCase("limit")) {
				kitUser.setCurrentLimit(kit.getLimit());
				Notifications notify = new Notifications("Set-Kit-Limit", kitName, player.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				player.sendMessage(notify.getMessage());
			}else{
				sender.sendMessage(ChatColor.YELLOW + "/kit reset [cooldown | limit] <kitname> <player>");
			}
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit reset [cooldown | limit] <kitname> <player>");
		}
	}

}
