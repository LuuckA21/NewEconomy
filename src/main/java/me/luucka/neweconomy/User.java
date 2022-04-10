package me.luucka.neweconomy;

import me.luucka.neweconomy.api.IUser;
import org.bukkit.OfflinePlayer;

import static me.luucka.neweconomy.utils.Color.colorize;

public class User implements IUser {

    private final NewEconomy PLUGIN;
    private final OfflinePlayer player;
    private final IUserDataManager userDataManager;

    private int money;
    private final long accountCreation;
    private long lastTransaction;
    private String lastAccountName;

    public User(final NewEconomy plugin, final OfflinePlayer player) {
        this.PLUGIN = plugin;
        this.player = player;
        this.userDataManager = this.PLUGIN.getUserDataManager();

        if (!exists()) {
            create();
        }

        this.money = userDataManager.getUserMoney(player);
        this.accountCreation = userDataManager.getUserAccountCreation(player);
        this.lastTransaction = userDataManager.getUserLastTransaction(player);
        setLastAccountName();

    }

    public void create() {
        userDataManager.createUser(player);
    }

    public boolean exists() {
        return userDataManager.userExists(player.getUniqueId());
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        userDataManager.setUserMoney(player, money);
        setLastTransaction();
        _sendMessage(PLUGIN.getMessages().getSetYourAccount(money));
    }

    public void addMoney(int money) {
        this.money += money;
        userDataManager.addUserMoney(player, money);
        setLastTransaction();
        _sendMessage(PLUGIN.getMessages().getAddYourAccount(money));
    }

    public void takeMoney(int money) {
        this.money -= money;
        if (this.money < 0) this.money = 0;
        userDataManager.takeUserMoney(player, money);
        setLastTransaction();
        _sendMessage(PLUGIN.getMessages().getTakeYourAccount(money));
    }

    public long getAccountCreation() {
        return accountCreation;
    }

    public long getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction() {
        this.lastTransaction = System.currentTimeMillis();
        userDataManager.setUserLastTransaction(player);
    }

    public String getLastAccountName() {
        return lastAccountName;
    }

    public void setLastAccountName() {
        this.lastAccountName = player.getName();
        userDataManager.setUserLastAccountName(player);
    }

    private void _sendMessage(final String message) {
        if (player.isOnline()) {
            player.getPlayer().sendMessage(colorize(message));
        }
    }
}
