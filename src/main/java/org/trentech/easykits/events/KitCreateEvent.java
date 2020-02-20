package org.trentech.easykits.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class KitCreateEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private Player creator;
	private String kitName;

	public KitCreateEvent(Player creator, String kitName) {
		this.creator = creator;
		this.kitName = kitName;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public String getName() {
		return kitName;
	}

	public Player getCreator() {
		return creator;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}