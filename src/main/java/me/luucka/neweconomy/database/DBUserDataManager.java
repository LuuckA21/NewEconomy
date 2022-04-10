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

    private final NewEconomy PLUGIN;
    private final DBConnection dbConnection;

    public DBUserDataManager(final NewEconomy plugin) throws SQLException {
        this.PLUGIN = plugin;
        this.dbConnection = new DBConnection(this.PLUGIN);
        _init();
    }

    private static final String SQL_BASE_INIT = "CREATE TABLE IF NOT EXISTS `userdata` (" +
            "`uuid` VARCHAR(36) NOT NULL," +
            "`money` INT NOT NULL," +
            "`account_creation` BIGINT unsigned NOT NULL," +
            "`last_transaction` BIGINT unsigned NOT NULL," +
            "`last_account_name` VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (`uuid`))";

    private static final String H2_INIT = "CREATE TABLE IF NOT EXISTS `userdata` (" +
            "`uuid` VARCHAR(36) PRIMARY KEY," +
            "`money` INT NOT NULL," +
            "`account_creation` BIGINT NOT NULL," +
            "`last_transaction` BIGINT NOT NULL," +
            "`last_account_name` VARCHAR(50) NOT NULL)";

    private void _init() throws SQLException {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(PLUGIN.getSettings().getStorageType().equals("h2") ? H2_INIT : SQL_BASE_INIT)
        ) {
            ps.executeUpdate();
        }
    }

    @Override
    public boolean userExists(UUID uuid) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM userdata WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            return ps.executeQuery().next();
        } catch (final SQLException ex) {
            return false;
        }
    }

    @Override
    public void createUser(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO userdata (uuid, money, account_creation, last_transaction, last_account_name) VALUES(?,?,?,?,?)")) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setInt(2, PLUGIN.getSettings().getStartMoney());
            ps.setLong(3, System.currentTimeMillis());
            ps.setLong(4, -1L);
            ps.setString(5, player.getName());
            ps.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getUserMoney(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT money FROM userdata WHERE uuid=?")) {
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
             PreparedStatement ps = conn.prepareStatement("UPDATE userdata SET money=? WHERE uuid=?")) {
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
    public long getUserAccountCreation(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT account_creation FROM userdata WHERE uuid=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("account_creation");
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
        return -1L;
    }

    @Override
    public long getUserLastTransaction(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT last_transaction FROM userdata WHERE uuid=?")) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("last_transaction");
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
        return -1L;
    }

    @Override
    public void setUserLastTransaction(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE userdata SET last_transaction=? WHERE uuid=?")) {
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getUserLastAccountName(OfflinePlayer player) {
        try (Connection conn = dbConnection.getDbSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT last_account_name FROM userdata WHERE uuid=?")) {
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
             PreparedStatement ps = conn.prepareStatement("UPDATE userdata SET last_account_name=? WHERE uuid=?")) {
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
