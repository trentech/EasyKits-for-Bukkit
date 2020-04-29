package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDKit {

	public static void execute(CommandSender sender, String[] args) {
		if(args.length == 1) {
			if(!(sender instanceof Player)) {
				Notifications notify = new Notifications("not-player", null, sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			Player player = (Player) sender;
			
			Optional<Kit> optional = KitService.instance().getKit(args[0]);
			
			if(!optional.isPresent()) {
				Notifications notify = new Notifications("kit-not-exist", args[0], sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			Kit kit = optional.get();

			KitService.instance().setKit(player, kit, true);
		}else{
			sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Command List:\n");
			sender.sendMessage(ChatColor.YELLOW + "/kit -or- /k");
			sender.sendMessage(ChatColor.YELLOW + "/kit [kitname]");	
			if(sender.hasPermission("easykits.cmd.help")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit help [command]");
			}
			if(sender.hasPermission("easykits.cmd.reload")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit reload");
			}
			if(sender.hasPermission("easykits.cmd.create")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit create <kitname>");
			}
			if(sender.hasPermission("easyKits.cmd.remove")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit remove <kitname>");
			}
			if(sender.hasPermission("easyKits.cmd.limit")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit limit <kitname> <cooldown>");
			}
			if(sender.hasPermission("easykits.cmd.cooldown")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit cooldown <kitname> <cooldown>");
			}
			if(sender.hasPermission("easykits.cmd.price")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit price <kitname> <price>");
			}
			if(sender.hasPermission("easyKits.cmd.reset")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit reset [cooldown | limit] <kitname> <player>");
			}
			if(sender.hasPermission("easyKits.cmd.give")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit give <kitname> <player>");
			}
			if(sender.hasPermission("easykits.cmd.book")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit book");
			}
			if(sender.hasPermission("easykits.cmd.view")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit view <kitname>");
			}
			if(sender.hasPermission("easykits.cmd.list")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit list");
			}
		}	
	}

}
