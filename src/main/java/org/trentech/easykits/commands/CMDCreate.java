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

public class CMDCreate {

	public static void execute(CommandSender sender, String[] args) {		
		if(!(sender instanceof Player)) {
			Notifications notify = new Notifications("Not-Player", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		Player player = (Player) sender;	
		if(!player.hasPermission("EasyKits.cmd.create")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			player.sendMessage(notify.getMessage());
			return;
		}
		
		if (args.length == 2){
			String kitName = args[1];
			
			Optional<Kit> optional = KitService.instance().getKit(args[1]);
			
			if(optional.isPresent()) {
				Notifications notify = new Notifications("Kit-Exist", kitName, null, 0, null, 0);
				player.sendMessage(notify.getMessage());
				return;
			}

			Kit kit = new Kit(kitName, player.getInventory().getContents(), player.getInventory().getArmorContents());
			
			KitEvent.Create event = new KitEvent.Create(player, kit);

			Bukkit.getServer().getPluginManager().callEvent(event);

			if(!event.isCancelled()){
				KitService.instance().save(kit);
				Notifications notify = new Notifications("Kit-Created", kitName, sender.getName(), 0, null, 0);
				player.sendMessage(notify.getMessage());
			}
		}else{
			player.sendMessage(ChatColor.YELLOW + "/kit create <kitname>");
		}
	}

}
