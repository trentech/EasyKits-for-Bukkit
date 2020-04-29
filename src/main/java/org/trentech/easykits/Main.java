package org.trentech.easykits;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.trentech.easykits.commands.CommandHandler;
import org.trentech.easykits.events.MainListener;
import org.trentech.easykits.events.SignListener;
import org.trentech.easykits.sql.SQLKits;
import org.trentech.easykits.sql.SQLUtils;
import org.trentech.easykits.utils.Notifications;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Main plugin;
	private static HashMap<String, String> messages = new HashMap<String, String>();
	
	private boolean econSupport = true;
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
		
		if (!setupEconomy()) {
        	getLogger().warning(String.format("[%s] Vault not found! Economy support disabled!", new Object[] {getDescription().getName()}));
        	econSupport = false;
		}else{
			getLogger().info(String.format("[%s] Vault found! Economy support enabled!", new Object[] { getDescription().getName() }));
		}

		try{
			SQLUtils.connect();
		}catch(Exception e){
			getLogger().severe(String.format("[%s] Disabled! Unable to connect to database!", new Object[] { getDescription().getName() }));
			return;
		}
		
		if(!SQLKits.tableExist()) {
			SQLKits.createTable();
			getLogger().info("Creating Database tables!");
		}
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

	public boolean supportsEconomy() {
		return econSupport;
	}
	
	public Economy getEconomy() {
		return economy;
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	public static HashMap<String, String> getMessages() {
		return messages;
	}
}
