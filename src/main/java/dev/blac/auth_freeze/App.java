package dev.blac.auth_freeze;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.model.user.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class App extends JavaPlugin implements Listener {

	private final Set<UUID> frozenPlayers = new HashSet<>();
	private LuckPerms luckPerms;

	@Override
	public void onEnable() {
		this.luckPerms = LuckPermsProvider.get();

		Bukkit.getPluginManager().registerEvents(this, this);

		// Listen for group promotions
		EventBus bus = luckPerms.getEventBus();
		bus.subscribe(this, UserPromoteEvent.class, event -> {
			Optional<String> optionalGroup = event.getGroupTo();
			optionalGroup.ifPresent(newGroup -> {
				if (newGroup.equalsIgnoreCase("member")) {
					UUID uuid = event.getUser().getUniqueId();
					frozenPlayers.remove(uuid);

					Player player = Bukkit.getPlayer(uuid);
					if (player != null && player.isOnline()) {
						player.sendMessage("§aYou are now a member and have been unfrozen!");
					}
				}
			});
		});
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		User user = luckPerms.getUserManager().getUser(player.getUniqueId());
		if (user != null) {
			String primaryGroup = user.getPrimaryGroup();
			if (!primaryGroup.equalsIgnoreCase("member")) {
				frozenPlayers.add(player.getUniqueId());
				player.sendMessage("§cYou are currently frozen until you're added to the member group.");
			}
		} else {
			frozenPlayers.add(player.getUniqueId());
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (frozenPlayers.contains(event.getPlayer().getUniqueId())) {
			if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
				event.setTo(event.getFrom());
			}
		}
	}
}
