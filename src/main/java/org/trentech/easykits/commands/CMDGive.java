package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.events.KitEvent;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDGive {

	public static void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("easykits.cmd.give")){
			Notifications notify = new Notifications("permission-denied");
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		if(args.length == 3){
			Player reciever = Main.getPlugin().getServer().getPlayerExact(args[2]);

			if(reciever == null || !reciever.isOnline()) {
				Notifications notify = new Notifications("no-player");
				sender.sendMessage(notify.getMessage());
				return;
			}

			Optional<Kit> optional = KitService.instance().getKit(args[1]);
			
			if(!optional.isPresent()) {
				Notifications notify = new Notifications("kit-not-exist", args[1]);
				sender.sendMessage(notify.getMessage());
				return;
			}
			Kit kit = optional.get();

			KitEvent.Give event = new KitEvent.Give(reciever, kit);

			Bukkit.getServer().getPluginManager().callEvent(event);

			if(!event.isCancelled()){
				if(!KitService.instance().setKit(reciever, kit, false)) {
					sender.sendMessage(ChatColor.RED + "Could not give kit. Possibly need more inventory space.");
					return;
				}
			}

			Notifications notify = new Notifications("kit-received", kit.getName(), sender.getName(), kit.getPrice());
			reciever.sendMessage(notify.getMessage());
			notify = new Notifications("Kit-Sent", kit.getName(), reciever.getName());
			sender.sendMessage(notify.getMessage());
		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit give <kitname> <player>");
		}
	}

}
