package me.luucka.neweconomy.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.api.IUser;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderNewEconomy extends PlaceholderExpansion {

    private final NewEconomy plugin;

    public PlaceholderNewEconomy(final NewEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "neweconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        params = params.toLowerCase();
        final IUser user = plugin.getUser(player);

        return switch (params) {
            case "balance" -> Integer.toString(user.getMoney());
            case "last-account-name" -> user.getLastAccountName();
            default -> null;
        };
    }
}
