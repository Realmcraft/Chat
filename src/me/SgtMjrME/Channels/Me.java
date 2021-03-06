package me.SgtMjrME.Channels;

import java.util.Iterator;

import me.SgtMjrME.Perm;
import me.SgtMjrME.RCChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Me extends BaseChannel {

	public Me(RCChat pl) {
		super(pl, "me");
//		this.pl = pl;
//		try {
//			cfg.load(pl.getDataFolder().getAbsolutePath() + "/channels/me.yml");
//			setName(cfg.getString("name"));
//			setDisp(cfg.getString("disp"));
//			setPermission(cfg.getString("permission"));
//			setColor(ChatColor.valueOf(cfg.getString("chatColor")));
//			setPermErr(ChatColor.translateAlternateColorCodes('&', cfg.getString("permerr")));
//			setOtherErr(ChatColor.translateAlternateColorCodes('&', cfg.getString("othererr")));
//		} catch (IOException | InvalidConfigurationException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	void getDestination(AsyncPlayerChatEvent e) {
		// First, check if player has perms
		Player p = e.getPlayer();
		Perm perm = RCChat.getPerm(p);
		if (!perm.hasPerm(2)) {
			p.sendMessage(getPermErr());
			e.getRecipients().clear();
			e.setCancelled(true);
			return;
		}
		
//		//Questionable call, never used aslist
//		List<Player> players = new ArrayList<Player>(
//				Arrays.asList(pl.getServer().getOnlinePlayers()));
		
		//Remove non-permission
		Iterator<Player> i = e.getRecipients().iterator();
		while(i.hasNext()){
			if (!RCChat.getPerm(i.next()).hasPerm(15))
				i.remove();
		}
		//send
		receiveDestination(e);
	}

	@Override
	public int getPerm() {
		return 2;
	}
}
