package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDPrice {
	
    public static void execute(CommandSender sender, String[] args) {   	
        if (!sender.hasPermission("easykits.cmd.price")) {
            Notifications notify = new Notifications("permission-denied");
            sender.sendMessage(notify.getMessage());
            return;
        }
        
        if (args.length == 3) {
            KitService kitService = KitService.instance();

            Optional<Kit> optional = kitService.getKit(args[1]);

            if (!optional.isPresent()) {
                Notifications notifications = new Notifications("kit-not-exist", args[1]);
                sender.sendMessage(notifications.getMessage());
                return;
            }
            Kit kit = optional.get();

            String price = args[2];
            try {
                Double.parseDouble(price);
            } catch (NumberFormatException e) {
                Notifications notifications = new Notifications("invalid-number");
                sender.sendMessage(notifications.getMessage());

                return;
            }
            kit.setPrice(Double.parseDouble(price));
            kitService.save(kit);

            Notifications notify = new Notifications("set-price", kit.getName(), sender.getName(), Double.parseDouble(price));
            sender.sendMessage(notify.getMessage());
        } else {
            sender.sendMessage(ChatColor.YELLOW + "/kit price <kitname> <price>");
        }
    }
}