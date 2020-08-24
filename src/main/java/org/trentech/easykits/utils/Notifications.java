package org.trentech.easykits.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.trentech.easykits.Main;

public class Notifications {
	
	private String type;
	private String kitName;
	private String playerName;
	private double price;
	private String cooldown;
	private int limit;
	
	public Notifications() {
		
	}
	
	public Notifications(String type) {
		this.type = type;
		this.kitName = null;
		this.playerName = null;
		this.price = 0;
		this.cooldown = null;
		this.limit = 0;	
	}
	
	public Notifications(String type, String kitName) {
		this.type = type;
		this.kitName = kitName;
		this.playerName = null;
		this.price = 0;
		this.cooldown = null;
		this.limit = 0;	
	}
	
	public Notifications(String type, String kitName, Player player) {
		this.type = type;
		this.kitName = kitName;
		this.playerName = player.getName();
		this.price = 0;
		this.cooldown = null;
		this.limit = 0;	
	}
	
	public Notifications(String type, String kitName, String playerName, double price, String cooldown, int limit) {
		this.type = type;
		this.kitName = kitName;
		this.playerName = playerName;
		this.price = price;
		this.cooldown = cooldown;
		this.limit = limit;	
	}
	
	public void getMessages() {
		Main.getPlugin().getMessages().put("permission-denied", Main.getPlugin().getConfig().getString("messages.permission-denied"));
		Main.getPlugin().getMessages().put("kit-created", Main.getPlugin().getConfig().getString("messages.kit-created"));
		Main.getPlugin().getMessages().put("kit-deleted", Main.getPlugin().getConfig().getString("messages.kit-deleted"));
		Main.getPlugin().getMessages().put("kit-obtained", Main.getPlugin().getConfig().getString("messages.kit-obtained"));
		Main.getPlugin().getMessages().put("kit-sent", Main.getPlugin().getConfig().getString("messages.kit-sent"));
		Main.getPlugin().getMessages().put("kit-received", Main.getPlugin().getConfig().getString("messages.kit-received"));
		Main.getPlugin().getMessages().put("kit-exist", Main.getPlugin().getConfig().getString("messages.kit-exist"));
		Main.getPlugin().getMessages().put("kit-not-exist", Main.getPlugin().getConfig().getString("messages.kit-not-exist"));
		Main.getPlugin().getMessages().put("get-kit-limit", Main.getPlugin().getConfig().getString("messages.get-kit-limit"));
		Main.getPlugin().getMessages().put("set-kit-limit", Main.getPlugin().getConfig().getString("messages.set-kit-limit"));
		Main.getPlugin().getMessages().put("reset-kit-limit", Main.getPlugin().getConfig().getString("messages.reset-kit-limit"));
		Main.getPlugin().getMessages().put("get-cooldown", Main.getPlugin().getConfig().getString("messages.get-cooldown"));
		Main.getPlugin().getMessages().put("set-cooldown", Main.getPlugin().getConfig().getString("messages.set-cooldown"));
		Main.getPlugin().getMessages().put("reset-cooldown", Main.getPlugin().getConfig().getString("messages.reset-cooldown"));
		Main.getPlugin().getMessages().put("get-price", Main.getPlugin().getConfig().getString("messages.get-price"));
		Main.getPlugin().getMessages().put("set-price", Main.getPlugin().getConfig().getString("messages.set-price"));
		Main.getPlugin().getMessages().put("insufficient-funds", Main.getPlugin().getConfig().getString("messages.insufficient-funds"));
		Main.getPlugin().getMessages().put("inventory-space", Main.getPlugin().getConfig().getString("messages.inventory-space"));
		Main.getPlugin().getMessages().put("new-player-kit", Main.getPlugin().getConfig().getString("messages.new-player-kit"));
		Main.getPlugin().getMessages().put("kit-book-full", Main.getPlugin().getConfig().getString("messages.kit-book-full"));
		Main.getPlugin().getMessages().put("not-player", Main.getPlugin().getConfig().getString("messages.not-player"));
		Main.getPlugin().getMessages().put("no-player", Main.getPlugin().getConfig().getString("messages.no-player"));
		Main.getPlugin().getMessages().put("invalid-number", Main.getPlugin().getConfig().getString("messages.invalid-number"));
		Main.getPlugin().getMessages().put("invalid-argument", Main.getPlugin().getConfig().getString("messages.invalid-argument"));
		Main.getPlugin().getMessages().put("db-fail", Main.getPlugin().getConfig().getString("messages.db-fail"));
	}
	
	public String getMessage() {
		String msg = null;
		if(Main.getPlugin().getMessages().get(type) != null) {
			msg = Main.getPlugin().getMessages().get(type);
			if(msg.contains("%K") && kitName != null) {
				msg = msg.replaceAll("%K", kitName);
			}
			if(msg.contains("%M") && price != 0) {
				msg = msg.replaceAll("%M", Double.toString(price));
			}
			if(msg.contains("%T") && cooldown != null) {
				msg = msg.replaceAll("%T", cooldown);
			}
			if(msg.contains("%P") && playerName != null) {
				msg = msg.replaceAll("%P", playerName);
			}
			if(msg.contains("%L") && limit != 0) {
				msg = msg.replaceAll("%L", Integer.toString(limit));
			}
		}else{
			throw new NullPointerException("Message Missing from Config!");
		}
		
		String message = ChatColor.translateAlternateColorCodes('&', msg);
		return message;
	}
}
