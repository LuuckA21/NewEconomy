package me.luucka.neweconomy.commands;

import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.api.IUser;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalanceCommand extends BaseCommand {

    private final NewEconomy plugin;

    public BalanceCommand(final NewEconomy plugin) {
        super("balance", "Show your balance", "neweconomy.bal", "bal");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (args.length == 0 && sender.isPlayer()) {
            sender.sendMessage(plugin.getMessages().getBalance(sender.getUser(plugin).getMoney()));
        } else if (args.length >= 1 && sender.hasPermission("neweconomy.bal.others")) {
            final IUser user = plugin.getUser(args[0]);
            sender.sendMessage(plugin.getMessages().getBalanceOther(user.getLastAccountName(), user.getMoney()));
        } else {
            if (sender.isPlayer()) {
                throw new Exception(plugin.getMessages().getNoPermission());
            } else {
                sender.sendMessage(plugin.getMessages().getNoConsole());
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (args.length == 1 && sender.hasPermission("neweconomy.bal.others")) {
            final List<String> players = new ArrayList<>();
            for (final Player p : plugin.getServer().getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }
        return Collections.emptyList();
    }
}
