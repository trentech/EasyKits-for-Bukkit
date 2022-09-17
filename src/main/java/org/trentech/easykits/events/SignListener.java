package org.trentech.easykits.events;

import java.util.ArrayList;
import java.util.Optional;

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
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class SignListener implements Listener {
	
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Kit]")) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.hasPermission("easykits.sign.use")) {
            Notifications notify = new Notifications("permission-denied");
            player.sendMessage(notify.getMessage());
            event.setCancelled(true);

            return;
        }
        
        String kitName = ChatColor.stripColor(sign.getLine(1));

        Optional<Kit> optionalKit = KitService.instance().getKit(kitName);

        if (!optionalKit.isPresent()) {
            Notifications notify = new Notifications("kit-not-exist", kitName);
            player.sendMessage(notify.getMessage());
            return;
        }
        Kit kit = optionalKit.get();

        if (Main.getPlugin().getConfig().getString("config.sign-action").equalsIgnoreCase("view")) {
            ItemStack[] inv = kit.getInventory();
            ItemStack[] arm = kit.getEquipment();

            Inventory showInv = Main.getPlugin().getServer().createInventory(player, 45, "EasyKits: " + kit.getName());
            showInv.setContents(inv);
            int index = 36;
            for (ItemStack a : arm) {
                showInv.setItem(index, a);
                index++;
            }
            ItemStack getKit = new ItemStack(Material.NETHER_STAR);
            ItemMeta getKitMeta = getKit.getItemMeta();
            getKitMeta.setDisplayName(ChatColor.GREEN + "Get " + kitName.toLowerCase());
            ArrayList<String> getKitLores = new ArrayList<>();
            getKitLores.add(ChatColor.DARK_PURPLE + "Click to equip kit!");
            getKitMeta.setLore(getKitLores);
            getKit.setItemMeta(getKitMeta);
            showInv.setItem(44, getKit);
            player.openInventory(showInv);
        } else if (Main.getPlugin().getConfig().getString("config.sign-action").equalsIgnoreCase("obtain")) {
            KitService.instance().setKit(player, kit, true);
        } else {
            player.sendMessage(ChatColor.DARK_RED + "ERROR: Check your config!");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignPlace(SignChangeEvent event) {
        String[] line = event.getLines();

        if (!line[0].equalsIgnoreCase("[Kit]")) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission("easykits.sign.create")) {
            Notifications notify = new Notifications("permission-denied");
            player.sendMessage(notify.getMessage());
            event.setCancelled(true);
            return;
        }
        
        String kitName = line[1];

        Optional<Kit> optionalKit = KitService.instance().getKit(kitName);

        if (!optionalKit.isPresent()) {
            Notifications notify = new Notifications("kit-not-exist", kitName);
            player.sendMessage(notify.getMessage());
            event.setCancelled(true);
            return;
        }
        Kit kit = optionalKit.get();

        event.setLine(0, ChatColor.DARK_BLUE + "[Kit]");
        event.setLine(3, "");

        double price = kit.getPrice();
        if (price > 0.0) {
            event.setLine(2, ChatColor.GREEN + Main.getPlugin().getConfig().getString("config.currency-symbol") + Double.toString(price));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getBlockData() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) event.getBlock().getState();
        String[] line = sign.getLines();

        if (!ChatColor.stripColor(line[0]).equalsIgnoreCase("[Kit]")) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.hasPermission("easykits.sign.remove")) {
            Notifications notify = new Notifications("permission-denied");
            player.sendMessage(notify.getMessage());
            event.setCancelled(true);
            return;
        }
    }
}