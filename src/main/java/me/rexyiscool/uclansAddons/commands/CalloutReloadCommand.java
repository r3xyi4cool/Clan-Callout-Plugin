package me.rexyiscool.uclansAddons.commands;

import me.rexyiscool.uclansAddons.UClansAddons;
import me.rexyiscool.uclansAddons.manager.CooldownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CalloutReloadCommand implements CommandExecutor {
    private final UClansAddons plugin;
    private final CooldownManager cooldownManager;

    public CalloutReloadCommand(UClansAddons plugin, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("callout.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        try {
            plugin.reloadConfig();
            cooldownManager.clearAllCooldowns();
            sender.sendMessage("§7ClanCallout configuration and CoolDown Reloaded.");
        } catch (Exception e) {
            sender.sendMessage("§cError reloading config: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}
