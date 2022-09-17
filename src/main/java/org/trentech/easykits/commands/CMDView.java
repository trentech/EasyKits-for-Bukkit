package org.trentech.easykits.commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Book;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.utils.Notifications;

public class CMDView {
	
    public static void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Notifications notify = new Notifications("not-player");
            sender.sendMessage(notify.getMessage());
            return;
        }
        
        Player player = (Player) sender;
        
        if (!sender.hasPermission("easyKits.cmd.view")) {
            Notifications notify = new Notifications("permission-denied");
            sender.sendMessage(notify.getMessage());
            return;
        }
        
        if (args.length == 2) {
            Optional<Kit> optional = KitService.instance().getKit(args[1]);

            if (!optional.isPresent()) {
                Notifications notify = new Notifications("kit-not-exist", args[1]);
                sender.sendMessage(notify.getMessage());
                return;
            }
            Kit kit = optional.get();

            Book.pageTwo(player, kit);
        } else {
            sender.sendMessage(ChatColor.YELLOW + "/kit view <kitname>");
        }
    }
}