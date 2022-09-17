package org.trentech.easykits.utils;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.trentech.easykits.Main;

public class Notifications {
	
    private static HashMap<String, String> messages = new HashMap<>();

    private String type;
    private String kitName;
    private String playerName;
    private double price;
    private String cooldown;
    private int limit;

    public Notifications(String type) {
        this.type = type;
        this.kitName = null;
        this.playerName = null;
        this.price = 0.0;
        this.cooldown = null;
        this.limit = 0;
    }

    public Notifications(String type, String kitName) {
        this.type = type;
        this.kitName = kitName;
        this.playerName = null;
        this.price = 0.0;
        this.cooldown = null;
        this.limit = 0;
    }

    public Notifications(String type, String kitName, String playerName) {
        this.type = type;
        this.kitName = kitName;
        this.playerName = playerName;
        this.price = 0.0;
        this.cooldown = null;
        this.limit = 0;
    }

    public Notifications(String type, String kitName, String playerName, double price) {
        this.type = type;
        this.kitName = kitName;
        this.playerName = playerName;
        this.price = price;
        this.cooldown = null;
        this.limit = 0;
    }

    public Notifications(String type, String kitName, String playerName, String cooldown) {
        this.type = type;
        this.kitName = kitName;
        this.playerName = playerName;
        this.price = 0.0;
        this.cooldown = cooldown;
        this.limit = 0;
    }

    public Notifications(String type, String kitName, String playerName, int limit) {
        this.type = type;
        this.kitName = kitName;
        this.playerName = playerName;
        this.price = 0.0;
        this.cooldown = null;
        this.limit = limit;
    }

    public String getMessage() {
        String msg = null;
        if (messages.get(type) != null) {
            msg = messages.get(type);

            if (msg.contains("%K") && kitName != null) {
                msg = msg.replaceAll("%K", kitName);
            }
            if (msg.contains("%M") && price != 0.0) {
                msg = msg.replaceAll("%M", Main.getPlugin().getConfig().getString("config.currency-symbol") + Double.toString(price));
            }
            if (msg.contains("%T") && cooldown != null) {
                msg = msg.replaceAll("%T", cooldown);
            }
            if (msg.contains("%P") && playerName != null) {
                msg = msg.replaceAll("%P", playerName);
            }
            if (msg.contains("%L") && limit != 0) {
                msg = msg.replaceAll("%L", Integer.toString(limit));
            }
        } else {
            throw new NullPointerException("Message Missing from Config!");
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void init() {
        FileConfiguration config = Main.getPlugin().getConfig();

        messages.put("permission-denied", config.getString("messages.permission-denied"));
        messages.put("kit-created", config.getString("messages.kit-created"));
        messages.put("kit-deleted", config.getString("messages.kit-deleted"));
        messages.put("kit-obtained", config.getString("messages.kit-obtained"));
        messages.put("kit-sent", config.getString("messages.kit-sent"));
        messages.put("kit-received", config.getString("messages.kit-received"));
        messages.put("kit-exist", config.getString("messages.kit-exist"));
        messages.put("kit-not-exist", config.getString("messages.kit-not-exist"));
        messages.put("get-kit-limit", config.getString("messages.get-kit-limit"));
        messages.put("set-kit-limit", config.getString("messages.set-kit-limit"));
        messages.put("reset-kit-limit", config.getString("messages.reset-kit-limit"));
        messages.put("get-cooldown", config.getString("messages.get-cooldown"));
        messages.put("set-cooldown", config.getString("messages.set-cooldown"));
        messages.put("reset-cooldown", config.getString("messages.reset-cooldown"));
        messages.put("get-price", config.getString("messages.get-price"));
        messages.put("set-price", config.getString("messages.set-price"));
        messages.put("insufficient-funds", config.getString("messages.insufficient-funds"));
        messages.put("inventory-space", config.getString("messages.inventory-space"));
        messages.put("new-player-kit", config.getString("messages.new-player-kit"));
        messages.put("kit-book-full", config.getString("messages.kit-book-full"));
        messages.put("not-player", config.getString("messages.not-player"));
        messages.put("no-player", config.getString("messages.no-player"));
        messages.put("invalid-number", config.getString("messages.invalid-number"));
        messages.put("invalid-argument", config.getString("messages.invalid-argument"));
        messages.put("db-fail", config.getString("messages.db-fail"));
    }
}