package me.luucka.neweconomy.commands;

import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.InsufficientPermissionException;
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
            sender.sendMessage(PLUGIN.getMessages().getBalance(sender.getUser(PLUGIN).getMoney()));
        } else if (args.length >= 1 && sender.hasPermission("neweconomy.bal.others")) {
            final IUser user = PLUGIN.getUserMap().getUser(args[0]);
            sender.sendMessage(PLUGIN.getMessages().getBalanceOther(user.getLastAccountName(), user.getMoney()));
        } else {
            if (sender.isPlayer()) {
                throw new InsufficientPermissionException(PLUGIN.getMessages().getNoPermission());
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
