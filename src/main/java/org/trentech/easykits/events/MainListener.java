package org.trentech.easykits.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.trentech.easykits.Book;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitUser;
import org.trentech.easykits.sql.SQLPlayers;
import org.trentech.easykits.utils.Notifications;

public class MainListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLoginEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();

		if(!SQLPlayers.tableExist(player)) {
			SQLPlayers.createTable(player);
		}
		
		if(player.hasPlayedBefore()){
			return;
		}

		String kitName = Main.getPlugin().getConfig().getString("Config.New-Player-Kit");
		
		if(kitName.equalsIgnoreCase("NONE")) {
			return;
		}
		
		Optional<Kit> optionalKit = Kit.get(kitName);
		
		if(!optionalKit.isPresent()) {
			Main.getPlugin().getLogger().warning(ChatColor.RED + "Could not give new player kit because " + kitName + " does not exist.");
			return;
		}
		Kit kit = optionalKit.get();

		
		KitUser kitUser = new KitUser(player, kit);
		try {
			kitUser.applyKit();
			Notifications notify = new Notifications("New-Player-Kit", null, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
		} catch (Exception e) {
			Main.getPlugin().getLogger().severe(e.getMessage());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerClickKitEvent(InventoryClickEvent event){
		if(!event.getView().getTitle().contains("EasyKits:")){
			return;
		}

		event.setCancelled(true);
		
		Player player = (Player) event.getWhoClicked();
		if(event.getSlot() == 44){
			String kitName = event.getView().getTitle().replace("EasyKits: ", "").toLowerCase();
			Kit kit = new Kit(kitName);
			if(!kit.exists()){
				Notifications notify = new Notifications("Kit-Not-Exist", kit.getName(), player.getName(), 0, null, 0);
				player.sendMessage(notify.getMessage());
				return;
			}
			
			KitUser kitUser = new KitUser(player, kit);
			try {	
				player.closeInventory();
				kitUser.applyKit();
			} catch (Exception e) {
				Main.getPlugin().getLogger().severe(e.getMessage());
			}
		}else if(event.getSlot() == 43){
			if(event.getInventory().getItem(event.getSlot()) == null){
				return;
			}		
			if(event.getInventory().getItem(event.getSlot()).getType() != Material.BOOK){
				return;
			}
			if(!event.getInventory().getItem(event.getSlot()).hasItemMeta()){
				return;
			}
			if(!event.getInventory().getItem(event.getSlot()).getItemMeta().hasDisplayName()){
				return;
			}
			if(!event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Back")){
				return;
			}

			Book.pageOne(player);
		}

	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerClickBookEvent(InventoryClickEvent event){
		if(!event.getView().getTitle().contains("EasyKits:")){
			return;
		}

		if(event.getCurrentItem() == null){
			return;
		}

		if(event.getCurrentItem().getType() != Material.BOOK && event.getCurrentItem().hasItemMeta()){
			return;
		}
		
		if(!event.getCurrentItem().hasItemMeta()){
			return;
		}
		
		if(!event.getCurrentItem().getItemMeta().hasDisplayName()){
			return;
		}

		if(!event.getCurrentItem().getItemMeta().getDisplayName().contains("EasyKits:")){
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		String kitName = event.getCurrentItem().getItemMeta().getDisplayName().replace("EasyKits: ", "").toLowerCase();
		event.setCancelled(true);

		Kit kit = new Kit(kitName);
		
		if(!kit.exists()){
			Notifications notify = new Notifications("Kit-Not-Exist", kit.getName(), player.getName(), 0, null, 0);
			player.sendMessage(notify.getMessage());
			return;
		}
		
		Book.pageTwo(player, kit);
	
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		if(!(player.getInventory().getItemInMainHand().getType() == Material.BOOK && player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())){
			return;
		}
		
		if(!player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("EasyKits")){
			return;
		}
		
		Book.pageOne(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKitEquipEvent(KitPlayerEquipEvent event) {
		Kit kit = event.getKit();
		Player player = event.getPlayer();
		KitUser kitUser = event.getKitUser();
		
		if(!(player.hasPermission("EasyKits.kits." + kit.getName()) || player.hasPermission("EasyKits.kits.*"))){
			Notifications notify = new Notifications("Permission-Denied", null, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
			event.setCancelled(true);
			return;
		}
		
		if(!event.getPlayer().hasPermission("EasyKits.bypass.cooldown")){
			if((kitUser.getCooldownTimeRemaining() != null) && (kit.getCooldown() != null)){
				Notifications notify = new Notifications("Get-Cooldown", event.getKit().getName(), null, 0, kitUser.getCooldownTimeRemaining(), 0);
				player.sendMessage(notify.getMessage());
				event.setCancelled(true);
				return;
			}
		}

		if(!event.getPlayer().hasPermission("EasyKits.bypass.limit")){
			if((kit.getLimit() != 0) && (kitUser.getCurrentLimit() == 0)){
				Notifications notify = new Notifications("Get-Kit-Limit", kit.getName(), null, 0, null, kit.getLimit());
				player.sendMessage(notify.getMessage());
				event.setCancelled(true);
				return;
			}
		}
		
		if(!event.getPlayer().hasPermission("EasyKits.bypass.price")){
			if(!kitUser.canAfford()){
				Notifications notify = new Notifications("Insufficient-Funds", kit.getName(), null, kit.getPrice(), kitUser.getCooldownTimeRemaining(), 0);
				player.sendMessage(notify.getMessage());
				event.setCancelled(true);
			    return;
			}
		}
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateObtained = dateFormat.format(date).toString();
		kitUser.setDateObtained(dateObtained);
		
		kitUser.setCurrentLimit((kitUser.getCurrentLimit() - 1));
		
		kitUser.chargeUser();

		Notifications notify = new Notifications("Kit-Obtained", kit.getName(), null, 0, null, 0);
		player.sendMessage(notify.getMessage());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onKitCooldownEvent(KitPlayerCooldownEvent event) {
		if(event.getPlayer().hasPermission("EasyKits.bypass.cooldown")){
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onKitLimitEvent(KitPlayerLimitEvent event) {
		if(event.getPlayer().hasPermission("EasyKits.bypass.limit")){
			event.setCancelled(true);
	        return;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWithdrawMoneyEvent(WithdrawMoneyEvent event) {
		if(event.getPlayer().hasPermission("EasyKits.bypass.price")){
			event.setCancelled(true);
    		return;
		}
		if(!Main.getPlugin().supportsEconomy()){
			event.setCancelled(true);
    		return;
		}
		if(event.getKit().getPrice() == 0){
			event.setCancelled(true);
    		return;
		}
		Notifications notify = new Notifications("Get-Price", event.getKit().getName(), null, event.getKit().getPrice(), event.getKitUser().getCooldownTimeRemaining(), 0);
		event.getPlayer().sendMessage(notify.getMessage());
	}	
	
	// FIX BUKKIT STUPIDITY
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPrepareItem(final PrepareItemCraftEvent event) {
		if(!event.getInventory().contains(Book.getBook("EasyKits", "List of Kits!"))){			
			return;
		}
		String msg = ChatColor.GREEN + "[EasyKits]: " + ChatColor.GOLD + event.getViewers().get(0).getName() + " is a book cheater!";
		Main.getPlugin().getServer().broadcast(msg, "EasyKits.cmd.book");
		event.getInventory().setResult(new ItemStack(Material.AIR));
	}

}
