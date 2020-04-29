package org.trentech.easykits.commands;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDList {

	public static void execute(CommandSender sender) {
		if(!sender.hasPermission("easyKits.cmd.list")){
			Notifications notify = new Notifications("permission-denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		ConcurrentHashMap<String, Kit> list = KitService.instance().getKits();
		
		sender.sendMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_GREEN + "Kits:\n");
		for(Entry<String, Kit> entry : list.entrySet()) {
			Kit kit = entry.getValue();
			
			if(sender.hasPermission("easyKits.kits." + kit.getName()) && !kit.getName().equalsIgnoreCase(Main.getPlugin().getConfig().getString("config.new-player-kit")) || sender instanceof ConsoleCommandSender) {
				String kitMsg = ChatColor.YELLOW + "- " + kit.getName();
				if(sender instanceof Player) {
					Player player = (Player) sender;

					double price = kit.getPrice();
					if(price > 0) {
						if(!player.hasPermission("easyKits.bypass.price")){
							kitMsg = kitMsg + ":";
							double balance = Main.getPlugin().getEconomy().getBalance(player);
							if(balance < price) {
								kitMsg = kitMsg + ChatColor.DARK_RED + " $" + price;
							}else{
								kitMsg = kitMsg + ChatColor.DARK_GREEN + " $" + price;
							}
						}
					}
				}
				sender.sendMessage(kitMsg);
			}				
		}
	}

}
