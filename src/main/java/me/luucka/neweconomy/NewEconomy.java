package me.luucka.neweconomy;

import lombok.Getter;
import me.luucka.neweconomy.database.DBConnection;
import me.luucka.neweconomy.yaml.UsersFiles;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class NewEconomy extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    @Getter
    private Settings settings;
    private Messages messages;

    private DBConnection dbConnection;
    private UsersFiles usersFiles;

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());

        settings = new Settings(this);
        messages = new Messages(this);

        if (Settings.REMOTE_DB_STORAGE_TYPES.contains(settings.getStorageType()) || Settings.FILE_DB_STORAGE_TYPES.contains(settings.getStorageType())) {
            dbConnection = new DBConnection(this);
            LOGGER.log(Level.INFO, String.format("Database connected successfully! (%s)", settings.getStorageType().toUpperCase()));
        } else {
            usersFiles = new UsersFiles(this);
            LOGGER.log(Level.INFO, "User data manager works with YAML");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
