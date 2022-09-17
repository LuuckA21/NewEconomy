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

public class UserMap implements IConfig {

    private final NewEconomy plugin;

    @Getter
    private final UUIDMap uuidMap;

    @Getter
    private final ConcurrentHashMap<String, UUID> nameUUID = new ConcurrentHashMap<>();

    private final Cache<UUID, User> userCache = CacheBuilder.newBuilder().maximumSize(500).build();

    public UserMap(final NewEconomy plugin) {
        this.plugin = plugin;
        this.uuidMap = new UUIDMap(this.plugin);
    }

    public void loadNameUUID() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> uuidMap.loadNameUUID(nameUUID));
    }

    public void addNameUUID(final Player player) {
        nameUUID.put(player.getName().toLowerCase(), player.getUniqueId());
        uuidMap.writeUUIDMap();
    }

    public IUser getUser(final String name) throws UserNotExistsException {
        final UUID uuid = nameUUID.get(name.toLowerCase());
        if (uuid == null) {
            throw new UserNotExistsException(plugin.getMessages().getUserNotExists(name));
        }
        return getUser(plugin.getServer().getOfflinePlayer(uuid));
    }

    public IUser getUser(final OfflinePlayer player) {
        IUser user = userCache.getIfPresent(player.getUniqueId());
        while (user == null) {
            loadUser(player);
            user = userCache.getIfPresent(player.getUniqueId());
        }
        return user;
    }

    public void loadUser(final OfflinePlayer player) {
        userCache.put(player.getUniqueId(), new User(plugin, player));
    }

    public void unloadUser(final OfflinePlayer player) {
        userCache.invalidate(player.getUniqueId());
    }

    @Override
    public void reloadConfig() {
        userCache.invalidateAll();
        uuidMap.forceWriteUUIDMap();
        loadNameUUID();
    }
}
