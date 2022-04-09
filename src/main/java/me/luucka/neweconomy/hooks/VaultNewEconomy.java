package me.luucka.neweconomy.hooks;

import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.User;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.UserNotExistsException;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class VaultNewEconomy implements Economy {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private final NewEconomy PLUGIN;

    public VaultNewEconomy(NewEconomy plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean isEnabled() {
        return PLUGIN.isEnabled();
    }

    @Override
    public String getName() {
        return "NewEconomy Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);
        return format.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public boolean hasAccount(String playerName) {
        final OfflinePlayer player = PLUGIN.getUserCacheManager().getPlayer(playerName);
        if (player == null) return false;
        final IUser user = new User(PLUGIN, player);
        return user.exists();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        final IUser user = new User(PLUGIN, player);
        return user.exists();
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        final OfflinePlayer player = PLUGIN.getUserCacheManager().getPlayer(playerName);
        if (player == null) return PLUGIN.getSettings().getStartMoney();
        final IUser user = new User(PLUGIN, player);
        try {
            return user.getMoney();
        } catch (UserNotExistsException e) {
            return PLUGIN.getSettings().getStartMoney();
        }
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        final IUser user = new User(PLUGIN, player);
        try {
            return user.getMoney();
        } catch (UserNotExistsException e) {
            return PLUGIN.getSettings().getStartMoney();
        }
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        final OfflinePlayer player = PLUGIN.getUserCacheManager().getPlayer(playerName);
        if (player == null) return false;
        final IUser user = new User(PLUGIN, player);
        try {
            return (user.getMoney() - amount) >= 0;
        } catch (UserNotExistsException e) {
            return false;
        }
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        final IUser user = new User(PLUGIN, player);
        try {
            return (user.getMoney() - amount) >= 0;
        } catch (UserNotExistsException e) {
            return false;
        }
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        final OfflinePlayer player = PLUGIN.getUserCacheManager().getPlayer(playerName);
        if (player == null) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null");
        final IUser user = new User(PLUGIN, player);
        try {
            user.takeMoney((int) amount);
            return new EconomyResponse(amount, user.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (UserNotExistsException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        final IUser user = new User(PLUGIN, player);
        try {
            user.takeMoney((int) amount);
            return new EconomyResponse(amount, user.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (UserNotExistsException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        final OfflinePlayer player = PLUGIN.getUserCacheManager().getPlayer(playerName);
        if (player == null) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null");
        final IUser user = new User(PLUGIN, player);
        try {
            user.addMoney((int) amount);
            return new EconomyResponse(amount, user.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (UserNotExistsException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null");
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        final IUser user = new User(PLUGIN, player);
        try {
            user.addMoney((int) amount);
            return new EconomyResponse(amount, user.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (UserNotExistsException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "NewEconomy does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        final OfflinePlayer player = PLUGIN.getUserCacheManager().getPlayer(playerName);
        if (player == null) return false;
        final IUser user = new User(PLUGIN, player);
        if (user.exists()) {
            return false;
        } else {
            user.create();
            return true;
        }
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        final IUser user = new User(PLUGIN, player);
        if (user.exists()) {
            return false;
        } else {
            user.create();
            return true;
        }
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
