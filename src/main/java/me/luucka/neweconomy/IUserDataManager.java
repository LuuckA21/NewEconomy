package me.luucka.neweconomy;

import me.luucka.neweconomy.exceptions.UserNotExistsException;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface IUserDataManager extends IConfig {

    void createUser(OfflinePlayer player);

    boolean userExists(UUID uuid);

    int getUserMoney(OfflinePlayer player) throws UserNotExistsException;

    void setUserMoney(OfflinePlayer player, int money) throws UserNotExistsException;

    void addUserMoney(OfflinePlayer player, int money) throws UserNotExistsException;

    void takeUserMoney(OfflinePlayer player, int money) throws UserNotExistsException;

    long getUserAccountCreation(OfflinePlayer player) throws UserNotExistsException;

    long getUserLastTransaction(OfflinePlayer player) throws UserNotExistsException;

    void setUserLastTransaction(OfflinePlayer player) throws UserNotExistsException;

    String getUserLastAccountName(OfflinePlayer player) throws UserNotExistsException;

    void setUserLastAccountName(OfflinePlayer player) throws UserNotExistsException;

}
