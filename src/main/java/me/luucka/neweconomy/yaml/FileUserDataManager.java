package me.luucka.neweconomy.yaml;

import me.luucka.neweconomy.IUserDataManager;
import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.config.BaseConfiguration;
import me.luucka.neweconomy.exceptions.UserNotExistsException;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUserDataManager implements IUserDataManager {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private final NewEconomy PLUGIN;
    private final File dataFolder;
    private final Map<UUID, BaseConfiguration> usersData = new HashMap<>();

    public FileUserDataManager(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        this.dataFolder = new File(PLUGIN.getDataFolder(), "userdata");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
        reloadConfig();
    }

    @Override
    public void createUser(OfflinePlayer player) {
        BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            final File file = new File(dataFolder, player.getUniqueId() + ".yml");
            if (file.exists()) {
                return;
            }
            configuration = new BaseConfiguration(file);
            configuration.load();
            configuration.setProperty("money", PLUGIN.getSettings().getStartMoney());
            configuration.setProperty("account-creation", System.currentTimeMillis());
            configuration.setProperty("last-transaction", -1L);
            configuration.setProperty("last-account-name", player.getName());
            configuration.save();
            usersData.put(player.getUniqueId(), configuration);
        }
    }

    @Override
    public boolean userExists(UUID uuid) {
        final BaseConfiguration configuration = usersData.get(uuid);
        return configuration != null;
    }

    @Override
    public int getUserMoney(OfflinePlayer player) throws UserNotExistsException {
        final BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(player.getName()));
        }
        return configuration.getInt("money", 0);
    }

    @Override
    public void setUserMoney(OfflinePlayer player, int money) throws UserNotExistsException {
        final BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(player.getName()));
        }
        configuration.setProperty("money", money);
        configuration.save();
    }

    @Override
    public void addUserMoney(OfflinePlayer player, int money) throws UserNotExistsException {
        setUserMoney(player, getUserMoney(player) + money);
    }

    @Override
    public void takeUserMoney(OfflinePlayer player, int money) throws UserNotExistsException {
        money = getUserMoney(player) - money;
        if (money < 0) money = 0;
        setUserMoney(player, money);
    }

    @Override
    public long getUserAccountCreation(OfflinePlayer player) throws UserNotExistsException {
        final BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(player.getName()));
        }
        return configuration.getLong("account-creation", 0L);
    }

    @Override
    public long getUserLastTransaction(OfflinePlayer player) throws UserNotExistsException {
        final BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(player.getName()));
        }
        return configuration.getLong("last-transaction", -1L);
    }

    @Override
    public void setUserLastTransaction(OfflinePlayer player) throws UserNotExistsException {
        final BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(player.getName()));
        }
        configuration.setProperty("last-transaction", System.currentTimeMillis());
        configuration.save();
    }

    @Override
    public String getUserLastAccountName(OfflinePlayer player) throws UserNotExistsException {
        final BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(player.getName()));
        }
        return configuration.getString("last-account-name", "NULL");
    }

    @Override
    public void setUserLastAccountName(OfflinePlayer player) throws UserNotExistsException {
        final BaseConfiguration configuration = usersData.get(player.getUniqueId());
        if (configuration == null) {
            throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(player.getName()));
        }
        configuration.setProperty("last-account-name", player.getName());
        configuration.save();
    }

    @Override
    public void reloadConfig() {
        usersData.clear();
        final File[] listOfFiles = dataFolder.listFiles();
        //LOGGER.warning("listOfFiles.length: " + listOfFiles.length);
        if (listOfFiles.length >= 1) {
            for (final File file : listOfFiles) {
                String fileName = file.getName();
                //LOGGER.warning("fileName: " + fileName);
                if (file.isFile() && fileName.endsWith(".yml")) {
                    try {
                        final BaseConfiguration configuration = new BaseConfiguration(file);
                        configuration.load();
                        usersData.put(UUID.fromString(fileName.substring(0, fileName.length() - 4)), configuration);
                        //LOGGER.warning("fileName.substring: " + fileName.substring(0, fileName.length() - 4));
                    } catch (final Exception ex) {
                        LOGGER.log(Level.WARNING, "User file " + fileName + " loading error!");
                    }
                }
            }
        }
    }
}
