package me.luucka.neweconomy;

import me.luucka.neweconomy.config.BaseConfiguration;
import me.luucka.neweconomy.config.IConfig;

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
        return noPermission.replace("{PREFIX}", prefix);
    }

    public String getNoConsole() {
        return noConsole.replace("{PREFIX}", prefix);
    }

    public String getReload() {
        return reload.replace("{PREFIX}", prefix);
    }

    public String getCommandUsage(final String usage) {
        return commandUsage.replace("{PREFIX}", prefix).replace("{COMMAND_USAGE}", usage);
    }

    public String getBalance(final int money) {
        return balance.replace("{PREFIX}", prefix).replace("{MONEY}", Integer.toString(money));
    }

    public String getBalanceOther(final String player, final int money) {
        return balanceOther.replace("{PREFIX}", prefix).replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getAddYourAccount(final int money) {
        return addYourAccount.replace("{PREFIX}", prefix).replace("{MONEY}", Integer.toString(money));
    }

    public String getAddOtherAccount(final String player, final int money) {
        return addOtherAccount.replace("{PREFIX}", prefix).replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getTakeYourAccount(final int money) {
        return takeYourAccount.replace("{PREFIX}", prefix).replace("{MONEY}", Integer.toString(money));
    }

    public String getTakeOtherAccount(final String player, final int money) {
        return takeOtherAccount.replace("{PREFIX}", prefix).replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getSetYourAccount(final int money) {
        return setYourAccount.replace("{PREFIX}", prefix).replace("{MONEY}", Integer.toString(money));
    }

    public String getSetOtherAccount(final String player, final int money) {
        return setOtherAccount.replace("{PREFIX}", prefix).replace("{PLAYER}", player).replace("{MONEY}", Integer.toString(money));
    }

    public String getUserNotExists(final String player) {
        return userNotExists.replace("{PREFIX}", prefix).replace("{PLAYER}", player);
    }

    @Override
    public void reloadConfig() {
        config.load();
        prefix = config.getString("prefix", "<grey>[<#2fba54>New<#ffe921>Economy<grey>]");
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
}
