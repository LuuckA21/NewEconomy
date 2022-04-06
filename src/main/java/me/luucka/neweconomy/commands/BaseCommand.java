package me.luucka.neweconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class BaseCommand extends BukkitCommand {

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
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        try {
            execute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(e.getMessage());
        }
        return false;
    }

    public abstract void execute(CommandSender sender, String[] args) throws Exception;

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}
