package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.events.KitEvent;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDRemove {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("easykits.cmd.remove")){
			Notifications notify = new Notifications("permission-denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 2){
			Optional<Kit> optional = KitService.instance().getKit(args[1]);
			
			if(!optional.isPresent()) {
				Notifications notify = new Notifications("kit-not-exist", args[1], sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			Kit kit = optional.get();
			
			KitEvent.Delete event = new KitEvent.Delete((Player) sender, kit);

			Bukkit.getServer().getPluginManager().callEvent(event);

			if(!event.isCancelled()){
				KitService.instance().delete(kit.getName());
				Notifications notify = new Notifications("kit-deleted", kit.getName(), sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
			}
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit remove <kitname>");
		}
	}

}
