package me.luucka.neweconomy.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Color {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    private Color() {
    }

    public static Component colorize(final String input) {
        StringBuilder sb = new StringBuilder(input);
        Matcher m = HEX_PATTERN.matcher(sb);
        while (m.find()) {
            sb.replace(m.start(), m.end(), "<" + sb.substring(m.start(), m.end()) + ">");
        }

        return MiniMessage.miniMessage().deserialize(
                sb.toString()
                        .replace("&0", "<black>")
                        .replace("&1", "<dark_blue>")
                        .replace("&2", "<dark_green>")
                        .replace("&3", "<dark_aqua>")
                        .replace("&4", "<dark_red>")
                        .replace("&5", "<dark_purple>")
                        .replace("&6", "<gold>")
                        .replace("&7", "<grey>")
                        .replace("&8", "<dark_grey>")
                        .replace("&9", "<blue>")
                        .replace("&a", "<green>")
                        .replace("&b", "<aqua>")
                        .replace("&c", "<red>")
                        .replace("&d", "<light_purple>")
                        .replace("&e", "<yellow>")
                        .replace("&f", "<white>")
                        .replace("&k", "<obf>")
                        .replace("&l", "<b>")
                        .replace("&m", "<st>")
                        .replace("&n", "<u>")
                        .replace("&o", "<i>")
                        .replace("&r", "<r>")
        );

    }

}
