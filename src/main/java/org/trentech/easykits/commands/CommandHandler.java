package org.trentech.easykits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("kit") || label.equalsIgnoreCase("k") || label.equalsIgnoreCase("easykits")) {
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("reload")){
					CMDReload.execute(sender);
				}else if(args[0].equalsIgnoreCase("create")){
					CMDCreate.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("cooldown")){
					CMDCooldown.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("limit")){
					CMDLimit.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("price")){
					CMDPrice.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("remove")){
					CMDRemove.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("give")){
					CMDGive.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("book")){
					CMDBook.execute(sender);
				}else if(args[0].equalsIgnoreCase("list")){
					CMDList.execute(sender);
				}else if(args[0].equalsIgnoreCase("view")){
					CMDView.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("info")){
					CMDInfo.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("reset")){
					CMDReset.execute(sender, args);
				}else if(args[0].equalsIgnoreCase("help")){
					CMDHelp.execute(sender, args);
				}else{
					CMDKit.execute(sender, args);
				}
			}else{
				CMDKit.execute(sender, args);
			}
		}
		return true;
	}
	
}
