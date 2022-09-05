package me.luucka.neweconomy;

import me.luucka.neweconomy.config.BaseConfiguration;

import java.io.File;

public class Messages implements IConfig {

    private final NewEconomy plugin;
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
        this.plugin = plugin;
        this.config = new BaseConfiguration(new File(this.plugin.getDataFolder(), "messages.yml"), "/messages.yml");
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
        noPermission = config.getString("no-permission", "<red>You do not have permission");
        noConsole = config.getString("no-console", "<red>Console cannot run this command");
        reload = config.getString("reload", "<green>Plugin reloaded!");
        commandUsage = config.getString("command-usage", "<red>Usage: {COMMAND_USAGE}");
        balance = config.getString("balance", "<red>Balance: &6{MONEY}");
        balanceOther = config.getString("balance-other", "<grey>{PLAYER}'s <green>balance: <gold>{MONEY}");
        addYourAccount = config.getString("add.your-account", "<gold>{MONEY} <green>has been added to your balance");
        addOtherAccount = config.getString("add.other-account", "<gold>{MONEY} <green>added to <grey>{PLAYER}'s <green>balance");
        takeYourAccount = config.getString("take.your-account", "<gold>{MONEY} <green>has been taken from your balance");
        takeOtherAccount = config.getString("take.other-account", "<gold>{MONEY} <green>taken from <grey>{PLAYER}'s <green>balance");
        setYourAccount = config.getString("set.your-account", "<green>Your balance was set to <gold>{MONEY}");
        setOtherAccount = config.getString("set.other-account", "<green>You set <grey>{PLAYER}'s <green>balance to <gold>{MONEY}");
        userNotExists = config.getString("user-not-exists", "<red>The user <grey>{PLAYER} <red>does not exist");
    }

    private String _getPrefix() {
        String prefix = config.getString("prefix", "<grey>[<#2fba54>New<#ffe921>Economy<grey>]");
        return prefix.isEmpty() ? "" : prefix + " ";
    }
}
