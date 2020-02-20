package org.trentech.easykits.events;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitUser;
import org.trentech.easykits.utils.Notifications;

public class SignListener implements Listener{

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignUse(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		if(!(event.getClickedBlock().getBlockData() instanceof Sign)) {
			return;
		}
		
		Sign sign = (Sign) event.getClickedBlock().getState();
		String[] line = sign.getLines();
		String kitSign = ChatColor.DARK_BLUE + "[Kit]";
		
		if (!line[0].equalsIgnoreCase(kitSign)) {
			return;
		}
        
		Player player = event.getPlayer();
		
		if(!player.hasPermission("EasyKits.sign.use")) {
			Notifications notify = new Notifications("Permission-Denied", null, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
			event.setCancelled(true);
			return;
		}

		String kitName = line[1];
		Kit kit = new Kit(kitName);
		if (!kit.exists()) {
			Notifications notify = new Notifications("Kit-Not-Exist", kitName, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
			return;
		}

		if(Main.getPlugin().getConfig().getString("Config.Sign-Action").equalsIgnoreCase("view")) {
			ItemStack[] inv = kit.getInventory();
			ItemStack[] arm = kit.getArmor();

			Inventory showInv = Main.getPlugin().getServer().createInventory(player, 45, "EasyKits Kit: " + kit.getName());
			showInv.setContents(inv);								
			int index = 36;
			for(ItemStack a : arm){
				showInv.setItem(index, a);
				index++;
			}	
			ItemStack getKit = new ItemStack(Material.NETHER_STAR);
			ItemMeta getKitMeta = getKit.getItemMeta();
			getKitMeta.setDisplayName(ChatColor.GREEN + "Get " + kitName.toLowerCase());
			ArrayList<String> getKitLores = new ArrayList<String>();
			getKitLores.add(ChatColor.DARK_PURPLE+ "Click to equip kit!");
			getKitMeta.setLore(getKitLores);
			getKit.setItemMeta(getKitMeta);								
			showInv.setItem(44, getKit);
			player.openInventory(showInv);
			
		}else if(Main.getPlugin().getConfig().getString("Config.Sign-Action").equalsIgnoreCase("obtain")) {

			KitUser kitUser = new KitUser(player, kit);
			try {
				kitUser.applyKit();
			} catch (Exception e) {
				Main.getPlugin().getLogger().equals(e.getMessage());
			}
		}else{
			player.sendMessage(ChatColor.DARK_RED + "ERROR: Check your config!");
		}

	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignPlace(SignChangeEvent event) {
		String[] line = event.getLines();

		String kitSign = "[Kit]";
		if (!line[0].equalsIgnoreCase(kitSign)) {
			return;
		}

		Player player = event.getPlayer();
		if(!player.hasPermission("EasyKits.sign.create")) {
			Notifications notify = new Notifications("Permission-Denied", null, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
			event.setCancelled(true);
			return;
		}

		String kitName = line[1];
		Kit kit = new Kit(kitName);
		if (!kit.exists()) {
			Notifications notify = new Notifications("Kit-Not-Exist", kitName, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
			event.setCancelled(true);
			return;
		}

		String newLine = ChatColor.DARK_BLUE + kitSign;
		event.setLine(0, newLine);					
		event.setLine(3, null);
		
		double price = kit.getPrice();
		if(price > 0) {
			event.setLine(2, ChatColor.GREEN + Double.toString(price));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignBreak(BlockBreakEvent event) {
		if (!(event.getBlock().getBlockData() instanceof Sign)) {
			return;
		}
		
		Sign sign = (Sign) event.getBlock().getState();
		String[] line = sign.getLines();
		String kitSign = ChatColor.DARK_BLUE + "[Kit]";
		
		if (!line[0].equalsIgnoreCase(kitSign)){
			return;
		}
        
		Player player = event.getPlayer();
		
		if(!player.hasPermission("EasyKits.sign.remove")) {
			Notifications notify = new Notifications("Permission-Denied", null, null, 0, null, 0);
			player.sendMessage(notify.getMessage());
			event.setCancelled(true);
			return;
		}	
	}
	
}
