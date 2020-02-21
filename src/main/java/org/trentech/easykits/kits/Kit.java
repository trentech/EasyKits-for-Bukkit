package org.trentech.easykits.kits;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.trentech.easykits.Main;

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
}
