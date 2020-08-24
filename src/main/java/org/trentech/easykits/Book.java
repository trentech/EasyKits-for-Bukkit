package org.trentech.easykits;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.utils.Notifications;
import org.trentech.easykits.utils.Utils;

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
		ConcurrentHashMap<String, Kit> list = SQLKits.all();
		
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
		for(Entry<String, Kit> entry : list.entrySet()){
			Kit kit = entry.getValue();
			ItemStack kitItem = new ItemStack(Material.BOOK);
			ItemMeta itemMeta = kitItem.getItemMeta();
			itemMeta.setDisplayName("EasyKits: " + kit.getName());
			ArrayList<String> lore = new ArrayList<String>();
			if(Main.getPlugin().getEconomy() != null){
				double price = kit.getPrice();
				if(price > 0){		
					if(!player.hasPermission("EasyKits.bypass.price")){
						lore.add(ChatColor.GREEN + "Price: $" + price);
					}	
				}
			}

			int limit = kit.getLimit();
			if(limit > 0){
				if(!player.hasPermission("EasyKits.bypass.limit")){
					lore.add(ChatColor.GREEN + "Limit: " + limit);	
				}		
			}
			
			long cooldown = kit.getCooldown();
			if(cooldown != 0){
				if(!player.hasPermission("EasyKits.bypass.cooldown")){
					lore.add(ChatColor.GREEN + "Cooldown: " + cooldown);
				}
			}
			
			itemMeta.setLore(lore);
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
			Notifications notify = new Notifications("kit-book-full");
			player.sendMessage(notify.getMessage());
		}
	}
	
	public static void pageTwo(Player player, Kit kit){
		Inventory showInv = Main.getPlugin().getServer().createInventory(player, 45, "EasyKits: " + kit.getName());

		showInv.setContents(kit.getInventory());

		int index = 36;
		for(ItemStack a : kit.getEquipment()){
			showInv.setItem(index, a);
			index++;
		}
	
		String currency = Main.getPlugin().getConfig().getString("config.currency-symbol");

		ItemStack getPrice = new ItemStack(Material.BARRIER);
		ItemMeta priceMeta = getPrice.getItemMeta();
		priceMeta.setDisplayName(ChatColor.GREEN + "Price: " + currency + ChatColor.WHITE + new DecimalFormat(".##").format(kit.getPrice()));
		getPrice.setItemMeta(priceMeta);
		
		ItemStack getLimit = new ItemStack(Material.BARRIER);
		ItemMeta limitMeta = getLimit.getItemMeta();
		limitMeta.setDisplayName(ChatColor.GREEN + "Limit: " + ChatColor.WHITE + kit.getLimit());
		getLimit.setItemMeta(limitMeta);
		
		ItemStack getCooldown = new ItemStack(Material.BARRIER);
		ItemMeta cooldownMeta = getCooldown.getItemMeta();
		cooldownMeta.setDisplayName(ChatColor.GREEN + "Cooldown: " + ChatColor.WHITE + Utils.getReadableTime(kit.getCooldown()));
		getCooldown.setItemMeta(cooldownMeta);
		
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
		
		showInv.setItem(40, getPrice);
		showInv.setItem(41, getLimit);
		showInv.setItem(42, getCooldown);
		showInv.setItem(43, backButton);
		showInv.setItem(44, getKit);
		
		player.openInventory(showInv);
	}
}
