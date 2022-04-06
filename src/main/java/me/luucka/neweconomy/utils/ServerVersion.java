package me.luucka.neweconomy.utils;

import org.bukkit.Bukkit;

public class ServerVersion {

    public static final int MAJOR;

    public static final int MINOR;

    public static final int PATCH;

    private ServerVersion() {}

    static {
        final String[] version = Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-")).split("\\.");
        MAJOR = Integer.parseInt(version[0]);
        MINOR = Integer.parseInt(version[1]);
        PATCH = Integer.parseInt(version[2]);
    }
}
