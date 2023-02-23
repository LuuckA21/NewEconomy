package me.luucka.neweconomy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.luucka.neweconomy.config.BaseConfiguration;
import me.luucka.neweconomy.config.IConfig;

import java.io.File;

public class Settings implements IConfig {

    private final NewEconomy plugin;

    private final BaseConfiguration config;

    @Getter
    private HikariDataSource dbSource;

    @Getter
    private int startMoney;

    @Getter
    private boolean useVault;

    public Settings(final NewEconomy plugin) {
        this.plugin = plugin;
        this.config = new BaseConfiguration(new File(this.plugin.getDataFolder(), "config.yml"), "/config.yml");
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        close();
        config.load();
        createConnection();
        startMoney = config.getInt("start-money", 0);
        useVault = config.getBoolean("use-vault", true);
    }

    private void createConnection() {
        final HikariConfig hConfig = new HikariConfig();
        hConfig.setPoolName("NewEconomy");

        StorageType type;
        try {
            type = StorageType.valueOf(this.config.getString("storage.type", "h2").toUpperCase());
        } catch (IllegalArgumentException e) {
            type = StorageType.H2;
        }

        hConfig.setDriverClassName(type.driverClass);

        if (type.remote) {
            hConfig.setJdbcUrl(
                    String.format(
                            "jdbc:%s://%s:%s/%s",
                            type.name().toLowerCase(),
                            this.config.getString("storage.settings.remote-db.host", "localhost"),
                            this.config.getString("storage.settings.remote-db.port", "3306"),
                            this.config.getString("storage.settings.remote-db.database", "lobbyplus")
                    )
            );
            hConfig.setUsername(config.getString("storage.settings.remote-db.username", "root"));
            hConfig.setPassword(config.getString("storage.settings.remote-db.password", ""));

        } else {
            String dbFile = plugin.getDataFolder().getAbsolutePath() + "/";
            if (type == StorageType.SQLITE) dbFile += config.getString("storage.settings.sqlite", "lobbyplus.db");
            if (type == StorageType.H2) dbFile += config.getString("storage.settings.h2", "lobbyplus");

            hConfig.setJdbcUrl(String.format("jdbc:%s:%s", type.name().toLowerCase(), dbFile));
        }

        dbSource = new HikariDataSource(hConfig);
    }

    public void close() {
        if (dbSource != null) {
            dbSource.close();
        }
    }

    private enum StorageType {
        MYSQL("com.mysql.cj.jdbc.Driver", true),
        MARIADB("org.mariadb.jdbc.Driver", true),
        POSTGRESQL("org.postgresql.Driver", true),
        SQLITE("org.sqlite.JDBC", false),
        H2("org.h2.Driver", false);

        private final String driverClass;

        private final boolean remote;

        StorageType(String driverClass, boolean remote) {
            this.driverClass = driverClass;
            this.remote = remote;
        }
    }

}
