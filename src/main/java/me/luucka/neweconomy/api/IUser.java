package me.luucka.neweconomy.api;

import me.luucka.neweconomy.exceptions.UserNotExistsException;

public interface IUser {

    void create();

    boolean exists();

    int getMoney() throws UserNotExistsException;

    void setMoney(int money) throws UserNotExistsException;

    void addMoney(int money) throws UserNotExistsException;

    void takeMoney(int money) throws UserNotExistsException;

    long getAccountCreation() throws UserNotExistsException;

    long getLastTransaction() throws UserNotExistsException;

    void setLastTransaction() throws UserNotExistsException;

    String getLastAccountName() throws UserNotExistsException;

    void setLastAccountName() throws UserNotExistsException;

}
