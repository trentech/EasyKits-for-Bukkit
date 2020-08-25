package org.trentech.easykits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.trentech.easykits.utils.Notifications;

public class CMDHelp {

	public static void execute(CommandSender sender, String[] args) {
		if(args.length == 1) {
			sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Command List:\n");
			sender.sendMessage(ChatColor.YELLOW + "/kit -or- /k");
			sender.sendMessage(ChatColor.YELLOW + "/kit [kitname]");	
			sender.sendMessage(ChatColor.YELLOW + "/kit help [command]");
			if(sender.hasPermission("easykits.cmd.reload")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit reload");
			}
			if(sender.hasPermission("easyKits.cmd.create")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit create <kitname>");
			}
			if(sender.hasPermission("easyKits.cmd.remove")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit remove <kitname>");
			}
			if(sender.hasPermission("easyKits.cmd.limit")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit limit <kitname> <cooldown>");
			}
			if(sender.hasPermission("easyKits.cmd.cooldown")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit cooldown <kitname> <cooldown>");
			}
			if(sender.hasPermission("easyKits.cmd.price")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit price <kitname> <price>");
			}
			if(sender.hasPermission("easyKits.cmd.reset")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit reset [cooldown | limit] <kitname> <player>");
			}
			if(sender.hasPermission("easyKits.cmd.give")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit give <kitname> <player>");
			}
			if(sender.hasPermission("easykits.cmd.give")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit book");
			}
			if(sender.hasPermission("easykits.cmd.view")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit view <kitname>");
			}
			if(sender.hasPermission("easykits.cmd.list")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit list");
			}
			if(sender.hasPermission("easykits.cmd.info")) {
				sender.sendMessage(ChatColor.YELLOW + "/kit info <kitname>");
			}
		}else{
			switch(args[1]) {
			case "reload":
				if(!sender.hasPermission("easykits.cmd.reload")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Reloads the Plugin.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit reload");
				break;
			case "create":
				if(!sender.hasPermission("easykits.cmd.create")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Creates a new Kit.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit create PVP");
				break;
			case "remove":
				if(!sender.hasPermission("easykits.cmd.remove")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Deletes a Kit.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit remove PVP");
				break;
			case "limit":
				if(!sender.hasPermission("easykits.cmd.limit")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Limits the number of times kit can be obtained by player. Set 0 to disable");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit limit PVP 5");
				break;
			case "cooldown":
				if(!sender.hasPermission("easykits.cmd.cooldown")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Cooldown is the amount of time between player use. Once\n    player obtains kit, they will wait a set amount of time before\n    they can access that kit again.");
				sender.sendMessage(ChatColor.GOLD + "    Time format is [week][day][hour][minute][second]\n    or simply 0 to disable");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit cooldown PVP 1d,7h,6m");
				sender.sendMessage(ChatColor.YELLOW + "    /kit cooldown PVP 3d,0h,2m,24s");
				sender.sendMessage(ChatColor.YELLOW + "    /kit cooldown PVP 2w");
				sender.sendMessage(ChatColor.YELLOW + "    /kit cooldown PVP 0");
				break;
			case "price":
				if(!sender.hasPermission("easyKits.cmd.price")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Amount of money player pays to obtain kit.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit price PVP 10");
				sender.sendMessage(ChatColor.YELLOW + "    /kit price PVP 100.55");
				break;
			case "reset":
				if(!sender.hasPermission("easyKits.cmd.reset")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Resets the cooldown or limit on a kit for a specified player.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit reset cooldown PVP Notch");
				sender.sendMessage(ChatColor.YELLOW + "    /kit reset limit PVP Notch");
				break;
			case "give":
				if(!sender.hasPermission("easykits.cmd.give")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Give kit to a specified player.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit give PVP Notch");
				break;
			case "book":
				if(!sender.hasPermission("easykits.cmd.book")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Gives player book with list of kits.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit book");
				break;
			case "view":
				if(!sender.hasPermission("easykits.cmd.view")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    View Kit Contents.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit view PVP");
				break;
			case "list":
				if(!sender.hasPermission("easykits.cmd.list")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    List of Available Kits.");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit list");
				break;
			case "info":
				if(!sender.hasPermission("easykits.cmd.info")) {
					Notifications notify = new Notifications("permission-denied");
					sender.sendMessage(notify.getMessage());
					break;
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Description:\n");
				sender.sendMessage(ChatColor.YELLOW + "    Show limit, cooldown and price of kit");
				sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "Example:\n");
				sender.sendMessage(ChatColor.YELLOW + "    /kit info PVP");
				break;
			default:
				Notifications notify = new Notifications("invalid-argument");
				sender.sendMessage(notify.getMessage());
			}
		}
	}

}
