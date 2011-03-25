package org.mcteam.onlinelog;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OnlineLog extends JavaPlugin {
	
	public static OnlineLog instance; 
	
	private OnlineLogPlayerListener playerListener = new OnlineLogPlayerListener();
	
	public OnlineLog() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// Ensure the data folder exists
		this.getDataFolder().mkdir();
		
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener, Event.Priority.Normal, this);
	}
	
	// -------------------------------------------- //
	// Logging
	// -------------------------------------------- //
	public static void log(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void log(Level level, String msg) {
		Logger.getLogger("Minecraft").log(level, "["+instance.getDescription().getFullName()+"] "+msg);
	}

}