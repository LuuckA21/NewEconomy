package me.luucka.neweconomy;

import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.managers.UserDataManager;
import org.bukkit.OfflinePlayer;

import static me.luucka.neweconomy.utils.Color.colorize;

public class User implements IUser {

    private final NewEconomy plugin;

    private final UserDataManager userDataManager;

    private final OfflinePlayer player;

    private int money;

    public User(final NewEconomy plugin, final OfflinePlayer player) {
        this.plugin = plugin;
        this.player = player;
        this.userDataManager = this.plugin.getUserDataManager();

        create();
        this.money = this.userDataManager.getMoney(this.player);
        this.setLastAccountName();

    }

    @Override
    public void create() {
        userDataManager.create(player);
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public void setMoney(int money) {
        this.money = money;
        this.userDataManager.setMoney(player, money);
        _sendMessage(plugin.getMessages().getSetYourAccount(money));
    }

    @Override
    public void addMoney(int money) {
        this.money += money;
        userDataManager.addMoney(player, money);
        _sendMessage(plugin.getMessages().getAddYourAccount(money));
    }

    @Override
    public void takeMoney(int money) {
        this.money = Math.max(this.money - money, 0);
        userDataManager.takeMoney(player, money);
        _sendMessage(plugin.getMessages().getTakeYourAccount(money));
    }

    @Override
    public String getLastAccountName() {
        return this.player.getName();
    }

    @Override
    public void setLastAccountName() {
        userDataManager.setLastAccountName(player);
    }

    private void _sendMessage(final String message) {
        if (player.isOnline()) {
            player.getPlayer().sendMessage(colorize(message));
        }
    }
}
