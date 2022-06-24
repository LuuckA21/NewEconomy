package me.luucka.neweconomy.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.api.IUser;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderNewEconomy extends PlaceholderExpansion {

    private final NewEconomy PLUGIN;

    public PlaceholderNewEconomy(final NewEconomy plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "neweconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return PLUGIN.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return PLUGIN.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        params = params.toLowerCase();
        IUser user = PLUGIN.getUser(player);

        return switch (params) {
            case "balance" -> Integer.toString(user.getMoney());
            case "last-account-name" -> user.getLastAccountName();
            default -> null;
        };
    }
}
