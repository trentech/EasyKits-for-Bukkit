package org.trentech.easykits.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;
import org.trentech.easykits.kits.Kit;
import org.trentech.easykits.kits.KitService;
import org.trentech.easykits.kits.KitUsage;
import org.trentech.easykits.sql.SQLPlayers;
import org.trentech.easykits.utils.Notifications;

public class CMDReset {
	
    public static void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("easyKits.cmd.reset")) {
            Notifications notify = new Notifications("permission-denied");
            sender.sendMessage(notify.getMessage());
            return;
        }
        
        if (args.length == 4) {
            Player player = Main.getPlugin().getServer().getPlayerExact(args[3]);

            if (player == null || !player.isOnline()) {
                Notifications notify = new Notifications("no-player");
                sender.sendMessage(notify.getMessage());
                return;
            }
            
            String property = args[1];
            String kitName = args[2];

            Optional<Kit> optional = KitService.instance().getKit(kitName);

            if (!optional.isPresent()) {
                Notifications notify = new Notifications("kit-not-exist", kitName);
                sender.sendMessage(notify.getMessage());
                return;
            }
            Kit kit = optional.get();

            KitUsage kitUsage = SQLPlayers.get(player, kit.getName());

            if (property.equalsIgnoreCase("cooldown")) {
                try {
                    kitUsage.setDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("2000-01-01 12:00:00"));
                    SQLPlayers.save(player, kitUsage);
                    Notifications notify = new Notifications("reset-cooldown", kitName, player.getName(), "NONE");
                    sender.sendMessage(notify.getMessage());
                    player.sendMessage(notify.getMessage());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (property.equalsIgnoreCase("limit")) {
                kitUsage.setTimesUsed(0);
                SQLPlayers.save(player, kitUsage);
                Notifications notify = new Notifications("reset-kit-limit", kitName, player.getName(), 0);
                sender.sendMessage(notify.getMessage());
                player.sendMessage(notify.getMessage());
            } else {
                sender.sendMessage(ChatColor.YELLOW + "/kit reset [cooldown | limit] <kitname> <player>");
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "/kit reset [cooldown | limit] <kitname> <player>");
        }
    }
}