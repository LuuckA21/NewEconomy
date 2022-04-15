package me.luucka.neweconomy.listeners;

import me.luucka.neweconomy.NewEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener {

    private final NewEconomy PLUGIN;

    public PlayerListeners(NewEconomy plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PLUGIN.addNameUUID(event.getPlayer());
        PLUGIN.loadUser(event.getPlayer());
    }
}
