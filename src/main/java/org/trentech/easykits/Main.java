package org.trentech.easykits;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.trentech.easykits.commands.CMDKit;
import org.trentech.easykits.commands.CommandHandler;
import org.trentech.easykits.events.MainListener;
import org.trentech.easykits.events.SignListener;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.utils.Notifications;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Main plugin;
	private static HashMap<String, String> messages = new HashMap<String, String>();

	private CommandHandler cmdExecutor;
	private Economy economy;

    @Override
    public void onEnable(){
    	plugin = this;

    	registerEvents(this, new MainListener(), new SignListener());
    	
    	getConfig().options().copyDefaults(true);
    	saveConfig();
	
		new Notifications().getMessages();

		this.cmdExecutor = new CommandHandler();
		getCommand("kit").setExecutor(cmdExecutor);
		getCommand("kit").setTabCompleter(new CMDKit());
		
		if (!setupEconomy()) {
        	getLogger().warning("Vault not found! Economy support disabled!");
		}else{
			getLogger().info("Vault found! Economy support enabled!");
		}

		SQLKits.createTable();
    }

	public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}

	private boolean setupEconomy() {
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
			}
		}

		return (economy != null);
	}

	public Economy getEconomy() {
		return economy;
	}
	
	public HashMap<String, String> getMessages() {
		return messages;
	}
	
	public static Main getPlugin() {
		return plugin;
	}

}
