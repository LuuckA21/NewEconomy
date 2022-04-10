package me.luucka.neweconomy.api;

public interface IUser {

    void create();

    boolean exists();

    int getMoney();

    void setMoney(int money);

    void addMoney(int money);

    void takeMoney(int money);

    long getAccountCreation();

    long getLastTransaction();

    void setLastTransaction();

    String getLastAccountName();

    void setLastAccountName();

}
