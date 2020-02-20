package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitUser;
import org.trentech.easykits.utils.Notifications;

public class CMDKit {

	public static void execute(CommandSender sender, String[] args) {
		if(args.length == 1) {
			if(!(sender instanceof Player)) {
				Notifications notify = new Notifications("Not-Player", null, sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}
			
			String kitName = args[0];
			Kit kit = new Kit(kitName);
			if(!kit.exists()) {
				Notifications notify = new Notifications("Kit-Not-Exist", kit.getName(), sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}

			Player player = (Player) sender;
			KitUser kitUser = new KitUser(player, kit);
			try {
				kitUser.applyKit();
			} catch (Exception e) {
				Main.getPlugin().getLogger().severe(e.getMessage());
			}

		}else{
			sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Command List:\n");
			sender.sendMessage(ChatColor.YELLOW + "/kit -or- /k");
			sender.sendMessage(ChatColor.YELLOW + "/kit [kitname]");	
			if(sender.hasPermission("EasyKits.cmd.help")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit help [command]");
			}
			if(sender.hasPermission("EasyKits.cmd.reload")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit reload");
			}
			if(sender.hasPermission("EasyKits.cmd.create")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit create <kitname>");
			}
			if(sender.hasPermission("EasyKits.cmd.remove")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit remove <kitname>");
			}
			if(sender.hasPermission("EasyKits.cmd.limit")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit limit <kitname> <cooldown>");
			}
			if(sender.hasPermission("EasyKits.cmd.cooldown")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit cooldown <kitname> <cooldown>");
			}
			if(sender.hasPermission("EasyKits.cmd.price")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit price <kitname> <price>");
			}
			if(sender.hasPermission("EasyKits.cmd.reset")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit reset [cooldown | limit] <kitname> <player>");
			}
			if(sender.hasPermission("EasyKits.cmd.give")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit give <kitname> <player>");
			}
			if(sender.hasPermission("EasyKits.cmd.book")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit book");
			}
			if(sender.hasPermission("EasyKits.cmd.view")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit view <kitname>");
			}
			if(sender.hasPermission("EasyKits.cmd.list")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit list");
			}
		}	
	}

}
