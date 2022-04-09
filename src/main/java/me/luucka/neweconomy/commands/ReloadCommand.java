package me.luucka.neweconomy.commands;

import me.luucka.neweconomy.NewEconomy;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends BaseCommand {

    private final NewEconomy PLUGIN;

    public ReloadCommand(final NewEconomy plugin) {
        super("neweconomy", "Reload plugin", "neweconomy.admin", "neweco", "neco");
        this.PLUGIN = plugin;
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        PLUGIN.reload();
        sender.sendMessage(PLUGIN.getMessages().getReload());
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        return Collections.emptyList();
    }
}
