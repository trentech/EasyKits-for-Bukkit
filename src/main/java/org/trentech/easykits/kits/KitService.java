package org.trentech.easykits.kits;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

	private static KitService kitService;
	
	public KitService() {
		kitService = this;
	}
	
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
	
	public boolean updateUsage(Kit kit, Player player) {
		Optional<KitUsage> optional = SQLPlayers.getKitUsage(player, kit.getName());
		KitUsage kitUsage;
		
		if (optional.isPresent()) {
			kitUsage = optional.get();
		} else {
			kitUsage = new KitUsage(kit.getName());
		}
		
		if(!player.hasPermission("easykits.override.cooldown")) {
			if(kit.getCooldown() > 0) {
				Date date = kitUsage.getDate();
				
				long timeSince = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - date.getTime());
				long waitTime = kit.getCooldown();
				
				if(waitTime - timeSince > 0) {	
					return false;
				}
				
				kitUsage.setDate(new Date());
			}
		}

		if(!player.hasPermission("easykits.override.limit")) {
			if(kit.getLimit() > 0) {
				if(kitUsage.getTimesUsed() >= kit.getLimit()) {
					return false;
				}
				
				kitUsage.setTimesUsed(kitUsage.getTimesUsed() + 1);
			}
		}

		if(!player.hasPermission("easykits.override.price")) {
			if(kit.getPrice() > 0) {
				if(Main.getPlugin().supportsEconomy()) {
					if(Main.getPlugin().getEconomy().getBalance(player) < kit.getPrice()){
						player.sendMessage(ChatColor.RED + "You do not have enough money. Require " + Main.getPlugin().getConfig().getString("config.currency-symbol") + kit.getPrice());
						return false;
					}
					
					Main.getPlugin().getEconomy().withdrawPlayer(player, kit.getPrice());
				}
			}
		}
		
		SQLPlayers.update(player, kitUsage);
		
		return true;
	}
	
	public boolean setKit(Player player, Kit kit, boolean updateUsage) {
		KitEvent.Get event = new KitEvent.Get(player, kit);
		
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
							Notifications notify = new Notifications("inventory-space", kit.getName(), player.getName(), 0, null, 0);
							player.sendMessage(notify.getMessage());
							return false;					
						}
					}else{
						for(int i = 10; i <= 36; i++){
							if(i == 36){
								Notifications notify = new Notifications("inventory-space", kit.getName(), player.getName(), 0, null, 0);
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
						Notifications notify = new Notifications("inventory-space", kit.getName(), player.getName(), 0, null, 0);
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
							Notifications notify = new Notifications("inventory-space", kit.getName(), player.getName(), 0, null, 0);
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
							Notifications notify = new Notifications("inventory-space", kit.getName(), player.getName(), 0, null, 0);
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
							Notifications notify = new Notifications("inventory-space", kit.getName(), player.getName(), 0, null, 0);
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
							Notifications notify = new Notifications("inventory-space", kit.getName(), player.getName(), 0, null, 0);
							player.sendMessage(notify.getMessage());
							return false;
						}
					}
					break;
				}
				index++;
			}
			
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
	
//	private boolean firstEmpty(ItemStack itemStack, Inventory inventory) {
//		for (Inventory slot : inventory.slots()) {
//
//			if(!slot.peek().isPresent()) {	
//				if(slot.set(itemStack).getType().equals(Type.SUCCESS)) {
//					return true;
//				}
//			}
//		}
//		
//		return false;
//	}
//	
//	private boolean likeStack(ItemStack itemStack, Inventory inventory) {
//		for (Inventory slot : inventory.slots()) {
//			Optional<ItemStack> optionalItem = slot.peek();
//			
//			if(optionalItem.isPresent()) {
//				ItemStack i = optionalItem.get();
//				
//				if(i.getType().equals(itemStack.getType())) {
//					int fit = i.getMaxStackQuantity() - i.getQuantity();
//					
//					if(fit >= itemStack.getQuantity()) {
//						i.setQuantity(i.getQuantity() + itemStack.getQuantity());
//						
//						if(slot.set(i).getType().equals(Type.SUCCESS)) {
//							return true;
//						}
//					} else if(fit != 0) {
//						i.setQuantity(i.getQuantity() + fit);
//
//						if(slot.set(i).getType().equals(Type.SUCCESS)) {
//							itemStack.setQuantity(itemStack.getQuantity() - fit);
//						}
//					}
//				}
//			}
//		}
//		
//		return false;
//	}
//	
//	private void restoreInventory(Player player, Kit backup) {
//		player.getInventory().clear();
//
//		PlayerInventory inv = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));
//
//		Map<Integer, ItemStack> hotbar = backup.getHotbar();
//
//		if (!hotbar.isEmpty()) {
//			int i = 0;
//			for (Inventory slot : inv.getHotbar().slots()) {
//				if (hotbar.containsKey(i)) {
//					slot.set(hotbar.get(i));
//				}
//				i++;
//			}
//		}
//
//		Map<Integer, ItemStack> grid = backup.getGrid();
//
//		if (!grid.isEmpty()) {
//			int i = 0;
//			for (Inventory slot : inv.getMainGrid().slots()) {
//				if (grid.containsKey(i)) {
//					slot.set(grid.get(i));
//				}
//				i++;
//			}
//		}
//
//		Optional<ItemStack> helmet = backup.getHelmet();
//
//		if (helmet.isPresent()) {
//			player.setHelmet(helmet.get());
//		}
//
//		Optional<ItemStack> chestPlate = backup.getChestPlate();
//
//		if (chestPlate.isPresent()) {
//			player.setChestplate(chestPlate.get());
//		}
//		
//		Optional<ItemStack> leggings = backup.getLeggings();
//
//		if (leggings.isPresent()) {
//			player.setLeggings(leggings.get());
//		}
//		
//		Optional<ItemStack> boots = backup.getBoots();
//
//		if (boots.isPresent()) {
//			player.setBoots(boots.get());
//		}
//		
//		Optional<ItemStack> offHand = backup.getOffHand();
//
//		if (offHand.isPresent()) {
//			player.setItemInHand(HandTypes.OFF_HAND, offHand.get());
//		}
//	}
}
