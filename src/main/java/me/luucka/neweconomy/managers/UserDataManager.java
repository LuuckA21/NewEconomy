package me.luucka.neweconomy.managers;

import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.config.IConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class UserDataManager implements IConfig {

    private final NewEconomy plugin;

    public UserDataManager(final NewEconomy plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    private static final String SQL_INIT = "CREATE TABLE IF NOT EXISTS `NewEconomy_Balance` (" +
            "`uuid` VARCHAR(36) NOT NULL," +
            "`money` INT NOT NULL," +
            "`last_account_name` VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (`uuid`))";

    private void _init() throws SQLException {
        try (Connection conn = plugin.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INIT)
        ) {
            ps.executeUpdate();
        }
    }

    public void create(final OfflinePlayer player) {
        try (Connection conn = plugin.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO NewEconomy_Balance (uuid, money, last_account_name) VALUES(?,?,?)");) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setInt(2, plugin.getSettings().getStartMoney());
            ps.setString(3, player.getName());
            ps.executeUpdate();
        } catch (final SQLException ignored) {
        }
    }

    public int getMoney(final OfflinePlayer player) {
        try (Connection conn = plugin.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT money FROM NewEconomy_Balance WHERE uuid=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("money");
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setMoney(final OfflinePlayer player, final int money) {
        try (Connection conn = plugin.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE NewEconomy_Balance SET money=? WHERE uuid=?")) {
            ps.setInt(1, money);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addMoney(final OfflinePlayer player, final int money) {
        setMoney(player, getMoney(player) + money);
    }

    public void takeMoney(final OfflinePlayer player, int money) {
        money = getMoney(player) - money;
        if (money < 0) money = 0;
        setMoney(player, money);
    }

    public void setLastAccountName(final OfflinePlayer player) {
        try (Connection conn = plugin.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE NewEconomy_Balance SET last_account_name=? WHERE uuid=?")) {
            ps.setString(1, player.getName());
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void reloadConfig() {
        try {
            _init();
        } catch (final SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Database connection error! Try to restart your server");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }
}
