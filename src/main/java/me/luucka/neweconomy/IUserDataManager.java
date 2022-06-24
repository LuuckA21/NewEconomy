package me.luucka.neweconomy;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface IUserDataManager extends IConfig {

    void createUser(OfflinePlayer player);

    boolean userExists(UUID uuid);

    int getUserMoney(OfflinePlayer player);

    void setUserMoney(OfflinePlayer player, int money);

    void addUserMoney(OfflinePlayer player, int money);

    void takeUserMoney(OfflinePlayer player, int money);

    String getUserLastAccountName(OfflinePlayer player);

    void setUserLastAccountName(OfflinePlayer player);

}
