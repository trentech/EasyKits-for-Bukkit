package org.trentech.easykits.events;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
    public void onPlayerLoginEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        SQLPlayers.createTable(player);

        if (player.hasPlayedBefore()) {
            return;
        }

        String kitName = Main.getPlugin().getConfig().getString("config.new-player-kit");

        if (kitName.equalsIgnoreCase("NONE")) {
            return;
        }

        Optional<Kit> optionalKit = KitService.instance().getKit(kitName);

        if (!optionalKit.isPresent()) {
            Main.getPlugin().getLogger().warning(ChatColor.RED + "Could not give new player kit because " + kitName + " does not exist.");
            return;
        }
        Kit kit = optionalKit.get();

        if (!player.hasPermission("easykits.kits." + kitName)) {
            Main.getPlugin().getLogger().warning(ChatColor.RED + "Could not give new player kit because " + player.getName() + " does not have permission.");
            return;
        }
        
        KitService.instance().setKit(player, kit, false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        String kitName = Main.getPlugin().getConfig().getString("config.death-kit");

        if (kitName.equalsIgnoreCase("NONE")) {
            return;
        }

        Optional<Kit> optionalKit = KitService.instance().getKit(kitName);

        if (!optionalKit.isPresent()) {
            Main.getPlugin().getLogger().warning(ChatColor.RED + "Could not give death kit because " + kitName + " does not exist.");
            return;
        }
        Kit kit = optionalKit.get();

        if (!player.hasPermission("easykits.kits." + kitName)) {
            Main.getPlugin().getLogger().warning(ChatColor.RED + "Could not give death kit because " + player.getName() + " does not have permission.");
            return;
        }
        
        KitService.instance().setKit(player, kit, false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerClickKitEvent(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("EasyKits:")) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == 44) {
            String kitName = event.getView().getTitle().replace("EasyKits: ", "").toLowerCase();

            Optional<Kit> optionalKit = KitService.instance().getKit(kitName);
            if (!optionalKit.isPresent()) {
                Notifications notify = new Notifications("kit-not-exist", kitName, player.getName());
                player.sendMessage(notify.getMessage());
                return;
            }
            Kit kit = optionalKit.get();

            player.closeInventory();

            KitService.instance().setKit(player, kit, true);
        } else if (event.getSlot() == 43) {
            if (event.getInventory().getItem(event.getSlot()) == null) {
                return;
            }
            if (event.getInventory().getItem(event.getSlot()).getType() != Material.BOOK) {
                return;
            }
            if (!event.getInventory().getItem(event.getSlot()).hasItemMeta()) {
                return;
            }
            if (!event.getInventory().getItem(event.getSlot()).getItemMeta().hasDisplayName()) {
                return;
            }
            if (!event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Back")) {
                return;
            }

            Book.pageOne(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerClickBookEvent(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("EasyKits:")) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().getType() != Material.BOOK && event.getCurrentItem().hasItemMeta()) {
            return;
        }

        if (!event.getCurrentItem().hasItemMeta()) {
            return;
        }

        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) {
            return;
        }

        if (!event.getCurrentItem().getItemMeta().getDisplayName().contains("EasyKits:")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String kitName = event.getCurrentItem().getItemMeta().getDisplayName().replace("EasyKits: ", "").toLowerCase();
        event.setCancelled(true);

        Optional<Kit> optionalKit = KitService.instance().getKit(kitName);
        if (!optionalKit.isPresent()) {
            Notifications notify = new Notifications("kit-not-exist", kitName, player.getName());
            player.sendMessage(notify.getMessage());
            return;
        }
        Kit kit = optionalKit.get();

        KitEvent.View e = new KitEvent.View(player, kit);

        Bukkit.getServer().getPluginManager().callEvent(e);

        if (!e.isCancelled()) {
            Book.pageTwo(player, kit);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (player.getInventory().getItemInMainHand().getType() != Material.BOOK || !player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
            return;
        }

        if (!player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("EasyKits")) {
            return;
        }

        Book.pageOne(player);
    }

    @EventHandler
    public void onKitEventEventGet(KitEvent.Get event) {
        if (!event.doChecks()) {
            return;
        }

        Player player = event.getPlayer();
        Kit kit = event.getKit();

        if (!player.hasPermission("easykits.kits." + kit.getName())) {
            player.sendMessage(ChatColor.RED + "You do not have permission to get " + kit.getName());
            event.setCancelled(true);
            return;
        }
        
        KitUsage kitUsage = SQLPlayers.get(player, kit.getName());

        if (!player.hasPermission("easykits.override.cooldown") && kit.getCooldown() > 0L) {
            Date date = kitUsage.getDate();

            long timeSince = TimeUnit.MILLISECONDS.toSeconds((new Date()).getTime() - date.getTime());
            long waitTime = kit.getCooldown();

            if (waitTime - timeSince > 0L) {
                player.sendMessage(ChatColor.RED + "You must wait " + Utils.getReadableTime(waitTime - timeSince));
                event.setCancelled(true);
                return;
            }
        }

        if (!player.hasPermission("easykits.override.limit") && kit.getLimit() > 0 && kitUsage.getTimesUsed() >= kit.getLimit()) {
            player.sendMessage(ChatColor.RED + "You've reached the max number of this kit you can get.");
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission("easykits.override.price") && kit.getPrice() > 0.0D && Main.getPlugin().getEconomy() != null && Main.getPlugin().getEconomy().getBalance((OfflinePlayer) player) < kit.getPrice()) {
            player.sendMessage(ChatColor.RED + "You do not have enough money. Requires " + Main.getPlugin().getConfig().getString("config.currency-symbol") + kit.getPrice());
            event.setCancelled(true);
            return;
        }
    }

    // FIX BUKKIT STUPIDITY
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareItem(PrepareItemCraftEvent event) {
        if (!event.getInventory().contains(Book.getBook("EasyKits", "List of Kits!"))) {
            return;
        }
        String msg = ChatColor.GREEN + "[EasyKits]: " + ChatColor.GOLD + event.getViewers().get(0).getName() + " is a book cheater!";
        Main.getPlugin().getServer().broadcast(msg, "easyKits.cmd.book");
        event.getInventory().setResult(new ItemStack(Material.AIR));
    }
}