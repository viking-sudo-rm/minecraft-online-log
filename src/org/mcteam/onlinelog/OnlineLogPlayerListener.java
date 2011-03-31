package org.mcteam.onlinelog;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnlineLogPlayerListener extends PlayerListener {
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		OnlineLog.logCurrentState();
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		OnlineLog.logCurrentState();
	}	

}
