package me.luucka.neweconomy.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Color {

    private Color() {
    }

    public static Component colorize(final String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

}
