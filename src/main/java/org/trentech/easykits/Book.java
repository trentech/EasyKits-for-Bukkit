package org.trentech.easykits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitUser;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.utils.Notifications;

public class Book {

	public static ItemStack getBook(String meta, String strLore){
		ItemStack itemStack = new ItemStack(Material.BOOK, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setDisplayName(meta);
		
		if(strLore != null){
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GREEN + strLore);
			itemMeta.setLore(lore);
		}
		
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	public static void pageOne(Player player){
		Inventory kitInv = Main.getPlugin().getServer().createInventory(player, 54, "EasyKits:");
		List<Kit> list = SQLKits.getKitList();
		
		if(list.size() <= 9){
			kitInv = Main.getPlugin().getServer().createInventory(player, 9, "EasyKits:");
		}else if(list.size() > 9 && list.size() <= 18){
			kitInv = Main.getPlugin().getServer().createInventory(player, 18, "EasyKits:");
		}else if(list.size() > 18 && list.size() <= 27){
			kitInv = Main.getPlugin().getServer().createInventory(player, 27, "EasyKits:");
		}else if(list.size() > 27 && list.size() <= 36){
			kitInv = Main.getPlugin().getServer().createInventory(player, 36, "EasyKits:");
		}else if(list.size() > 36 && list.size() <= 45){
			kitInv = Main.getPlugin().getServer().createInventory(player, 45, "EasyKits:");
		}
		int index = 0;
		boolean warning = false;
		for(Kit kit : list){
			ItemStack kitItem = new ItemStack(Material.BOOK);
			ItemMeta itemMeta = kitItem.getItemMeta();
			itemMeta.setDisplayName("EasyKits: " + kit.getName());
			ArrayList<String> lore = new ArrayList<String>();
			if(Main.getPlugin().supportsEconomy()){
				double price = kit.getPrice();
				if(price > 0){		
					if(!player.hasPermission("EasyKits.bypass.price")){
						double balance = Main.getPlugin().getEconomy().getBalance(player);
						if(balance < price) {
							lore.add(ChatColor.DARK_RED + "Price: $" + price);
						}else{
							lore.add(ChatColor.GREEN + "Price: $" + price);
						}
						itemMeta.setLore(lore);
					}	
				}
			}
			
			KitUser kitUser = new KitUser(player, kit);
			int limit = kit.getLimit();
			if(limit > 0){
				if(!player.hasPermission("EasyKits.bypass.limit")){
					if(((kitUser.getCurrentLimit() == 0) && (kit.getLimit() > 0))) {
						lore.add(ChatColor.DARK_RED + "Limit: " + limit);
					}else{
						lore.add(ChatColor.GREEN + "Limit: " + limit);	
					}
					itemMeta.setLore(lore);
				}		
			}
			
			String cooldown = kit.getCooldown();
			if(!cooldown.equalsIgnoreCase("0")){
				if(!player.hasPermission("EasyKits.bypass.cooldown")){
					if(kitUser.getCooldownTimeRemaining() != null){
						lore.add(ChatColor.DARK_RED + "Cooldown: " + cooldown);
					}else{
						lore.add(ChatColor.GREEN + "Cooldown: " + cooldown);
					}	
					itemMeta.setLore(lore);
				}
			}
			
			kitItem.setItemMeta(itemMeta);
			if(index < 54){
				kitInv.addItem(kitItem);
				index++;
			}else{
				warning = true;
			}		
		}
		
		player.openInventory(kitInv);
		if(warning == true){
			Notifications notify = new Notifications("Kit-Book-Full", null, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
		}
	}
	
	public static void pageTwo(Player player, Kit kit){
		ItemStack[] inv = kit.getInventory();
		ItemStack[] arm = kit.getEquipment();

		Inventory showInv = Main.getPlugin().getServer().createInventory(player, 45, "EasyKits: " + kit.getName());
		showInv.setContents(inv);								
		int index = 36;
		for(ItemStack a : arm){
			showInv.setItem(index, a);
			index++;
		}	
		ItemStack getKit = new ItemStack(Material.NETHER_STAR);
		ItemMeta getKitMeta = getKit.getItemMeta();
		getKitMeta.setDisplayName(ChatColor.GREEN + "Get " + kit.getName().toLowerCase());
		ArrayList<String> getKitLores = new ArrayList<String>();
		getKitLores.add(ChatColor.DARK_PURPLE+ "Click to equip kit!");
		getKitMeta.setLore(getKitLores);
		getKit.setItemMeta(getKitMeta);
		
		ItemStack backButton = new ItemStack(Material.BOOK);
		ItemMeta backButtonMeta = backButton.getItemMeta();
		backButtonMeta.setDisplayName(ChatColor.GREEN + "Back");
		ArrayList<String> backButtonLores = new ArrayList<String>();
		backButtonLores.add(ChatColor.DARK_PURPLE+ "Back to Kits!");
		backButtonMeta.setLore(backButtonLores);
		backButton.setItemMeta(backButtonMeta);
		
		showInv.setItem(43, backButton);
		showInv.setItem(44, getKit);
		player.openInventory(showInv);
	}
}
