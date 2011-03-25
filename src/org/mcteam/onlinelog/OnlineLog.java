package org.mcteam.onlinelog;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class OnlineLog extends JavaPlugin {
	public static OnlineLog instance; 
	private OnlineLogPlayerListener playerListener = new OnlineLogPlayerListener();
	
	public static Map<String, String> dbSettings = new LinkedHashMap<String, String>();
	public static File dbSettingsFile;
    public static Connection conn;
	
	public OnlineLog() {
		instance = this;
		dbSettings.put("hostname", "localhost");
		dbSettings.put("port", "3306");
		dbSettings.put("username", "root");
		dbSettings.put("password", "");
		dbSettings.put("database", "minecraft");
		dbSettings.put("table", "onlinelog");
	}
	
	@Override
	public void onDisable() {
		if (conn != null) {
			OnlineLog.doOnlineLog(0, "");
		}
		log("Disabled");
	}

	@Override
	public void onEnable() {
		if (init()) {
			log("Enabled");
		} else {
			log("I failed to init and will now suicide :O");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
        
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener, Event.Priority.Normal, this);
	}
	
	public boolean init() {
		// Where is the config file?
		dbSettingsFile = new File(getDataFolder(), "config.yml");
		
		// Create default file if it does not exist
		if ( ! dbSettingsFile.exists()) {
			log("No conf.yml found. Creating default one.");
			
			// Ensure the data folder exists
			getDataFolder().mkdir();
			
			String content = "";
			for (Entry<String, String> setting : dbSettings.entrySet()) {
				content += setting.getKey()+": '"+setting.getValue()+"'\n";
			}
			
			// Write the conf.yml to disc
			try {
				DiscUtil.write(dbSettingsFile, content);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		// Load Configuration
		Configuration conf;
        try {
        	conf = new Configuration(dbSettingsFile);
        	conf.load();
        } catch (Exception e) {
            log(Level.WARNING, "Failed to retrieve configuration file: "+e);
            return false;
        }
        
        // Load values from it
        for (String key : dbSettings.keySet()) {
        	dbSettings.put(key, conf.getString(key));
        	if (dbSettings.get(key) == null) {
        		log(Level.WARNING, key+" missing in "+dbSettingsFile);
        		return false;
        	}
        }
        
        // Connect to the database
        String dsn = "jdbc:mysql://" + dbSettings.get("hostname") + ":" + dbSettings.get("port") + "/" + dbSettings.get("database");
        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	
            if(dbSettings.get("username").equalsIgnoreCase("") && dbSettings.get("password").equalsIgnoreCase("")) {
            	conn = DriverManager.getConnection(dsn);	
            } else {
            	conn = DriverManager.getConnection(dsn, dbSettings.get("username"), dbSettings.get("password"));
            }
            log("Connected to \""+dsn+"\" using table \""+dbSettings.get("table")+"\"");
        } catch (Exception e) {
            log(Level.WARNING, "DB connection failed: "+e);
            return false;
        }
        
        return true;
	}
	
	public static void doOnlineLog(int playercount, String playernames) {
		try {
			Statement statement = conn.createStatement();
			String query = "INSERT INTO `" + dbSettings.get("table") + "` (playercount, playernames) VALUES ('"+playercount+"', '"+playernames+"')";
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void logCurrentState() {
		Player[] onlinePlayers = OnlineLog.instance.getServer().getOnlinePlayers();
		
		String playernames = "";
		
		if (onlinePlayers.length > 0) {
			playernames += " ";
			for (Player player : onlinePlayers) {
				playernames += player.getName() + " ";
			}
		}
		
		doOnlineLog(onlinePlayers.length, playernames);
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