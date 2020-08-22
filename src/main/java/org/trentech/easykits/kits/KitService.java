package org.trentech.easykits.kits;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.trentech.easykits.Main;
import org.trentech.easykits.events.KitEvent;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.sql.SQLPlayers;
import org.trentech.easykits.utils.Notifications;

public class KitService {

	private static KitService kitService = new KitService();

	public static KitService instance() {
		return kitService;
	}
	
	public Optional<Kit> getKit(String name) {
		return SQLKits.get(name);
	}

	public ConcurrentHashMap<String, Kit> getKits() {
		return SQLKits.all();
	}

	public void delete(String name) {
		SQLKits.delete(name);
	}
	
	public void save(Kit kit) {
		if (SQLKits.get(kit.getName()).isPresent()) {
			SQLKits.update(kit);
		} else {
			SQLKits.create(kit);
		}
	}
	
	private void updateUsage(Kit kit, Player player) {
		KitUsage kitUsage = SQLPlayers.getKitUsage(player, kit.getName());

		if(!player.hasPermission("easykits.override.cooldown")) {
			if(kit.getCooldown() > 0) {
				kitUsage.setDate(new Date());
			}
		}

		if(!player.hasPermission("easykits.override.limit")) {
			if(kit.getLimit() > 0) {
				kitUsage.setTimesUsed(kitUsage.getTimesUsed() + 1);
			}
		}

		if(!player.hasPermission("easykits.override.price")) {
			if(kit.getPrice() > 0) {
				if(Main.getPlugin().supportsEconomy()) {
					Main.getPlugin().getEconomy().withdrawPlayer(player, kit.getPrice());
				}
			}
		}
		
		SQLPlayers.update(player, kitUsage);
	}
	
	public boolean setKit(Player player, Kit kit, boolean updateUsage) {
		KitEvent.Get event = new KitEvent.Get(player, kit, updateUsage);
		
		Bukkit.getServer().getPluginManager().callEvent(event);

		if(!event.isCancelled()){
			ItemStack[] inv = kit.getInventory();
			ItemStack[] arm = kit.getEquipment();
			
			Inventory tempInv = Main.getPlugin().getServer().createInventory(player, InventoryType.PLAYER);
			Inventory tempArm = Main.getPlugin().getServer().createInventory(player, 9);
			
			tempInv.setContents(player.getInventory().getContents());
			tempArm.setContents(player.getInventory().getArmorContents());
			
			int index = 0;
			for(ItemStack item : inv){
				if(item == null){
					item = new ItemStack(Material.AIR);
				}
				if(tempInv.getItem(index) == null){
					tempInv.setItem(index, item);
				}else if(tempInv.firstEmpty() > -1){
					tempInv.addItem(item);			
				}else{
					int amount = item.getAmount();
					HashMap<Integer, ? extends ItemStack> matchItem = player.getInventory().all(item);				
					int size = matchItem.size();
					if(size < item.getMaxStackSize()){
						size = item.getMaxStackSize() - size;
						if(amount <= size){
							tempInv.setItem(index, item);
						}else{
							Notifications notify = new Notifications("inventory-space", kit.getName(), player);
							player.sendMessage(notify.getMessage());
							return false;					
						}
					}else{
						for(int i = 10; i <= 36; i++){
							if(i == 36){
								Notifications notify = new Notifications("inventory-space", kit.getName(), player);
								player.sendMessage(notify.getMessage());
								return false;
							}
							size = size - item.getMaxStackSize();
							if(size < item.getMaxStackSize()){
								if(amount <= size){					
									tempInv.setItem(index, item);
								}
							}
						}
						Notifications notify = new Notifications("inventory-space", kit.getName(), player);
						player.sendMessage(notify.getMessage());
						return false;
					}
				}
				index++;
			}
			index = 0;
			for(ItemStack item : arm){
				if(index == 0){
					if(item != null){
						if(tempArm.getItem(0) == null){
							tempArm.setItem(0, item);
						}else if(tempInv.firstEmpty() > -1){
							tempInv.addItem(item);
						}else{
							Notifications notify = new Notifications("inventory-space", kit.getName(), player);
							player.sendMessage(notify.getMessage());
							return false;
						}
					}
				}
				if(index == 1){
					if(item != null){
						if(tempArm.getItem(1) == null){
							tempArm.setItem(1, item);
						}else if(tempInv.firstEmpty() > -1){
							tempInv.addItem(item);
						}else{
							Notifications notify = new Notifications("inventory-space", kit.getName(), player);
							player.sendMessage(notify.getMessage());
							return false;
						}
					}
				}
				if(index == 2){
					if(item != null){
						if(tempArm.getItem(2) == null){
							tempArm.setItem(2, item);
						}else if(tempInv.firstEmpty() > -1){
							tempInv.addItem(item);
						}else{
							Notifications notify = new Notifications("inventory-space", kit.getName(), player);
							player.sendMessage(notify.getMessage());
							return false;
						}
					}
				}
				if(index == 3){
					if(item != null){
						if(tempArm.getItem(3) == null){
							tempArm.setItem(3, item);
						}else if(tempInv.firstEmpty() > -1){
							tempInv.addItem(item);
						}else{
							Notifications notify = new Notifications("inventory-space", kit.getName(), player);
							player.sendMessage(notify.getMessage());
							return false;
						}
					}
					break;
				}
				index++;
			}

			updateUsage(kit, player);
			
			player.getInventory().setContents(tempInv.getContents());
			
			if(tempArm.getItem(0) != null){
				player.getInventory().setBoots(tempArm.getItem(0));
			}
			if(tempArm.getItem(1) != null){
				player.getInventory().setLeggings(tempArm.getItem(1));
			}
			if(tempArm.getItem(2) != null){
				player.getInventory().setChestplate(tempArm.getItem(2));
			}
			if(tempArm.getItem(3) != null){
				player.getInventory().setHelmet(tempArm.getItem(3));
			}
			return true;
		}
		return false;
	}
}
