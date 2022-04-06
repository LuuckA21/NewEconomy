package me.luucka.neweconomy;

import me.luucka.neweconomy.config.BaseConfiguration;

import java.io.File;
import java.util.logging.Logger;

public class Messages implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private final NewEconomy PLUGIN;
    private final BaseConfiguration config;

    public Messages(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        this.config = new BaseConfiguration(new File(PLUGIN.getDataFolder(), "messages.yml"), "/messages.yml");
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        config.load();
    }
}
