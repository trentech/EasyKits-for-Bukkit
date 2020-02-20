package org.trentech.easykits.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.trentech.easykits.Main;
import org.trentech.easykits.utils.Notifications;

public class Kit {

	private String name;
	private int limit;
	private long cooldown;
	private double price;
	private ItemStack[] inventory;
	private ItemStack[] equipment;

	public Kit(String name, ItemStack[] inventory, ItemStack[] equipment) {
		this.name = name;
		this.inventory = inventory;
		this.equipment = equipment;
		
		FileConfiguration config = Main.getPlugin().getConfig();
		
		this.cooldown = config.getLong("config.default-cooldown");
		this.price = config.getDouble("config.default-price");
		this.limit = config.getInt("config.default-kit-limit");
	}
	
	public Kit(String name, ItemStack[] inventory, ItemStack[] equipment, long cooldown, double price, int limit) {
		this.name = name;
		this.inventory = inventory;
		this.equipment = equipment;
		this.cooldown = cooldown;
		this.price = price;
		this.limit = limit;
	}
	
	public Kit(String name, Player player, long cooldown, double price, int limit) {
		this.name = name;
		this.inventory = player.getInventory().getContents();
		this.equipment = player.getInventory().getArmorContents();
		this.cooldown = cooldown;
		this.price = price;
		this.limit = limit;
	}

	public String getName() {
		return name;
	}

	public ItemStack[] getInventory(){
        return inventory;
	}
	
	public ItemStack[] getEquipment(){
        return equipment;
	}

	public int getLimit(){
		return limit;	
	}

	public double getPrice(){
		return price;
	}

	public long getCooldown(){
		return cooldown;
	}

	public void setInventory(ItemStack[] inventory){	
		this.inventory = inventory;
	}
	
	public void setEquipment(ItemStack[] equipment){	
		this.equipment = equipment;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	
	public boolean setKit(Player player, boolean updateUsage) {
		Inventory tempInv = Main.getPlugin().getServer().createInventory(player, InventoryType.PLAYER);
		Inventory tempArm = Main.getPlugin().getServer().createInventory(player, 9);
		
		tempInv.setContents(player.getInventory().getContents());
		tempArm.setContents(player.getInventory().getArmorContents());
		
		int index = 0;
		for(ItemStack item : getInventory()){
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
						Notifications notify = new Notifications("Inventory-Space", getName(), getName(), 0, null, 0);
						player.sendMessage(notify.getMessage());
						return false;					
					}
				}else{
					for(int i = 10; i <= 36; i++){
						if(i == 36){
							Notifications notify = new Notifications("Inventory-Space", getName(), getName(), 0, null, 0);
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
					Notifications notify = new Notifications("Inventory-Space", getName(), getName(), 0, null, 0);
					player.sendMessage(notify.getMessage());
					return false;
				}
			}
			index++;
		}
		index = 0;
		for(ItemStack item : getEquipment()){
			if(index == 0){
				if(item != null){
					if(tempArm.getItem(0) == null){
						tempArm.setItem(0, item);
					}else if(tempInv.firstEmpty() > -1){
						tempInv.addItem(item);
					}else{
						Notifications notify = new Notifications("Inventory-Space", getName(), getName(), 0, null, 0);
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
						Notifications notify = new Notifications("Inventory-Space", getName(), getName(), 0, null, 0);
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
						Notifications notify = new Notifications("Inventory-Space", getName(), getName(), 0, null, 0);
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
						Notifications notify = new Notifications("Inventory-Space", getName(), getName(), 0, null, 0);
						player.sendMessage(notify.getMessage());
						return false;
					}
				}
				break;
			}
			index++;
		}

		if(updateUsage) {	
			if(!KitService.instance().updateUsage(this, player)) {
				return false;
			}
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

//	public static void create(String name, Player creator, ItemStack[] inventory, ItemStack[] equipment) throws SQLException {
//		KitCreateEvent event = new KitCreateEvent(creator, name);
//		Bukkit.getServer().getPluginManager().callEvent(event);
//
//		if(!event.isCancelled()) {
//			SQLKits.create(new Kit(name.toLowerCase(), inventory, equipment));
//		}
//	}
//	
//	public static void create(String name, Player creator, ItemStack[] inventory, ItemStack[] equipment, double price, long cooldown, int limit) throws SQLException {
//		KitCreateEvent event = new KitCreateEvent(creator, name);
//		Bukkit.getServer().getPluginManager().callEvent(event);
//
//		if(!event.isCancelled()) {
//			SQLKits.create(new Kit(name.toLowerCase(), inventory, equipment, cooldown, price, limit));
//		}
//	}
}
