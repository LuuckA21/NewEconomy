package me.luucka.neweconomy.database;

import me.luucka.neweconomy.IUserDataManager;
import me.luucka.neweconomy.NewEconomy;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DBUserDataManager implements IUserDataManager {

    private final NewEconomy plugin;
    private final DBConnection dbConnection;

    public DBUserDataManager(final NewEconomy plugin) throws SQLException {
        this.plugin = plugin;
        this.dbConnection = new DBConnection(this.plugin);
        _init();
    }

    private static final String SQL_BASE_INIT = "CREATE TABLE IF NOT EXISTS `neweconomy_balance` (" +
            "`uuid` VARCHAR(36) NOT NULL," +
            "`money` INT NOT NULL," +
            "`last_account_name` VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (`uuid`))";

    private static final String H2_INIT = "CREATE TABLE IF NOT EXISTS `neweconomy_balance` (" +
            "`uuid` VARCHAR(36) PRIMARY KEY," +
            "`money` INT NOT NULL," +
            "`last_account_name` VARCHAR(50) NOT NULL)";

    private void _init() throws SQLException {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(plugin.getSettings().getStorageType().equals("h2") ? H2_INIT : SQL_BASE_INIT)
        ) {
            ps.executeUpdate();
        }
    }

    @Override
    public boolean userExists(UUID uuid) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM neweconomy_balance WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            return ps.executeQuery().next();
        } catch (final SQLException ex) {
            return false;
        }
    }

    @Override
    public void createUser(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO neweconomy_balance (uuid, money, last_account_name) VALUES(?,?,?)")) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setInt(2, plugin.getSettings().getStartMoney());
            ps.setString(3, player.getName());
            ps.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getUserMoney(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT money FROM neweconomy_balance WHERE uuid=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("money");
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setUserMoney(OfflinePlayer player, int money) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE neweconomy_balance SET money=? WHERE uuid=?")) {
            ps.setInt(1, money);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addUserMoney(OfflinePlayer player, int money) {
        setUserMoney(player, getUserMoney(player) + money);
    }

    @Override
    public void takeUserMoney(OfflinePlayer player, int money) {
        money = getUserMoney(player) - money;
        if (money < 0) money = 0;
        setUserMoney(player, money);
    }

    @Override
    public String getUserLastAccountName(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT last_account_name FROM neweconomy_balance WHERE uuid=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("last_account_name");
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public void setUserLastAccountName(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE neweconomy_balance SET last_account_name=? WHERE uuid=?")) {
            ps.setString(1, player.getName());
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void reloadConfig() {
    }
}
