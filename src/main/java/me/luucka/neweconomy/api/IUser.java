package me.luucka.neweconomy.api;

public interface IUser {

    void create();

    boolean exists();

    int getMoney();

    void setMoney(int money);

    void addMoney(int money);

    void takeMoney(int money);

    String getLastAccountName();

    void setLastAccountName();

}
