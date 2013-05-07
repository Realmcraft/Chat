package me.SgtMjrME.Channels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.SgtMjrME.Perm;
import me.SgtMjrME.RCChat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Local extends BaseChannel {

	final RCChat pl;
	int d;

	public Local(RCChat pl) {
		this.pl = pl;
		try {
			cfg.load(pl.getDataFolder().getAbsolutePath() + "/l.yml");
			setName(cfg.getString("name"));
			setDisp(cfg.getString("disp"));
			setPermission(cfg.getString("permission"));
			setColor(ChatColor.valueOf(cfg.getString("chatColor")));
			setPermErr(cfg.getString("permerr"));
			setOtherErr(cfg.getString("othererr"));
			d = cfg.getInt("distance");
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	@Override
	void getDestination(final Player p, final String format, final String message) {
		//This one has to be done sync'd, due to getNearbyEntities
		
		//First, check if player has perms
		Perm perm = RCChat.permissions.get(p);
		if (!perm.hasPerm(5)){
			p.sendMessage(getPermErr());
			return;
		}
		
		//Start location
		pl.getServer().getScheduler().runTask(pl, new Runnable() {
			@Override
			public void run() {
				final List<Entity> ent = p.getNearbyEntities(d, d, d);
				pl.getServer().getScheduler().runTaskAsynchronously(pl, new Runnable(){
					@Override
					public void run() {
						List<Player> out = new ArrayList<Player>();
						for(Entity e : ent){
							if (!(e instanceof Player)) continue;
							Player other = (Player) e;
							if (RCChat.permissions.get(other).hasPerm(18)) out.add(other);
						}
						receiveDestination(out, p, format, message);
					}
				});
			}
		});
	}

}
