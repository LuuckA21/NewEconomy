package me.luucka.neweconomy.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.Settings;

import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    private static final Map<String, String> CLASS_DRIVER = new HashMap<>();

    private final NewEconomy plugin;

    @Getter
    private HikariDataSource dbSource;

    public DBConnection(NewEconomy plugin) {
        this.plugin = plugin;
        _loadClassDriverMap();
        _loadDbSource();
    }

    private static void _loadClassDriverMap() {
        CLASS_DRIVER.put("mysql", "com.mysql.cj.jdbc.Driver");
        CLASS_DRIVER.put("postgresql", "org.postgresql.Driver");
        CLASS_DRIVER.put("sqlite", "org.sqlite.JDBC");
        CLASS_DRIVER.put("h2", "org.h2.Driver");
    }

    private void _loadDbSource() {
        final String storageType = plugin.getSettings().getStorageType();
        final HikariConfig config = new HikariConfig();
        config.setPoolName("NewEconomy");
        config.setDriverClassName(CLASS_DRIVER.get(storageType));
        config.setJdbcUrl(plugin.getSettings().getDBStringConnection());
        if (Settings.REMOTE_DB_STORAGE_TYPES.contains(storageType)) {
            config.setUsername(plugin.getSettings().getDbUsername());
            config.setPassword(plugin.getSettings().getDbPassword());
        }
        dbSource = new HikariDataSource(config);
    }

}
