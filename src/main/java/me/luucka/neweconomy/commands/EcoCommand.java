package me.luucka.neweconomy.commands;

import com.google.common.collect.Lists;
import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.User;
import me.luucka.neweconomy.api.IUser;
import me.luucka.neweconomy.exceptions.NotEnoughArgumentsException;
import me.luucka.neweconomy.exceptions.UserNotExistsException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.luucka.neweconomy.utils.Color.colorize;

public class EcoCommand extends BaseCommand {

    private final NewEconomy PLUGIN;

    public EcoCommand(final NewEconomy plugin) {
        super("economy", "Economy admin commands", "neweconomy.eco", "eco");
        this.PLUGIN = plugin;
        this.setUsage("/economy <add | take | set | reset> <player> [money]");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (args.length < 2) throw new NotEnoughArgumentsException(PLUGIN.getMessages().getCommandUsage(getUsage()));

        final CommandType cmd;
        final OfflinePlayer player;
        final int money;

        try {
            cmd = CommandType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException ex) {
            throw new Exception(colorize(getUsage()));
        }

        if (cmd != CommandType.RESET && args.length < 3) throw new NotEnoughArgumentsException(PLUGIN.getMessages().getCommandUsage(getUsage()));

        player = PLUGIN.getUserCacheManager().getPlayer(args[1]);
        if (player == null) throw new UserNotExistsException(PLUGIN.getMessages().getUserNotExists(args[1]));

        try {
            money = cmd == CommandType.RESET ? PLUGIN.getSettings().getStartMoney() : Integer.parseInt(args[2]);
            if (money < 0) throw new NumberFormatException();
        } catch (final NumberFormatException ex) {
            throw new Exception("&cPlease insert a positive integer");
        }

        final IUser user = new User(PLUGIN, player);

        switch (cmd) {
            case ADD -> {
                user.addMoney(money);
                sender.sendMessage(PLUGIN.getMessages().getAddOtherAccount(player.getName(), money));
            }
            case TAKE -> {
                user.takeMoney(money);
                sender.sendMessage(PLUGIN.getMessages().getTakeOtherAccount(player.getName(), money));
            }
            case SET, RESET -> {
                user.setMoney(money);
                sender.sendMessage(PLUGIN.getMessages().getSetOtherAccount(player.getName(), money));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (args.length == 1) {
            final List<String> options = new ArrayList<>();
            for (final CommandType ct : CommandType.values()) {
                options.add(ct.name().toLowerCase());
            }
            return options;
        } else if (args.length == 2) {
            final List<String> players = new ArrayList<>();
            for (final Player p : PLUGIN.getServer().getOnlinePlayers()) {
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
