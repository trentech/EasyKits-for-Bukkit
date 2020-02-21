package org.trentech.easykits.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.trentech.easykits.kits.Kit;

public class KitEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	protected Kit kit;
	protected Player player;
	protected boolean cancelled = false;

	public KitEvent(Player player, Kit kit) {
		this.player = player;
		this.kit = kit;
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

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public static class Create extends KitEvent {

		public Create(Player player, Kit kit) {
			super(player, kit);
		}		
	}
	
	public static class Delete extends KitEvent {
		
		public Delete(Player player, Kit kit) {
			super(player, kit);
		}
	}
	
	public static class View extends KitEvent {
		
		public View(Player player, Kit kit) {
			super(player, kit);
		}	
	}
	
	public static class Get extends KitEvent {
		
		protected boolean checks;
		
		public Get(Player player, Kit kit, boolean checks) {
			super(player, kit);
			this.checks = checks;
		}
		
		public boolean doChecks() {
			return checks;
		}
	}
	
	public static class Give extends KitEvent {
		
		public Give(Player player, Kit kit) {
			super(player, kit);
		}
	}
}
