package me.luucka.neweconomy;

import lombok.Getter;
import me.luucka.neweconomy.api.INewEconomy;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.api.UserNotExistsException;
import me.luucka.neweconomy.commands.BalanceCommand;
import me.luucka.neweconomy.commands.EconomyCommand;
import me.luucka.neweconomy.commands.ReloadCommand;
import me.luucka.neweconomy.config.IConfig;
import me.luucka.neweconomy.managers.UserDataManager;
import me.luucka.neweconomy.hooks.PlaceholderNewEconomy;
import me.luucka.neweconomy.hooks.VaultNewEconomy;
import me.luucka.neweconomy.listeners.PlayerListeners;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NewEconomy extends JavaPlugin implements INewEconomy {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private final List<IConfig> configList = new ArrayList<>();

    @Getter
    private Settings settings;
    @Getter
    private Messages messages;

    @Getter
    private UserDataManager userDataManager;

    @Getter
    private UserMap userMap;

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());

        // Load Config.yml
        settings = new Settings(this);
        configList.add(settings);

        // Load Messages.yml
        messages = new Messages(this);
        configList.add(messages);

        // Check Vault and if present register VaultNewEconomy Service
        if (settings.isUseVault()) {
            try {
                Class.forName("net.milkbowl.vault.economy.Economy");
                getServer().getServicesManager().register(Economy.class, new VaultNewEconomy(this), this, ServicePriority.Normal);
                LOGGER.log(Level.INFO, "Find Vault...");
                LOGGER.log(Level.INFO, "Hook Vault for Economy management");
            } catch (final ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Vault not found... NewEconomy will works without Vault");
            }
        } else {
            LOGGER.log(Level.INFO, "Vault is disable... NewEconomy will works without Vault");
        }

        // Check PlaceholderAPI and if present load NewEconomy Placeholders
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderNewEconomy(this).register();
            LOGGER.log(Level.INFO, "Find PlaceholderAPI...");
            LOGGER.log(Level.INFO, "Hook PlaceholderAPI for Placeholders management");
        }

        userDataManager = new UserDataManager(this);
        configList.add(userDataManager);

        userMap = new UserMap(this);
        userMap.reloadConfig();
        configList.add(userMap);

        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);

        new BalanceCommand(this);
        new EconomyCommand(this);
        new ReloadCommand(this);
    }

    @Override
    public void onDisable() {
        userMap.getUuidMap().shutdown();
    }

    public void reload() {
        for (final IConfig iConfig : configList) {
            iConfig.reloadConfig();
        }
    }

    public Connection getConnection() {
        try {
            return this.getSettings().getDbSource().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error! Try to restart your server");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        return null;
    }

    @Override
    public void addNameUUID(final Player player) {
        userMap.addNameUUID(player);
    }

    @Override
    public IUser getUser(final String name) throws UserNotExistsException {
        return userMap.getUser(name);
    }

    @Override
    public IUser getUser(final OfflinePlayer player) {
        return userMap.getUser(player);
    }

    @Override
    public void loadUser(final OfflinePlayer player) {
        userMap.loadUser(player);
    }

    @Override
    public void unloadUser(final OfflinePlayer player) {
        userMap.unloadUser(player);
    }

}
