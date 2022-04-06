package me.luucka.neweconomy.yaml;

import me.luucka.neweconomy.IConfig;
import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.config.BaseConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersFiles implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private final NewEconomy PLUGIN;
    private final File usersFilesFolder;
    private final Map<UUID, BaseConfiguration> usersFiles = new HashMap<>();

    public UsersFiles(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        this.usersFilesFolder = new File(PLUGIN.getDataFolder(), "users");
        if (!this.usersFilesFolder.exists()) {
            this.usersFilesFolder.mkdirs();
        }
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        usersFiles.clear();
        final File[] listOfFiles = usersFilesFolder.listFiles();
        if (listOfFiles.length >= 1) {
            for (final File file : listOfFiles) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".yml")) {
                    try {
                        final BaseConfiguration configuration = new BaseConfiguration(file);
                        configuration.load();
                        usersFiles.put(UUID.fromString(fileName.substring(1, fileName.length() - 4)), configuration);
                    } catch (final Exception ex) {
                        LOGGER.log(Level.WARNING, "User file " + fileName + " loading error!");
                    }
                }
            }
        }
    }
}
