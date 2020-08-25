package org.trentech.easykits.commands;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.kits.KitUsage;
import org.trentech.easykits.sql.SQLPlayers;
import org.trentech.easykits.utils.Notifications;
import org.trentech.easykits.utils.Utils;

public class CMDList {

	public static void execute(CommandSender sender) {
		if(!sender.hasPermission("easykits.cmd.list")){
			Notifications notify = new Notifications("permission-denied");
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		ConcurrentHashMap<String, Kit> list = KitService.instance().getKits();
		
		sender.sendMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_GREEN + "Kits:\n");
		for(Entry<String, Kit> entry : list.entrySet()) {
			Kit kit = entry.getValue();
			
			if(sender.hasPermission("easykits.kits." + kit.getName()) && !kit.getName().equalsIgnoreCase(Main.getPlugin().getConfig().getString("config.new-player-kit")) || sender instanceof ConsoleCommandSender) {
				String kitMsg = ChatColor.GREEN + "Name: " + ChatColor.YELLOW + kit.getName();
				
				if(sender instanceof Player) {
					Player player = (Player) sender;
			
					double price = kit.getPrice();
					
					if(price > 0) {
						if(!player.hasPermission("easyKits.override.price")){
							kitMsg = kitMsg + ":";
							double balance = Main.getPlugin().getEconomy().getBalance(player);
							if(balance < price) {
								kitMsg = kitMsg + ChatColor.GREEN + " Price: " + ChatColor.DARK_RED + "$" + price;
							}else{
								kitMsg = kitMsg + ChatColor.GREEN + " Price: " + ChatColor.DARK_GREEN + "$" + price;
							}
						}
					}
					
					KitUsage kitUsage = SQLPlayers.get(player, kit.getName());
					
					int limit = kit.getLimit();
					
					if(limit > 0) {
						if(!player.hasPermission("easyKits.override.limit")){
							if(limit - kitUsage.getTimesUsed() <= 0) {
								kitMsg = kitMsg + ChatColor.GREEN + " Limit: " + ChatColor.DARK_RED + "0";
							} else {
								kitMsg = kitMsg + ChatColor.GREEN + " Limit: " + ChatColor.DARK_GREEN + (limit - kitUsage.getTimesUsed());
							}
						}
					}
					
					long cooldown = kit.getCooldown();
					
					if(cooldown > 0) {
						if(!player.hasPermission("easyKits.override.cooldown")){
							Date date = kitUsage.getDate();
							
							long timeSince = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - date.getTime());

							if(cooldown - timeSince > 0) {
								kitMsg = kitMsg + ChatColor.GREEN + " Cooldown: " + ChatColor.DARK_RED + Utils.getReadableTime(cooldown - timeSince);
							} else {
								kitMsg = kitMsg + ChatColor.GREEN + " Cooldown: " + ChatColor.DARK_GREEN + Utils.getReadableTime(cooldown);
							}
						}
					}
				}
				sender.sendMessage(kitMsg);
			}				
		}
	}

}
