package dev.blac.auth_freeze;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Plugin enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin disabled.");
	}
}
