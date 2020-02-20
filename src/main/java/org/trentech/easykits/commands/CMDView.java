package org.trentech.easykits.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.utils.Notifications;

public class CMDView {

	public static void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			Notifications notify = new Notifications("Not-Player", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}
		Player player = (Player) sender;
		if(!sender.hasPermission("EasyKits.cmd.view")){
			Notifications notify = new Notifications("Permission-Denied", null, sender.getName(), 0, null, 0);
			sender.sendMessage(notify.getMessage());
			return;
		}

		if(args.length == 2){
			String kitName = args[1];
			Kit kit = new Kit(kitName);
			if(!kit.exists()) {
				Notifications notify = new Notifications("Kit-Not-Exist", kit.getName(), sender.getName(), 0, null, 0);
				sender.sendMessage(notify.getMessage());
				return;
			}

			ItemStack[] inv = kit.getInventory();
			ItemStack[] arm = kit.getArmor();

			Inventory showInv = Main.getPlugin().getServer().createInventory(player, 45, "EasyKits Kit: " + kit.getName());
			showInv.setContents(inv);								
			int index = 36;
			for(ItemStack a : arm){
				showInv.setItem(index, a);
				index++;
			}	
			ItemStack getKit = new ItemStack(Material.NETHER_STAR);
			ItemMeta getKitMeta = getKit.getItemMeta();
			getKitMeta.setDisplayName(ChatColor.GREEN + "Get " + kitName.toLowerCase());
			ArrayList<String> getKitLores = new ArrayList<String>();
			getKitLores.add(ChatColor.DARK_PURPLE+ "Click to equip kit!");
			getKitMeta.setLore(getKitLores);
			getKit.setItemMeta(getKitMeta);								
			showInv.setItem(44, getKit);
			player.openInventory(showInv);

		}else{
			sender.sendMessage(ChatColor.YELLOW + "/kit view <kitname>");
		}
	}

}
