package me.luucka.neweconomy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.api.UserNotExistsException;
import me.luucka.neweconomy.config.IConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager implements IConfig {

    private final NewEconomy plugin;

    @Getter
    private final UUIDMap uuidMap;

    @Getter
    private final ConcurrentHashMap<String, UUID> playernameUuidMap = new ConcurrentHashMap<>();

    private final Cache<UUID, User> users = CacheBuilder.newBuilder().maximumSize(500).build();

    public UserManager(final NewEconomy plugin) {
        this.plugin = plugin;
        this.uuidMap = new UUIDMap(plugin);
    }

    public void loadPlayernameUuidMap() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> uuidMap.loadPlayernameUuidMap(playernameUuidMap));
    }

    public void addPlayernameUuidToMap(final Player player) {
        playernameUuidMap.put(player.getName().toLowerCase(), player.getUniqueId());
        uuidMap.writeUUIDMap();
    }

    public IUser getUser(final String name) throws UserNotExistsException {
        final UUID uuid = playernameUuidMap.get(name.toLowerCase());
        if (uuid == null) {
            throw new UserNotExistsException(plugin.getMessages().getUserNotExists(name));
        }
        return getUser(plugin.getServer().getOfflinePlayer(uuid));
    }

    public IUser getUser(final OfflinePlayer player) {
        IUser user = users.getIfPresent(player.getUniqueId());
        while (user == null) {
            loadUser(player);
            user = users.getIfPresent(player.getUniqueId());
        }
        return user;
    }

    public void loadUser(final OfflinePlayer player) {
        users.put(player.getUniqueId(), new User(plugin, player));
    }

    public void unloadUser(final OfflinePlayer player) {
        users.invalidate(player.getUniqueId());
    }

    @Override
    public void reloadConfig() {
        users.invalidateAll();
        uuidMap.forceWriteUUIDMap();
        loadPlayernameUuidMap();
    }
}
