package me.luucka.neweconomy;

import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.UserNotExistsException;
import org.bukkit.OfflinePlayer;

import static me.luucka.neweconomy.utils.Color.colorize;

public class User implements IUser {

    private final NewEconomy PLUGIN;
    private final OfflinePlayer player;
    private final IUserDataManager data;

    public User(final NewEconomy plugin, final OfflinePlayer player) {
        this.PLUGIN = plugin;
        this.player = player;
        this.data = this.PLUGIN.getUserDataManager();
    }

    @Override
    public void create() {
        data.createUser(player);
    }

    @Override
    public boolean exists() {
        return data.userExists(player.getUniqueId());
    }

    @Override
    public int getMoney() throws UserNotExistsException {
        return data.getUserMoney(player);
    }

    @Override
    public void setMoney(int money) throws UserNotExistsException {
        data.setUserMoney(player, money);
        setLastTransaction();
        _sendMessage(PLUGIN.getMessages().getSetYourAccount(money));
    }

    @Override
    public void addMoney(int money) throws UserNotExistsException {
        data.addUserMoney(player, money);
        setLastTransaction();
        _sendMessage(PLUGIN.getMessages().getAddYourAccount(money));
    }

    @Override
    public void takeMoney(int money) throws UserNotExistsException {
        data.takeUserMoney(player, money);
        setLastTransaction();
        _sendMessage(PLUGIN.getMessages().getTakeYourAccount(money));
    }

    @Override
    public long getAccountCreation() throws UserNotExistsException {
        return data.getUserAccountCreation(player);
    }

    @Override
    public long getLastTransaction() throws UserNotExistsException {
        return data.getUserLastTransaction(player);
    }

    @Override
    public void setLastTransaction() throws UserNotExistsException {
        data.setUserLastTransaction(player);
    }

    @Override
    public String getLastAccountName() throws UserNotExistsException {
        return data.getUserLastAccountName(player);
    }

    @Override
    public void setLastAccountName() throws UserNotExistsException {
        data.setUserLastAccountName(player);
    }

    private void _sendMessage(final String message) {
        if (player.isOnline()) {
            player.getPlayer().sendMessage(colorize(message));
        }
    }
}
