package org.trentech.easykits.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.sql.SQLPlayers;
import org.trentech.easykits.utils.Notifications;

public class CMDLimit {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("EasyKits.cmd.limit")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 3){
			String kitName = args[1];
			Kit kit = new Kit(kitName);
			if(!kit.exists()) {
				Notifications notify = new Notifications("Kit-Not-Exist", kitName, sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}

			String limit = args[2];
			try{
				Integer.parseInt(limit);
			}catch(NumberFormatException e) {
				Notifications notify = new Notifications("Invalid-Number", null, sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			
			kit.setLimit(Integer.parseInt(limit));
			Notifications notify = new Notifications("Set-Kit-Limit", kit.getName(), sender.getName(), 0, null, Integer.parseInt(limit));
			sender.sendMessage(notify.getMessage());
			
			List<String> list = SQLPlayers.getPlayerList();
			for(String playerUUID : list){
				if(SQLPlayers.getLimit(playerUUID, kit.getName()) == 0){
					SQLPlayers.setLimit(playerUUID, kit.getName(), Integer.parseInt(limit));
				}
			}
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit limit <kitname> <limit>");
		}
	}

}