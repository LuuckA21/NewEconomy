package me.luucka.neweconomy;

import lombok.Getter;
import me.luucka.neweconomy.commands.BalanceCommand;
import me.luucka.neweconomy.commands.EcoCommand;
import me.luucka.neweconomy.commands.ReloadCommand;
import me.luucka.neweconomy.database.DBUserDataManager;
import me.luucka.neweconomy.hooks.VaultNewEconomy;
import me.luucka.neweconomy.listeners.PlayerListeners;
import me.luucka.neweconomy.yaml.FileUserDataManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NewEconomy extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private final List<IConfig> configList = new ArrayList<>();

    @Getter
    private Settings settings;
    @Getter
    private Messages messages;

    @Getter
    private IUserDataManager userDataManager;

    @Getter
    private UserCacheManager userCacheManager;

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());

        try {
            Class.forName("net.milkbowl.vault.economy.Economy");
            getServer().getServicesManager().register(Economy.class, new VaultNewEconomy(this), this, ServicePriority.Normal);
            LOGGER.log(Level.INFO, "Find Vault...");
            LOGGER.log(Level.INFO, "Hook Vault for Economy management");
        } catch (final ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Vault not found... disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        settings = new Settings(this);
        configList.add(settings);

        messages = new Messages(this);
        configList.add(messages);

        userCacheManager = new UserCacheManager(this);
        userCacheManager.readCSV();

        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);

        if (Settings.REMOTE_DB_STORAGE_TYPES.contains(settings.getStorageType()) || Settings.FILE_DB_STORAGE_TYPES.contains(settings.getStorageType())) {
            try {
                userDataManager = new DBUserDataManager(this);
                LOGGER.log(Level.INFO, String.format("Database connected successfully! (%s)", settings.getStorageType().toUpperCase()));
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.log(Level.SEVERE, "Database connection error! Try to restart your server");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else {
            userDataManager = new FileUserDataManager(this);
            LOGGER.log(Level.INFO, "User data managed by YAML Manager");
        }
        configList.add(userDataManager);

        new BalanceCommand(this);
        new EcoCommand(this);
        new ReloadCommand(this);
    }

    @Override
    public void onDisable() {
        if (userCacheManager != null) {
            userCacheManager.writeCSV();
        }
    }

    public void reload() {
        for (final IConfig iConfig : configList) {
            iConfig.reloadConfig();
        }
    }

}
