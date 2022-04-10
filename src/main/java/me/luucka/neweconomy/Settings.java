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
    private String dateFormat;

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
            dbHost = _getDbHost();
            dbPort = _getDbPort();
            dbName = _getDbName();
            dbUsername = _getDbUsername();
            dbPassword = _getDbPassword();
        }
        if (storageType.equals("sqlite")) {
            sqliteDbName = _getSqliteDbName();
        }
        if (storageType.equals("h2")) {
            h2DbName = _getH2DbName();
        }
        startMoney = _getStartMoney();
        dateFormat = _getDateFormat();
    }

    private String _getStorageType() {
        final String storageType = config.getString("storage.type", "yaml").toLowerCase();
        if (!STORAGE_TYPES.contains(storageType)) {
            LOGGER.log(Level.WARNING, String.format("Storage type '%s' is invalid, use default (yaml)", storageType));
            return "yaml";
        }
        return storageType;
    }

    private String _getDbHost() {
        return config.getString("storage.settings.remote-db.host", "localhost");
    }

    private String _getDbPort() {
        return config.getString("storage.settings.remote-db.port", "3306");
    }

    private String _getDbName() {
        return config.getString("storage.settings.remote-db.database", "neweconomy");
    }

    private String _getDbUsername() {
        return config.getString("storage.settings.remote-db.username", "root");
    }

    private String _getDbPassword() {
        return config.getString("storage.settings.remote-db.password", "");
    }

    private String _getSqliteDbName() {
        String sqliteDbName = config.getString("storage.settings.sqlite", "neweconomy.db");
        if (!sqliteDbName.endsWith(".db")) sqliteDbName += ".db";
        return sqliteDbName;
    }

    private String _getH2DbName() {
        return config.getString("storage.settings.h2", "neweconomy");
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

    private String _getDateFormat() {
        return config.getString("date-format", "MMM-d-y HH:m:s");
    }

}
