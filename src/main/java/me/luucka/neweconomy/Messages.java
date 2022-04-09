package me.luucka.neweconomy;

import me.luucka.neweconomy.config.BaseConfiguration;

import java.io.File;
import java.util.logging.Logger;

public class Messages implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

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

    public String getPrefix() {
        return prefix;
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
        String prefix = config.getString("prefix", "&7[#2fba54New#ffe921Economy&7]");
        return prefix.isEmpty() ? "" : prefix + " ";
    }

    private String _getNoPermission() {
        return config.getString("no-permission", "&cYou do not have permission");
    }

    private String _getNoConsole() {
        return config.getString("no-console", "&cConsole cannot run this command");
    }

    private String _getReload() {
        return config.getString("reload", "&aPlugin reloaded!");
    }

    private String _getCommandUsage() {
        return config.getString("command-usage", "&cUsage: {COMMAND_USAGE}");
    }

    private String _getBalance() {
        return config.getString("balance", "&aBalance: &6{MONEY}");
    }

    private String _getBalanceOther() {
        return config.getString("balance-other", "&7{PLAYER}'s &abalance: &6{MONEY}");
    }

    private String _getAddYourAccount() {
        return config.getString("add.your-account", "&6{MONEY} &ahas been added to your balance");
    }

    private String _getAddOtherAccount() {
        return config.getString("add.other-account", "&6{MONEY} &aadded to &7{PLAYER}'s &abalance");
    }

    private String _getTakeYourAccount() {
        return config.getString("take.your-account", "&6{MONEY} &ahas been taken from your balance");
    }

    private String _getTakeOtherAccount() {
        return config.getString("take.other-account", "&6{MONEY} &ataken from &7{PLAYER}'s &abalance");
    }

    private String _getSetYourAccount() {
        return config.getString("set.your-account", "&aYour balance was set to &6{MONEY}");
    }

    private String _getSetOtherAccount() {
        return config.getString("set.other-account", "&aYou set &7{PLAYER}'s &abalance to &6{MONEY}");
    }

    private String _getYourNotExists() {
        return config.getString("user-not-exists", "&cThe user &7{PLAYER} &cdoes not exist");
    }
}
