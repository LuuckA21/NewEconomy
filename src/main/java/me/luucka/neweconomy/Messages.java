package me.luucka.neweconomy;

import lombok.Getter;
import me.luucka.neweconomy.config.BaseConfiguration;

import java.io.File;

public class Messages implements IConfig {

    private final NewEconomy PLUGIN;
    private final BaseConfiguration config;

    private String prefix;
    private String noPermission;
    private String noConsole;
    private String reload;
    private String commandUsage;
    private String balance;
    private String balanceOther;
    private String addYourAccount;
    private String addOtherAccount;
    private String takeYourAccount;
    private String takeOtherAccount;
    private String setYourAccount;
    private String setOtherAccount;
    private String userNotExists;

    public Messages(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        this.config = new BaseConfiguration(new File(PLUGIN.getDataFolder(), "messages.yml"), "/messages.yml");
        reloadConfig();
    }

    public String getNoPermission() {
        return prefix + noPermission;
    }

    public String getNoConsole() {
        return prefix + noConsole;
    }

    public String getReload() {
        return prefix + reload;
    }

    public String getCommandUsage(final String usage) {
        return prefix + commandUsage.replace("{COMMAND_USAGE}", usage);
    }

    public String getBalance(final int money) {
        return prefix + balance.replace("{MONEY}", Integer.toString(money));
    }

    public String getBalanceOther(final String player, final int money) {
        return prefix + balanceOther.replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getAddYourAccount(final int money) {
        return prefix + addYourAccount.replace("{MONEY}", Integer.toString(money));
    }

    public String getAddOtherAccount(final String player, final int money) {
        return prefix + addOtherAccount.replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getTakeYourAccount(final int money) {
        return prefix + takeYourAccount.replace("{MONEY}", Integer.toString(money));
    }

    public String getTakeOtherAccount(final String player, final int money) {
        return prefix + takeOtherAccount.replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getSetYourAccount(final int money) {
        return prefix + setYourAccount.replace("{MONEY}", Integer.toString(money));
    }

    public String getSetOtherAccount(final String player, final int money) {
        return prefix + setOtherAccount.replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getUserNotExists(final String player) {
        return prefix + userNotExists.replace("{PLAYER}", player);
    }

    @Override
    public void reloadConfig() {
        config.load();
        prefix = _getPrefix();
        noPermission = _getNoPermission();
        noConsole = _getNoConsole();
        reload = _getReload();
        commandUsage = _getCommandUsage();
        balance = _getBalance();
        balanceOther = _getBalanceOther();
        addYourAccount = _getAddYourAccount();
        addOtherAccount = _getAddOtherAccount();
        takeYourAccount = _getTakeYourAccount();
        takeOtherAccount = _getTakeOtherAccount();
        setYourAccount = _getSetYourAccount();
        setOtherAccount = _getSetOtherAccount();
        userNotExists = _getYourNotExists();
    }

    private String _getPrefix() {
        String prefix = config.getString("prefix", "<grey>[<#2fba54>New<#ffe921>Economy<grey>]");
        return prefix.isEmpty() ? "" : prefix + " ";
    }

    private String _getNoPermission() {
        return config.getString("no-permission", "<red>You do not have permission");
    }

    private String _getNoConsole() {
        return config.getString("no-console", "<red>Console cannot run this command");
    }

    private String _getReload() {
        return config.getString("reload", "<green>Plugin reloaded!");
    }

    private String _getCommandUsage() {
        return config.getString("command-usage", "<red>Usage: {COMMAND_USAGE}");
    }

    private String _getBalance() {
        return config.getString("balance", "<red>Balance: &6{MONEY}");
    }

    private String _getBalanceOther() {
        return config.getString("balance-other", "<grey>{PLAYER}'s <green>balance: <gold>{MONEY}");
    }

    private String _getAddYourAccount() {
        return config.getString("add.your-account", "<gold>{MONEY} <green>has been added to your balance");
    }

    private String _getAddOtherAccount() {
        return config.getString("add.other-account", "<gold>{MONEY} <green>added to <grey>{PLAYER}'s <green>balance");
    }

    private String _getTakeYourAccount() {
        return config.getString("take.your-account", "<gold>{MONEY} <green>has been taken from your balance");
    }

    private String _getTakeOtherAccount() {
        return config.getString("take.other-account", "<gold>{MONEY} <green>taken from <grey>{PLAYER}'s <green>balance");
    }

    private String _getSetYourAccount() {
        return config.getString("set.your-account", "<green>Your balance was set to <gold>{MONEY}");
    }

    private String _getSetOtherAccount() {
        return config.getString("set.other-account", "<green>You set <grey>{PLAYER}'s <green>balance to <gold>{MONEY}");
    }

    private String _getYourNotExists() {
        return config.getString("user-not-exists", "<red>The user <grey>{PLAYER} <red>does not exist");
    }
}
