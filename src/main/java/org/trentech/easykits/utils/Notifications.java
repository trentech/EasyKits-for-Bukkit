package org.trentech.easykits.utils;

import org.bukkit.ChatColor;
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
	
	public Notifications(String type, String kitName, String playerName, double price, String cooldown, int limit) {
		this.type = type;
		this.kitName = kitName;
		this.playerName = playerName;
		this.price = price;
		this.cooldown = cooldown;
		this.limit = limit;	
	}
	
	public void getMessages() {
		Main.getMessages().put("Permission-Denied", Main.getPlugin().getConfig().getString("Messages.Permission-Denied"));
		Main.getMessages().put("Kit-Created", Main.getPlugin().getConfig().getString("Messages.Kit-Created"));
		Main.getMessages().put("Kit-Deleted", Main.getPlugin().getConfig().getString("Messages.Kit-Deleted"));
		Main.getMessages().put("Kit-Obtained", Main.getPlugin().getConfig().getString("Messages.Kit-Obtained"));
		Main.getMessages().put("Kit-Sent", Main.getPlugin().getConfig().getString("Messages.Kit-Sent"));
		Main.getMessages().put("Kit-Received", Main.getPlugin().getConfig().getString("Messages.Kit-Received"));
		Main.getMessages().put("Kit-Exist", Main.getPlugin().getConfig().getString("Messages.Kit-Exist"));
		Main.getMessages().put("Kit-Not-Exist", Main.getPlugin().getConfig().getString("Messages.Kit-Not-Exist"));
		Main.getMessages().put("Get-Kit-Limit", Main.getPlugin().getConfig().getString("Messages.Get-Kit-Limit"));
		Main.getMessages().put("Set-Kit-Limit", Main.getPlugin().getConfig().getString("Messages.Set-Kit-Limit"));
		Main.getMessages().put("Reset-Kit-Limit", Main.getPlugin().getConfig().getString("Messages.Reset-Kit-Limit"));
		Main.getMessages().put("Get-Cooldown", Main.getPlugin().getConfig().getString("Messages.Get-Cooldown"));
		Main.getMessages().put("Set-Cooldown", Main.getPlugin().getConfig().getString("Messages.Set-Cooldown"));
		Main.getMessages().put("Reset-Cooldown", Main.getPlugin().getConfig().getString("Messages.Reset-Cooldown"));
		Main.getMessages().put("Get-Price", Main.getPlugin().getConfig().getString("Messages.Get-Price"));
		Main.getMessages().put("Set-Price", Main.getPlugin().getConfig().getString("Messages.Set-Price"));
		Main.getMessages().put("Insufficient-Funds", Main.getPlugin().getConfig().getString("Messages.Insufficient-Funds"));
		Main.getMessages().put("Inventory-Space", Main.getPlugin().getConfig().getString("Messages.Inventory-Space"));
		Main.getMessages().put("New-Player-Kit", Main.getPlugin().getConfig().getString("Messages.New-Player-Kit"));
		Main.getMessages().put("Kit-Book-Full", Main.getPlugin().getConfig().getString("Messages.Kit-Book-Full"));
		Main.getMessages().put("Not-Player", Main.getPlugin().getConfig().getString("Messages.Not-Player"));
		Main.getMessages().put("No-Player", Main.getPlugin().getConfig().getString("Messages.No-Player"));
		Main.getMessages().put("Invalid-Number", Main.getPlugin().getConfig().getString("Messages.Invalid-Number"));
		Main.getMessages().put("Invalid-Argument", Main.getPlugin().getConfig().getString("Messages.Invalid-Argument"));
		Main.getMessages().put("DB-Fail", Main.getPlugin().getConfig().getString("Messages.DB-Fail"));
	}
	
	public String getMessage() {
		String msg = null;
		if(Main.getMessages().get(type) != null) {
			msg = Main.getMessages().get(type);
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
