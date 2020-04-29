package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.Main;
import org.trentech.easykits.sql.SQLUtils;
import org.trentech.easykits.utils.Notifications;

public class CMDReload {
	
	public static void execute(CommandSender sender) {
		if(!sender.hasPermission("easyKits.cmd.reload")){
			Notifications notify = new Notifications("permission-denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		Main.getPlugin().reloadConfig();
		Main.getPlugin().saveConfig();

		SQLUtils.dispose();
		try {
			SQLUtils.connect();
		} catch (Exception e) {
			Main.getPlugin().getLogger().severe(String.format("[%s] - Unable to connect to database!", new Object[] { Main.getPlugin().getDescription().getName() }));
		}

		sender.sendMessage(ChatColor.DARK_GREEN + "EasyKits Reloaded!");
	}
}
