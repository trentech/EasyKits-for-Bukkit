package org.trentech.easykits.kits;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.naming.event.EventContext;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.trentech.easykits.Main;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.sql.SQLPlayers;

import com.google.common.graph.ElementOrder.Type;

public class KitService {

	public KitService() {
		
	}
	public static KitService instance() {
		return this;
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
		Kit backup = new Kit("backup", player, 0, 0, 0);
		
		PlayerInventory inv = player.getInventory();
		
		ItemStack[] inventory = kit.getInventory();

		if (!grid.isEmpty()) {
			int i = 0;
			for (Inventory slot : inv.getMainGrid().slots()) {
				if (grid.containsKey(i)) {
					if(slot.peek().isPresent()) {
						if(!likeStack(grid.get(i), inv)) {
							if(!firstEmpty(grid.get(i), inv)) {
								restoreInventory(player, backup);
								return false;
							}
						}
					} else {
						slot.set(grid.get(i));
					}
				}
				i++;
			}
		}

		Optional<ItemStack> helmet = kit.getHelmet();

		if (helmet.isPresent()) {
			if(player.getHelmet().isPresent()) {
				if(!likeStack(helmet.get(), inv)) {
					if(!firstEmpty(helmet.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setHelmet(helmet.get());
			}
		}

		Optional<ItemStack> chestPlate = kit.getChestPlate();

		if (chestPlate.isPresent()) {			
			if(player.getChestplate().isPresent()) {
				if(!likeStack(chestPlate.get(), inv)) {
					if(!firstEmpty(chestPlate.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setChestplate(chestPlate.get());
			}
		}
		
		Optional<ItemStack> leggings = kit.getLeggings();

		if (leggings.isPresent()) {
			if(player.getLeggings().isPresent()) {
				if(!likeStack(leggings.get(), inv)) {
					if(!firstEmpty(leggings.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setLeggings(leggings.get());
			}
		}
		
		Optional<ItemStack> boots = kit.getBoots();

		if (boots.isPresent()) {
			if(player.getBoots().isPresent()) {
				if(!likeStack(boots.get(), inv)) {
					if(!firstEmpty(boots.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setBoots(boots.get());
			}
		}
		
		Optional<ItemStack> offHand = kit.getOffHand();

		if (offHand.isPresent()) {
			if(player.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
				if(!likeStack(offHand.get(), inv)) {
					if(!firstEmpty(offHand.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setItemInHand(HandTypes.OFF_HAND, offHand.get());
			}
		}

		if(updateUsage) {
			if(!updateUsage(kit, player)) {
				restoreInventory(player, backup);
				return false;
			}
		}	
		
		Optional<ItemStack> optionalItemStack = player.getItemInHand(HandTypes.MAIN_HAND);
		
		if(optionalItemStack.isPresent()) {
			ItemStack itemStack = optionalItemStack.get();
			
			Optional<KitInfoData> optionalKitInfo = itemStack.get(KitInfoData.class);
			
			if(optionalKitInfo.isPresent()) {
				KitInfo kitInfo = optionalKitInfo.get().kitInfo().get();
				
				if(kitInfo.getKitName().equalsIgnoreCase(kit.getName())) {
					player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.empty());	
				}
			}
		}
		
		return true;
	}
	
	private boolean firstEmpty(ItemStack itemStack, Inventory inventory) {
		for (Inventory slot : inventory.slots()) {

			if(!slot.peek().isPresent()) {	
				if(slot.set(itemStack).getType().equals(Type.SUCCESS)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean likeStack(ItemStack itemStack, Inventory inventory) {
		for (Inventory slot : inventory.slots()) {
			Optional<ItemStack> optionalItem = slot.peek();
			
			if(optionalItem.isPresent()) {
				ItemStack i = optionalItem.get();
				
				if(i.getType().equals(itemStack.getType())) {
					int fit = i.getMaxStackQuantity() - i.getQuantity();
					
					if(fit >= itemStack.getQuantity()) {
						i.setQuantity(i.getQuantity() + itemStack.getQuantity());
						
						if(slot.set(i).getType().equals(Type.SUCCESS)) {
							return true;
						}
					} else if(fit != 0) {
						i.setQuantity(i.getQuantity() + fit);

						if(slot.set(i).getType().equals(Type.SUCCESS)) {
							itemStack.setQuantity(itemStack.getQuantity() - fit);
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private void restoreInventory(Player player, Kit backup) {
		player.getInventory().clear();

		PlayerInventory inv = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));

		Map<Integer, ItemStack> hotbar = backup.getHotbar();

		if (!hotbar.isEmpty()) {
			int i = 0;
			for (Inventory slot : inv.getHotbar().slots()) {
				if (hotbar.containsKey(i)) {
					slot.set(hotbar.get(i));
				}
				i++;
			}
		}

		Map<Integer, ItemStack> grid = backup.getGrid();

		if (!grid.isEmpty()) {
			int i = 0;
			for (Inventory slot : inv.getMainGrid().slots()) {
				if (grid.containsKey(i)) {
					slot.set(grid.get(i));
				}
				i++;
			}
		}

		Optional<ItemStack> helmet = backup.getHelmet();

		if (helmet.isPresent()) {
			player.setHelmet(helmet.get());
		}

		Optional<ItemStack> chestPlate = backup.getChestPlate();

		if (chestPlate.isPresent()) {
			player.setChestplate(chestPlate.get());
		}
		
		Optional<ItemStack> leggings = backup.getLeggings();

		if (leggings.isPresent()) {
			player.setLeggings(leggings.get());
		}
		
		Optional<ItemStack> boots = backup.getBoots();

		if (boots.isPresent()) {
			player.setBoots(boots.get());
		}
		
		Optional<ItemStack> offHand = backup.getOffHand();

		if (offHand.isPresent()) {
			player.setItemInHand(HandTypes.OFF_HAND, offHand.get());
		}
	}
}
