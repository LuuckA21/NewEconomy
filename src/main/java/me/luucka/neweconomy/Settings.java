package me.luucka.neweconomy;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import me.luucka.neweconomy.config.BaseConfiguration;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    public static final List<String> STORAGE_TYPES = ImmutableList.of("mysql", "postgresql", "sqlite", "h2", "yaml");
    public static final List<String> REMOTE_DB_STORAGE_TYPES = ImmutableList.of("mysql", "postgresql");
    public static final List<String> FILE_DB_STORAGE_TYPES = ImmutableList.of("sqlite", "h2");

    private final NewEconomy PLUGIN;
    private final BaseConfiguration config;

    @Getter
    private String storageType;
    @Getter
    private String dbHost;
    @Getter
    private String dbPort;
    @Getter
    private String dbName;
    @Getter
    private String dbUsername;
    @Getter
    private String dbPassword;
    @Getter
    private String sqliteDbName;
    @Getter
    private String h2DbName;
    @Getter
    private int startMoney;

    @Getter
    private boolean useVault;

    public Settings(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        this.config = new BaseConfiguration(new File(PLUGIN.getDataFolder(), "config.yml"), "/config.yml");
        reloadConfig();
    }

    public String getDBStringConnection() {
        if (REMOTE_DB_STORAGE_TYPES.contains(storageType)) {
            return String.format("jdbc:%s://%s:%s/%s", storageType, dbHost, dbPort, dbName);
        }
        if (FILE_DB_STORAGE_TYPES.contains(storageType)) {
            String dbFile = PLUGIN.getDataFolder().getAbsolutePath() + "/";
            if (storageType.equals("h2")) dbFile += h2DbName;
            if (storageType.equals("sqlite")) dbFile += sqliteDbName;
            return String.format("jdbc:%s:%s", storageType, dbFile);
        }
        return null;
    }

    @Override
    public void reloadConfig() {
        config.load();
        storageType = _getStorageType();
        if (REMOTE_DB_STORAGE_TYPES.contains(storageType)) {
            dbHost = config.getString("storage.settings.remote-db.host", "localhost");
            dbPort = config.getString("storage.settings.remote-db.port", "3306");
            dbName = config.getString("storage.settings.remote-db.database", "neweconomy");
            dbUsername = config.getString("storage.settings.remote-db.username", "root");
            dbPassword = config.getString("storage.settings.remote-db.password", "");
        }
        if (storageType.equals("sqlite")) {
            sqliteDbName = config.getString("storage.settings.sqlite", "neweconomy.db");
        }
        if (storageType.equals("h2")) {
            h2DbName = config.getString("storage.settings.h2", "neweconomy");
        }
        startMoney = _getStartMoney();
        useVault = config.getBoolean("use-vault", true);
    }

    private String _getStorageType() {
        final String storageType = config.getString("storage.type", "yaml").toLowerCase();
        if (!STORAGE_TYPES.contains(storageType)) {
            LOGGER.log(Level.WARNING, String.format("Storage type '%s' is invalid, use default (yaml)", storageType));
            return "yaml";
        }
        return storageType;
    }

    private int _getStartMoney() {
        final String sMoney = config.getString("start-money", "0");
        int money;
        try {
            money = Integer.parseInt(sMoney);
            if (money < 0) money = 0;
        } catch (final NumberFormatException e) {
            money = 0;
        }
        return money;
    }

}
