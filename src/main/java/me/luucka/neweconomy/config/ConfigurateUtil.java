package me.luucka.neweconomy.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;

public final class ConfigurateUtil {

    private ConfigurateUtil() {}

    public static Set<String> getRootNodeKeys(final BaseConfiguration config) {
        return getKeys(config.getRootNode());
    }

    public static Set<String> getKeys(final CommentedConfigurationNode node) {
        if (node == null || !node.isMap()) {
            return Collections.emptySet();
        }

        final Set<String> keys = new LinkedHashSet<>();
        for (Object obj : node.childrenMap().keySet()) {
            keys.add(String.valueOf(obj));
        }
        return keys;
    }

    public static Map<String, CommentedConfigurationNode> getMap(final CommentedConfigurationNode node) {
        if (node == null || !node.isMap()) {
            return Collections.emptyMap();
        }

        final Map<String, CommentedConfigurationNode> map = new LinkedHashMap<>();
        for (Map.Entry<Object, CommentedConfigurationNode> entry : node.childrenMap().entrySet()) {
            map.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return map;
    }

    public static Map<String, Object> getRawMap(final BaseConfiguration config, final String key) {
        if (config == null || key == null) {
            return Collections.emptyMap();
        }
        return getRawMap(config.getSection(key));
    }

    public static Map<String, Object> getRawMap(final CommentedConfigurationNode node) {
        if (node == null || !node.isMap()) {
            return Collections.emptyMap();
        }

        final Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<Object, CommentedConfigurationNode> entry : node.childrenMap().entrySet()) {
            map.put(String.valueOf(entry.getKey()), entry.getValue().raw());
        }
        return map;
    }

    public static List<Map<?, ?>> getMapList(final CommentedConfigurationNode node) {
        List<?> list = null;
        try {
            list = node.getList(Object.class);
        } catch (SerializationException ignored) {
        }
        final List<Map<?, ?>> result = new ArrayList<>();

        if (list == null) {
            return result;
        }

        for (Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }
        return result;
    }

    public static boolean isDouble(final CommentedConfigurationNode node) {
        return node != null && node.raw() instanceof Double;
    }

    public static boolean isInt(final CommentedConfigurationNode node) {
        return node != null && node.raw() instanceof Integer;
    }

    public static boolean isString(final CommentedConfigurationNode node) {
        return node != null && node.raw() instanceof String;
    }

}
