package me.luucka.neweconomy.listeners;

import me.luucka.neweconomy.NewEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final NewEconomy plugin;

    public PlayerListeners(final NewEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        plugin.addPlayernameUuidToMap(event.getPlayer());
        plugin.loadUser(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        plugin.unloadUser(event.getPlayer());
    }
}
