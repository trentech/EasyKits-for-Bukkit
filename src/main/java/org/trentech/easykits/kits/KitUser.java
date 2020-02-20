package org.trentech.easykits.kits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.trentech.easykits.Main;
import org.trentech.easykits.events.KitPlayerCooldownEvent;
import org.trentech.easykits.events.KitPlayerEquipEvent;
import org.trentech.easykits.events.KitPlayerLimitEvent;
import org.trentech.easykits.events.WithdrawMoneyEvent;
import org.trentech.easykits.sql.SQLPlayers;
import org.trentech.easykits.utils.Notifications;
import org.trentech.easykits.utils.Utils;

public class KitUser {

	private Player user;
	private Kit kit;
	private String userUUID;
	private int limit;
	private String dateObtained;

	public KitUser(Player user, Kit kit) {
		this.user = user;
		this.kit = kit;
		userUUID = user.getUniqueId().toString();		
		if(!SQLPlayers.kitExist(userUUID, kit.getName())) {
			SQLPlayers.addKit(userUUID, kit.getName(), "2000-1-1 12:00:00", kit.getLimit(), "FALSE");
		}
		limit = SQLPlayers.getLimit(userUUID, kit.getName());
		if(limit == -1){
			limit = kit.getLimit();
		}
		dateObtained = SQLPlayers.getDateObtained(userUUID, kit.getName());
	}

	public int getCurrentLimit() {
		return limit;
	}

	public String getCooldownTimeRemaining(){
		if(!hasObtainedBefore()) {
			return null;
		}
		if(kit.getCooldown().equalsIgnoreCase("0")) {
			return null;
		}
		
		Date date = new Date();
		Date dateObtained = null;
		if(getDateObtained().equalsIgnoreCase("0")){
			return null;
		}
		
		try {
			dateObtained = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getDateObtained());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long timeSince = TimeUnit.MILLISECONDS.toSeconds(date.getTime() - dateObtained.getTime());
		int waitTime = Utils.getTimeInSeconds(kit.getCooldown());		
		if(waitTime - timeSince <= 0) {	
			return null;
		}

		return Utils.getReadableTime((int) (waitTime - timeSince));
	}

	public String getDateObtained() {
		return dateObtained;
	}

	public boolean setDateObtained(String dateObtained) {
		KitPlayerCooldownEvent event = new KitPlayerCooldownEvent(user, kit);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if(!event.isCancelled()){
			SQLPlayers.setDateObtained(userUUID, kit.getName(), dateObtained);
			this.dateObtained = dateObtained;
			return true;
		}
		return false;
	}

	public boolean setCurrentLimit(int limit) {
		KitPlayerLimitEvent event = new KitPlayerLimitEvent(user, kit);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if(!event.isCancelled()){
			if(kit.getLimit() > 0) {
				SQLPlayers.setLimit(userUUID, kit.getName(), limit);
				this.limit = limit;
			}
			return true;
		}
		return false;
	}

	public boolean chargeUser(){		
		WithdrawMoneyEvent event = new WithdrawMoneyEvent(user, kit, this);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if(!event.isCancelled()){
			if(kit.getPrice() != 0){				
				if(Main.getPlugin().getEconomy().getBalance(user) < kit.getPrice()){
					return false;
				}
				Main.getPlugin().getEconomy().withdrawPlayer(user, kit.getPrice());
				return true;
			}
		}
		return false;
	}

	public boolean hasObtainedBefore() {
		if(SQLPlayers.getObtained(userUUID, kit.getName()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

	public boolean canAfford() {
		double price = kit.getPrice();
		double balance = Main.getPlugin().getEconomy().getBalance(user);
		if(balance < price){
			return false;
		}
		return true;
	}

	public boolean applyKit() throws Exception{
		KitPlayerEquipEvent event = new KitPlayerEquipEvent(user, kit, this);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if(!event.isCancelled()){
			ItemStack[] inv = kit.getInventory();
			ItemStack[] arm = kit.getArmor();
			Inventory tempInv = Main.getPlugin().getServer().createInventory(user, InventoryType.PLAYER);
			Inventory tempArm = Main.getPlugin().getServer().createInventory(user, 9);
			tempInv.setContents(user.getInventory().getContents());
			tempArm.setContents(user.getInventory().getArmorContents());
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
					HashMap<Integer, ? extends ItemStack> matchItem = user.getInventory().all(item);				
					int size = matchItem.size();
					if(size < item.getMaxStackSize()){
						size = item.getMaxStackSize() - size;
						if(amount <= size){
							tempInv.setItem(index, item);
						}else{
							Notifications notify = new Notifications("Inventory-Space", kit.getName(), user.getName(), 0, null, 0);
							user.sendMessage(notify.getMessage());
							return false;					
						}
					}else{
						for(int i = 10; i <= 36; i++){
							if(i == 36){
								Notifications notify = new Notifications("Inventory-Space", kit.getName(), user.getName(), 0, null, 0);
								user.sendMessage(notify.getMessage());
								return false;
							}
							size = size - item.getMaxStackSize();
							if(size < item.getMaxStackSize()){
								if(amount <= size){					
									tempInv.setItem(index, item);
								}
							}
						}
						Notifications notify = new Notifications("Inventory-Space", kit.getName(), user.getName(), 0, null, 0);
						user.sendMessage(notify.getMessage());
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
							Notifications notify = new Notifications("Inventory-Space", kit.getName(), user.getName(), 0, null, 0);
							user.sendMessage(notify.getMessage());
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
							Notifications notify = new Notifications("Inventory-Space", kit.getName(), user.getName(), 0, null, 0);
							user.sendMessage(notify.getMessage());
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
							Notifications notify = new Notifications("Inventory-Space", kit.getName(), user.getName(), 0, null, 0);
							user.sendMessage(notify.getMessage());
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
							Notifications notify = new Notifications("Inventory-Space", kit.getName(), user.getName(), 0, null, 0);
							user.sendMessage(notify.getMessage());
							return false;
						}
					}
					break;
				}
				index++;
			}
			user.getInventory().setContents(tempInv.getContents());
			if(tempArm.getItem(0) != null){
				user.getInventory().setBoots(tempArm.getItem(0));
			}
			if(tempArm.getItem(1) != null){
				user.getInventory().setLeggings(tempArm.getItem(1));
			}
			if(tempArm.getItem(2) != null){
				user.getInventory().setChestplate(tempArm.getItem(2));
			}
			if(tempArm.getItem(3) != null){
				user.getInventory().setHelmet(tempArm.getItem(3));
			}
			SQLPlayers.setObtained(userUUID, kit.getName(), "TRUE");
			return true;
		}
		return false;
	}

}
