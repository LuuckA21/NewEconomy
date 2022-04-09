package me.luucka.neweconomy.listeners;

import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.User;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.UserNotExistsException;
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
        PLUGIN.getUserCacheManager().addPlayer(event.getPlayer());
        IUser user = new User(PLUGIN, event.getPlayer());
        user.create();
        try {
            user.setLastAccountName();
        } catch (UserNotExistsException ignored) {}
    }
}
