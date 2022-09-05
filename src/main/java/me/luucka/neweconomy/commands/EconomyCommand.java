package me.luucka.neweconomy.commands;

import com.google.common.collect.Lists;
import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.InsufficientPermissionException;
import me.luucka.neweconomy.exceptions.NotEnoughArgumentsException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EconomyCommand extends BaseCommand {

    private final NewEconomy plugin;

    public EconomyCommand(final NewEconomy plugin) {
        super("economy", "Economy admin command", "neweconomy.eco");
        this.plugin = plugin;
        this.setUsage("/economy <add | take | set | reset> <player> [money]");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!testPermissionSilent(sender.getSender())) throw new InsufficientPermissionException(plugin.getMessages().getNoPermission());

        if (args.length < 2) throw new NotEnoughArgumentsException(plugin.getMessages().getCommandUsage(getUsage()));

        final CommandType cmd;
        final IUser user;
        final int money;

        try {
            cmd = CommandType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException ex) {
            throw new Exception(plugin.getMessages().getCommandUsage(getUsage()));
        }

        if (cmd != CommandType.RESET && args.length < 3) throw new NotEnoughArgumentsException(plugin.getMessages().getCommandUsage(getUsage()));

        user = plugin.getUser(args[1]);

        try {
            money = cmd == CommandType.RESET ? plugin.getSettings().getStartMoney() : Integer.parseInt(args[2]);
            if (money < 0) throw new NumberFormatException();
        } catch (final NumberFormatException ex) {
            throw new Exception("<red>Please insert a positive integer");
        }

        switch (cmd) {
            case ADD -> {
                user.addMoney(money);
                sender.sendMessage(plugin.getMessages().getAddOtherAccount(user.getLastAccountName(), money));
            }
            case TAKE -> {
                user.takeMoney(money);
                sender.sendMessage(plugin.getMessages().getTakeOtherAccount(user.getLastAccountName(), money));
            }
            case SET, RESET -> {
                user.setMoney(money);
                sender.sendMessage(plugin.getMessages().getSetOtherAccount(user.getLastAccountName(), money));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (!testPermissionSilent(sender.getSender())) return Collections.emptyList();

        if (args.length == 1) {
            final List<String> options = new ArrayList<>();
            for (final CommandType ct : CommandType.values()) {
                options.add(ct.name().toLowerCase());
            }
            return options;
        } else if (args.length == 2) {
            final List<String> players = new ArrayList<>();
            for (final Player p : plugin.getServer().getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        } else if (args.length == 3 && !args[0].equalsIgnoreCase(CommandType.RESET.name())) {
            return Lists.newArrayList("1", "10", "100", "1000");
        } else {
            return Collections.emptyList();
        }
    }

    private enum CommandType {
        ADD,
        TAKE,
        SET,
        RESET
    }
}
