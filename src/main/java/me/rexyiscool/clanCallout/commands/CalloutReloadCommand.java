package me.rexyiscool.clanCallout.commands;

import me.rexyiscool.clanCallout.ClanCallout;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CalloutReloadCommand implements CommandExecutor {
    private final ClanCallout plugin;

    public CalloutReloadCommand(ClanCallout plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("callout.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        try {
            plugin.reloadConfig();
            sender.sendMessage("§7ClanCallout configuration Reloaded.");
        } catch (Exception e) {
            sender.sendMessage("§cError reloading config: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}
