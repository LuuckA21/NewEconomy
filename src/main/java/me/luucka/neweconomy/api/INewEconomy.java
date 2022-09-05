package me.luucka.neweconomy.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface INewEconomy extends Plugin {

    void addNameUUID(final Player player);

    IUser getUser(final String name) throws UserNotExistsException;

    IUser getUser(final OfflinePlayer player);

    void loadUser(final OfflinePlayer player);

    void unloadUser(final OfflinePlayer player);

}
