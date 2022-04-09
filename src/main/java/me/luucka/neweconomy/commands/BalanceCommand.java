package me.luucka.neweconomy.commands;

import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.User;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.UserNotExistsException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalanceCommand extends BaseCommand {

    private final NewEconomy PLUGIN;

    public BalanceCommand(final NewEconomy plugin) {
        super("balance", "Show your balance", "neweconomy.bal", "bal", "money");
        this.PLUGIN = plugin;
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (args.length == 0 && sender.isPlayer()) {
            final IUser user = new User(PLUGIN, sender.getPlayer());
            sender.sendMessage(PLUGIN.getMessages().getBalance(user.getMoney()));
        } else if (args.length >= 1 && sender.hasPermission("neweconomy.bal.others")) {
            OfflinePlayer player = PLUGIN.getUserCacheManager().getPlayer(args[0]);
            if (player == null) throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(args[0]));
            final IUser user = new User(PLUGIN, player);
            sender.sendMessage(PLUGIN.getMessages().getBalanceOther(player.getName(), user.getMoney()));
        } else {
            if (sender.isPlayer()) {
                sender.sendMessage(PLUGIN.getMessages().getNoPermission());
            } else {
                sender.sendMessage(PLUGIN.getMessages().getNoConsole());
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (args.length == 1 && sender.hasPermission("neweconomy.bal.others")) {
            final List<String> players = new ArrayList<>();
            for (final Player p : PLUGIN.getServer().getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }
        return Collections.emptyList();
    }
}
