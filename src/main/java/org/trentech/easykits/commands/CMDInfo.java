package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;
import org.trentech.easykits.utils.Utils;

public class CMDInfo {
	
    public static void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("easykits.cmd.info")) {
            Notifications notify = new Notifications("permission-denied");
            sender.sendMessage(notify.getMessage());
            return;
        }
        
        if (args.length == 2) {
            KitService kitService = KitService.instance();

            Optional<Kit> optional = kitService.getKit(args[1]);

            if (!optional.isPresent()) {
                Notifications notify = new Notifications("kit-not-exist", args[1]);
                sender.sendMessage(notify.getMessage());
                return;
            }
            Kit kit = optional.get();

            sender.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.YELLOW + kit.getName());

            if (kit.getPrice() > 0.0D) {
                sender.sendMessage(ChatColor.GREEN + "Price: " + ChatColor.YELLOW + "$" + kit.getPrice());
            }

            if (kit.getLimit() > 0) {
                sender.sendMessage(ChatColor.GREEN + "Limit: " + ChatColor.YELLOW + kit.getLimit());
            }

            if (kit.getCooldown() > 0L) {
                sender.sendMessage(ChatColor.GREEN + "Cooldown: " + ChatColor.YELLOW + Utils.getReadableTime(kit.getCooldown()));
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "/kit info <kitname>");
        }
    }
}