package org.trentech.easykits.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitUser;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.utils.Notifications;

public class CMDList {

	public static void execute(CommandSender sender) {
		if(!sender.hasPermission("EasyKits.cmd.list")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		
		List<Kit> list = SQLKits.getKitList();
		sender.sendMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_GREEN + "Kits:\n");
		for(Kit kit : list) {
			if(sender.hasPermission("EasyKits.kits." + kit.getName()) && !kit.getName().equalsIgnoreCase(Main.getPlugin().getConfig().getString("Config.First-Join-Kit")) || sender instanceof ConsoleCommandSender) {
				String kitMsg = ChatColor.YELLOW + "- " + kit.getName();
				if(sender instanceof Player) {
					Player player = (Player) sender;

					KitUser kitUser = new KitUser(player, kit);
					if(!player.hasPermission("EasyKits.bypass.limit")){
						if(((kitUser.getCurrentLimit() == 0) && (kit.getLimit() > 0))){
							kitMsg = ChatColor.STRIKETHROUGH + "" + ChatColor.DARK_RED + "- " + kit.getName();
						}
					}
					
					if(!player.hasPermission("EasyKits.bypass.cooldown")){
						if((kitUser.getCooldownTimeRemaining() != null)) {
							kitMsg = ChatColor.STRIKETHROUGH + "" + ChatColor.DARK_RED + "- " + kit.getName();
						}
					}

					double price = kit.getPrice();
					if(price > 0) {
						if(!player.hasPermission("EasyKits.bypass.price")){
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
