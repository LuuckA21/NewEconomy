package me.luucka.neweconomy;

import me.luucka.neweconomy.api.IUser;
import org.bukkit.OfflinePlayer;

import static me.luucka.neweconomy.utils.Color.colorize;

public class User implements IUser {

    private final NewEconomy plugin;
    private final OfflinePlayer player;
    private final IUserDataManager userDataManager;

    public User(final NewEconomy plugin, final OfflinePlayer player) {
        this.plugin = plugin;
        this.player = player;
        this.userDataManager = this.plugin.getUserDataManager();

        if (!exists()) {
            create();
        }
        setLastAccountName();

    }

    public void create() {
        userDataManager.createUser(player);
    }

    public boolean exists() {
        return userDataManager.userExists(player.getUniqueId());
    }

    public int getMoney() {
        return userDataManager.getUserMoney(player);
    }

    public void setMoney(int money) {
        userDataManager.setUserMoney(player, money);
        _sendMessage(plugin.getMessages().getSetYourAccount(money));
    }

    public void addMoney(int money) {
        userDataManager.addUserMoney(player, money);
        _sendMessage(plugin.getMessages().getAddYourAccount(money));
    }

    public void takeMoney(int money) {
        userDataManager.takeUserMoney(player, money);
        _sendMessage(plugin.getMessages().getTakeYourAccount(money));
    }

    public String getLastAccountName() {
        return userDataManager.getUserLastAccountName(player);
    }

    public void setLastAccountName() {
        userDataManager.setUserLastAccountName(player);
    }

    private void _sendMessage(final String message) {
        if (player.isOnline()) {
            player.getPlayer().sendMessage(colorize(message));
        }
    }
}
