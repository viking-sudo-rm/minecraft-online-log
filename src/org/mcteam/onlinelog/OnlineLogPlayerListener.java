package org.mcteam.onlinelog;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class OnlineLogPlayerListener extends PlayerListener {
	
	@Override
	public void onPlayerJoin(PlayerEvent event) {
		OnlineLog.doOnlineLog(OnlineLog.instance.getServer().getOnlinePlayers().length);
	}
	
	@Override
	public void onPlayerQuit(PlayerEvent event) {
		OnlineLog.doOnlineLog(OnlineLog.instance.getServer().getOnlinePlayers().length);
	}

}
