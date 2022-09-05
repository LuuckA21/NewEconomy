package me.luucka.neweconomy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.api.UserNotExistsException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserMap implements IConfig {

    private final NewEconomy PLUGIN;

    @Getter
    private final ConcurrentHashMap<String, UUID> nameUUID = new ConcurrentHashMap<>();

    private final Cache<UUID, User> users = CacheBuilder.newBuilder().maximumSize(1000).build();

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
        IUser user = users.asMap().get(player.getUniqueId());
        while (user == null) {
            loadUser(player);
            user = users.asMap().get(player.getUniqueId());
        }
        return user;
    }

    public void loadUser(final OfflinePlayer player) {
        if (!users.asMap().containsKey(player.getUniqueId())) {
            users.put(player.getUniqueId(), new User(PLUGIN, player));
        }
    }

    public void unloadUser(final OfflinePlayer player) {
        if (users.asMap().containsKey(player.getUniqueId())) {
            users.invalidate(player.getUniqueId());
        }
    }

    @Override
    public void reloadConfig() {
        uuidMap.forceWriteUUIDMap();
        loadNameUUID();
    }
}
