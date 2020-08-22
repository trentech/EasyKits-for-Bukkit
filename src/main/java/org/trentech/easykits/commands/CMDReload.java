package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.Main;
import org.trentech.easykits.utils.Notifications;

public class CMDReload {
	
	public static void execute(CommandSender sender) {
		if(!sender.hasPermission("easyKits.cmd.reload")){
			Notifications notify = new Notifications("permission-denied");
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		Main.getPlugin().reloadConfig();
		Main.getPlugin().saveConfig();

		sender.sendMessage(ChatColor.DARK_GREEN + "EasyKits Reloaded!");
	}
}
