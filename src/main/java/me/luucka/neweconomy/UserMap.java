package me.luucka.neweconomy;

import lombok.Getter;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.UserNotExistsException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserMap implements IConfig {

    private final NewEconomy PLUGIN;

    @Getter
    private final Map<String, UUID> nameUUID = new HashMap<>();

    @Getter
    private final Map<UUID, User> users = new HashMap<>();

    @Getter
    private final UUIDMap uuidMap;

    public UserMap(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        this.uuidMap = new UUIDMap(PLUGIN);
    }

    public void loadNameUUID() {
        PLUGIN.getServer().getScheduler().runTaskAsynchronously(PLUGIN, () -> uuidMap.loadNameUUID(nameUUID));
    }

    public void addNameUUID(final Player player) {
        nameUUID.put(player.getName().toLowerCase(), player.getUniqueId());
        uuidMap.writeUUIDMap();
    }

    public IUser getUser(final String name) throws UserNotExistsException {
        UUID uuid = nameUUID.get(name.toLowerCase());
        if (uuid == null) throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(name));
        return getUser(PLUGIN.getServer().getOfflinePlayer(uuid));
    }

    public IUser getUser(final OfflinePlayer player) {
        IUser user = users.get(player.getUniqueId());
        if (user == null) {
            loadUser(player);
            user = users.get(player.getUniqueId());
        }
        return user;
    }

    public void loadUser(final String name) {
        UUID uuid = nameUUID.get(name.toLowerCase());
        if (uuid == null) return;
        loadUser(PLUGIN.getServer().getOfflinePlayer(uuid));
    }

    public void loadUser(final OfflinePlayer player) {
        if (!users.containsKey(player.getUniqueId())) {
            User user = new User(PLUGIN, player);
            users.put(player.getUniqueId(), user);
        }
    }

    @Override
    public void reloadConfig() {
        uuidMap.forceWriteUUIDMap();
        loadNameUUID();
    }
}