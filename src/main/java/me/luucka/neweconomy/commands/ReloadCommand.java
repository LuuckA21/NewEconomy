package me.luucka.neweconomy.commands;

import me.luucka.neweconomy.NewEconomy;
import me.luucka.neweconomy.exceptions.InsufficientPermissionException;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends BaseCommand {

    private final NewEconomy plugin;

    public ReloadCommand(final NewEconomy plugin) {
        super("neweconomy", "Reload plugin", "neweconomy.admin", "neweco");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!testPermissionSilent(sender.getSender())) throw new InsufficientPermissionException(plugin.getMessages().getNoPermission());
        plugin.reload();
        sender.sendMessage(plugin.getMessages().getReload());
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        return Collections.emptyList();
    }
}
