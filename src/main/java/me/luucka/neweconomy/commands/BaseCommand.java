package me.luucka.neweconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

import static me.luucka.neweconomy.utils.Color.colorize;

public abstract class BaseCommand extends BukkitCommand {

    private static final Set<BaseCommand> registeredCommands = new HashSet<>();

    public BaseCommand(String name, String description) {
        this(name, description, null);
    }

    public BaseCommand(String name, String description, String permission, String... aliases) {
        super(name);
        this.setDescription(description);
        this.setPermission(permission);
        this.setAliases(Arrays.asList(aliases));

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(name, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        registeredCommands.add(this);
        Bukkit.getHelpMap().clear();
        List<HelpTopic> topics = new ArrayList<>();
        for (final BaseCommand bc : registeredCommands) {
            topics.add(new GenericCommandHelpTopic(bc));
        }
        Bukkit.getHelpMap().addTopic(
                new IndexHelpTopic(
                        "NewEconomy",
                        "NewEco",
                        "neweconomy.help",
                        topics,
                        "NewEconomy Help page"
                )
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        try {
            execute(new CommandSource(sender), args);
        } catch (Exception e) {
            sender.sendMessage(colorize(e.getMessage()));
        }
        return false;
    }

    public abstract void execute(CommandSource sender, String[] args) throws Exception;

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(new CommandSource(sender), args);
    }

    public abstract List<String> onTabComplete(CommandSource sender, String[] args);
}
