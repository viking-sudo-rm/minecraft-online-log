package org.mcteam.onlinelog;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class OnlineLogPlayerListener extends PlayerListener {
	
	@Override
	public void onPlayerJoin(PlayerEvent event) {
		OnlineLog.log(event.getPlayer().getName()+" join");
	}
	
	@Override
	public void onPlayerQuit(PlayerEvent event) {
		OnlineLog.log(event.getPlayer().getName()+" quit");
	}

}
