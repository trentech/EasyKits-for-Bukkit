package org.trentech.easykits.events;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.kits.KitUsage;
import org.trentech.easykits.sql.SQLPlayers;
import org.trentech.easykits.utils.Notifications;
import org.trentech.easykits.utils.Utils;

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
		
		Optional<Kit> optionalKit = KitService.instance().getKit(kitName);
		
		if(!optionalKit.isPresent()) {
			Main.getPlugin().getLogger().warning(ChatColor.RED + "Could not give new player kit because " + kitName + " does not exist.");
			return;
		}
		Kit kit = optionalKit.get();

		KitService.instance().setKit(player, kit, true);
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

			Optional<Kit> optionalKit = KitService.instance().getKit(kitName);
			if(!optionalKit.isPresent()){
				Notifications notify = new Notifications("Kit-Not-Exist", kitName, player.getName(), 0, null, 0);
				player.sendMessage(notify.getMessage());
				return;
			}
			Kit kit = optionalKit.get();
			
			player.closeInventory();
			
			KitService.instance().setKit(player, kit, true);
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

		Optional<Kit> optionalKit = KitService.instance().getKit(kitName);
		if(!optionalKit.isPresent()){
			Notifications notify = new Notifications("Kit-Not-Exist", kitName, player.getName(), 0, null, 0);
			player.sendMessage(notify.getMessage());
			return;
		}
		Kit kit = optionalKit.get();

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

	@EventHandler
	public void onKitEventEventGet(KitEvent.Get event) {
		Player player = event.getPlayer();
		Kit kit = event.getKit();
		
		if(!player.hasPermission("easykits.kit." + kit.getName())) {
			player.sendMessage(ChatColor.RED + "You do not have permission to get " + kit.getName());
			event.setCancelled(true);
			return;
		}

		KitUsage kitUsage;
		Optional<KitUsage> optionalKitUsage = SQLPlayers.getKitUsage(player, kit.getName());
		if(optionalKitUsage.isPresent()) {
			kitUsage = optionalKitUsage.get();
		} else {
			kitUsage = new KitUsage(kit.getName());
		}

		if(!player.hasPermission("easykits.override.cooldown")) {
			Date date = kitUsage.getDate();
			
			long timeSince = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - date.getTime());
			long waitTime = kit.getCooldown();
			
			if(waitTime - timeSince > 0) {	
				player.sendMessage(ChatColor.RED + "You must wait " + Utils.getReadableTime(waitTime - timeSince));
				event.setCancelled(true);
				return;
			}
		}

		if(!player.hasPermission("easykits.override.limit")) {
			if(kit.getLimit() > 0) {
				if(kitUsage.getTimesUsed() >= kit.getLimit()) {
					player.sendMessage(ChatColor.RED + "You've reached the max number of this kit you can get.");
					event.setCancelled(true);
					return;
				}
			}
		}
		
		if(!player.hasPermission("easykits.override.price")) {
			if(kit.getPrice() > 0){				
				if(Main.getPlugin().getEconomy().getBalance(player) < kit.getPrice()){
					player.sendMessage(ChatColor.RED + "You do not have enough money. Requires " + Main.getPlugin().getConfig().getString("config.currency-symbol") + kit.getPrice());
					event.setCancelled(true);
					return;
				}
				
				Main.getPlugin().getEconomy().withdrawPlayer(player, kit.getPrice());
			}
		}
	}
	
//	@EventHandler(priority = EventPriority.MONITOR)
//	public void onKitCooldownEvent(KitPlayerCooldownEvent event) {
//		if(event.getPlayer().hasPermission("EasyKits.bypass.cooldown")){
//			event.setCancelled(true);
//			return;
//		}
//	}
//	
//	@EventHandler(priority = EventPriority.MONITOR)
//	public void onKitLimitEvent(KitPlayerLimitEvent event) {
//		if(event.getPlayer().hasPermission("EasyKits.bypass.limit")){
//			event.setCancelled(true);
//	        return;
//		}
//	}
//	
//	@EventHandler(priority = EventPriority.MONITOR)
//	public void onWithdrawMoneyEvent(WithdrawMoneyEvent event) {
//		if(event.getPlayer().hasPermission("EasyKits.bypass.price")){
//			event.setCancelled(true);
//    		return;
//		}
//		if(!Main.getPlugin().supportsEconomy()){
//			event.setCancelled(true);
//    		return;
//		}
//		if(event.getKit().getPrice() == 0){
//			event.setCancelled(true);
//    		return;
//		}
//		Notifications notify = new Notifications("Get-Price", event.getKit().getName(), null, event.getKit().getPrice(), event.getKitUser().getCooldownTimeRemaining(), 0);
//		event.getPlayer().sendMessage(notify.getMessage());
//	}	
	
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
