package org.trentech.easykits;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.trentech.easykits.utils.Utils;

public class Kit implements Serializable {

	private static final long serialVersionUID = 3223394759187720840L;
	
	private String name;
	private Optional<ItemStack> offHand = Optional.empty();
	private Optional<ItemStack> helmet = Optional.empty();
	private Optional<ItemStack> chestPlate = Optional.empty();
	private Optional<ItemStack> leggings = Optional.empty();
	private Optional<ItemStack> boots = Optional.empty();
	private Map<Integer, ItemStack> inventory = new HashMap<>();
	private long cooldown = 0;
	private int limit = 0;
	private double price = 0.0;
	
	protected Kit(String name, Optional<ItemStack> offHand, Optional<ItemStack> helmet, Optional<ItemStack> chestPlate, Optional<ItemStack> leggings, 
			Optional<ItemStack> boots, Map<Integer, ItemStack> inventory, long cooldown, int limit, double price) {
		this.name = name;
		this.offHand = offHand;
		this.inventory = inventory;
		this.helmet = helmet;
		this.chestPlate = chestPlate;
		this.leggings = leggings;
		this.boots = boots;
		this.cooldown = cooldown;
		this.limit = limit;
		this.price = price;
	}

	public Kit(String name, Player player, long cooldown, int limit, double price) {
		this.name = name;
		this.cooldown = cooldown;
		this.limit = limit;
		this.price = price;
		
		PlayerInventory inv = player.getInventory();

		int i = 0;
		for (ItemStack item : inv.getContents()) {
			if(item != null) {
				addInventory(i, item);
			}
			
			i++;
		}

		if(inv.getItemInOffHand() != null) {
			setOffHand(inv.getItemInOffHand());
		}
		
		if(inv.getHelmet() != null) {
			setHelmet(inv.getHelmet());
		}
		
		if(inv.getChestplate() != null) {
			setChestPlate(inv.getChestplate());
		}
		
		if(inv.getLeggings() != null) {
			setLeggings(inv.getLeggings());
		}
		
		if(inv.getBoots() != null) {
			setBoots(inv.getBoots());
		}

	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCooldown() {
		return cooldown;
	}
	
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public Optional<ItemStack> getOffHand() {
		return offHand;
	}

	public Optional<ItemStack> getHelmet() {
		return helmet;
	}

	public Optional<ItemStack> getChestPlate() {
		return chestPlate;
	}

	public Optional<ItemStack> getLeggings() {
		return leggings;
	}

	public Optional<ItemStack> getBoots() {
		return boots;
	}

	public Map<Integer, ItemStack> getInventory() {
		return inventory;
	}
	
	public void setOffHand(ItemStack itemStack) {
		this.offHand = Optional.of(itemStack);
	}
	
	public void setHelmet(ItemStack itemStack) {
		this.helmet = Optional.of(itemStack);
	}

	public void setChestPlate(ItemStack itemStack) {
		this.chestPlate = Optional.of(itemStack);
	}

	public void setLeggings(ItemStack itemStack) {
		this.leggings = Optional.of(itemStack);
	}

	public void setBoots(ItemStack itemStack) {
		this.boots = Optional.of(itemStack);
	}

	public void addEquipment(int slot, ItemStack itemStack) {
		if (slot == 0) {
			this.helmet = Optional.of(itemStack);
		} else if(slot == 1) {
			this.chestPlate = Optional.of(itemStack);
		} else if(slot == 2) {
			this.leggings = Optional.of(itemStack);
		} else if(slot == 3) {
			this.boots = Optional.of(itemStack);
		}
	}
	
	public void removeEquipment(int slot) {
		if (slot == 0) {
			this.helmet = Optional.empty();
		} else if(slot == 1) {
			this.chestPlate = Optional.empty();
		} else if(slot == 2) {
			this.leggings = Optional.empty();
		} else if(slot == 3) {
			this.boots = Optional.empty();
		}
	}
	
	public void addInventory(Integer slot, ItemStack itemStack) {
		this.inventory.put(slot, itemStack);
	}

	public void removeGrid(Integer slot) {
		this.inventory.remove(slot);
	}

	public ItemStack getBook(boolean check) {
		List<String> lore = new ArrayList<>();
		
		lore.add(ChatColor.GREEN + "Name: " + ChatColor.WHITE + getName());

		if(getPrice() > 0) {
			String currency = Main.getPlugin().getConfig().getString("config.currency-symbol");
			
			lore.add(ChatColor.GREEN + "Price: " + ChatColor.WHITE + currency + new DecimalFormat(".##").format(getPrice()));
		}
	
		if(getLimit() > 0) {
			lore.add(ChatColor.GREEN + "Limit: " + ChatColor.WHITE + getLimit());
		}
		
		if(getCooldown() > 0) {
			lore.add(ChatColor.GREEN + "Cooldown: " + ChatColor.WHITE + Utils.getReadableTime(getCooldown()));
		}

		ItemStack itemStack = new ItemStack(Material.BOOK);
		itemStack.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 1);
        ItemMeta meta = itemStack.getItemMeta();
        
        meta.setDisplayName(ChatColor.WHITE + "EasyKits");
        meta.setLore(lore);
        
		return itemStack;
	}
}
