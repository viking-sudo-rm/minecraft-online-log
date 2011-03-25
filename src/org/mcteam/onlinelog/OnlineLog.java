package org.mcteam.onlinelog;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class OnlineLog extends JavaPlugin {
	
	public static OnlineLog instance; 
	
	private OnlineLogPlayerListener playerListener = new OnlineLogPlayerListener();
	
    public static String hostname;
    public static String port;
    public static String username;
    public static String password;
    public static String database;
    public static String table;
    
    public static Connection conn;
	
	public OnlineLog() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		if (conn != null) {
			OnlineLog.doOnlineLog(0);
		}
		log("Disabled");
	}

	@Override
	public void onEnable() {
		if (initDbConnection()) {
			log("Database connection was established.");
		} else {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
        
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener, Event.Priority.Normal, this);
	}
	
	public boolean initDbConnection() {
		// Check file exists!
		File file = new File(getDataFolder(), "config.yml");
		if ( ! file.exists()) {
			log(Level.WARNING, "The file is missing: "+file);
			return false;
		}
		
		// Load Configuration
		Configuration conf;
        try {
        	conf = new Configuration(file);
        	conf.load();
        } catch (Exception e) {
            log(Level.WARNING, "Failed to retrieve configuration file: "+e);
            return false;
        }
        
        // Load values from it
    	hostname = conf.getString("hostname");
    	if (hostname == null) {
    		log(Level.WARNING, "hostname missing in "+file);
    		return false;
    	}
    	
    	port = conf.getString("port");
    	if (hostname == null) {
    		log(Level.WARNING, "port missing in "+file);
    		return false;
    	}
    	
    	username = conf.getString("username");
    	if (hostname == null) {
    		log(Level.WARNING, "username missing in "+file);
    		return false;
    	}
    	
    	password = conf.getString("password");
    	if (hostname == null) {
    		log(Level.WARNING, "password missing in "+file);
    		return false;
    	}
    	
    	database = conf.getString("database");
    	if (hostname == null) {
    		log(Level.WARNING, "database missing in "+file);
    		return false;
    	}
    	
    	table = conf.getString("table");
    	if (hostname == null) {
    		log(Level.WARNING, "table missing in "+file);
    		return false;
    	}
        
        // Connect to the database
        String dsn = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	
            if(username.equalsIgnoreCase("") && password.equalsIgnoreCase("")) {
            	conn = DriverManager.getConnection(dsn);	
            } else {
            	conn = DriverManager.getConnection(dsn, username, password);
            }
        } catch (Exception e) {
            log(Level.WARNING, "Failed to connect to database: "+e);
            return false;
        }
        
        return true;
	}
	
	public static void doOnlineLog(int playercount) {
		try {
			Statement statement = conn.createStatement();
			String query = "INSERT INTO `" + table + "` (playercount) VALUES ('"+playercount+"')";
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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