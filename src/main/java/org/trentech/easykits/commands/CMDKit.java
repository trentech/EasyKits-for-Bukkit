package org.trentech.easykits.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.utils.Notifications;

public class CMDKit implements TabCompleter {

	public static void execute(CommandSender sender, String[] args) {
		if(args.length == 1) {
			if(!(sender instanceof Player)) {
				Notifications notify = new Notifications("not-player");
				sender.sendMessage(notify.getMessage());
				return;
			}
			Player player = (Player) sender;
			
			Optional<Kit> optional = KitService.instance().getKit(args[0]);
			
			if(!optional.isPresent()) {
				Notifications notify = new Notifications("kit-not-exist", args[0]);
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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list = new ArrayList<>();
		
		if(!command.getName().equalsIgnoreCase("kit")) {
			return new ArrayList<>();
		}

		if((args.length == 1 && args[0].equalsIgnoreCase("")) || args.length == 0) {
			for(Entry<String, Kit> entry : SQLKits.all().entrySet()) {
				list.add(entry.getKey());
			}
			if(sender.hasPermission("easykits.cmd.help")) {
				list.add("help");
			}
			if(sender.hasPermission("easykits.cmd.reload")) {
				list.add("reload");
			}
			if(sender.hasPermission("easykits.cmd.create")) {
				list.add("create");
			}
			if(sender.hasPermission("easyKits.cmd.remove")) {
				list.add("remove");
			}
			if(sender.hasPermission("easyKits.cmd.limit")) {
				list.add("limit");
			}
			if(sender.hasPermission("easykits.cmd.cooldown")) {
				list.add("cooldown");
			}
			if(sender.hasPermission("easykits.cmd.price")) {
				list.add("price");
			}
			if(sender.hasPermission("easyKits.cmd.reset")) {
				list.add("reset");
			}
			if(sender.hasPermission("easyKits.cmd.give")) {
				list.add("give");
			}
			if(sender.hasPermission("easykits.cmd.book")) {
				list.add("book");
			}
			if(sender.hasPermission("easykits.cmd.view")) {
				list.add("view");
			}
			if(sender.hasPermission("easykits.cmd.list")) {
				list.add("list");
			}
		} else if(args.length == 1) {
			for(Entry<String, Kit> entry : SQLKits.all().entrySet()) {
				if(entry.getKey().startsWith(args[0])) {
					list.add(entry.getKey());
				}
			}
			if("help".startsWith(args[0])) {
				list.add("help");
			}
			if("reload".startsWith(args[0])) {
				list.add("reload");
			}
			if("create".startsWith(args[0])) {
				list.add("create");
			}
			if("remove".startsWith(args[0])) {
				list.add("remove");
			}
			if("limit".startsWith(args[0])) {
				list.add("limit");
			}
			if("cooldown".startsWith(args[0])) {
				list.add("cooldown");
			}
			if("price".startsWith(args[0])) {
				list.add("price");
			}
			if("reset".startsWith(args[0])) {
				list.add("reset");
			}
			if("give".startsWith(args[0])) {
				list.add("give");
			}
			if("book".startsWith(args[0])) {
				list.add("book");
			}
			if("view".startsWith(args[0])) {
				list.add("view");
			}
			if("list".startsWith(args[0])) {
				list.add("list");
			}
		}
		
		return list;
	}
}
