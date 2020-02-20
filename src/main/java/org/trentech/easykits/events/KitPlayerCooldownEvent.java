package org.trentech.easykits.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.trentech.easykits.kits.Kit;

public class KitPlayerCooldownEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private Player player;
	private Kit kit;

	public KitPlayerCooldownEvent(Player player, Kit kit) {
		this.player = player;
		this.kit = kit;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public Kit getKit() {
		return kit;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
