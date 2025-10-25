package me.rexyiscool.uclansAddons.commands;

import me.rexyiscool.uclansAddons.UClansAddons;
import me.rexyiscool.uclansAddons.manager.CalloutManager;
import me.ulrich.clans.data.ClanData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CalloutCommand implements CommandExecutor {
    private final UClansAddons plugin;
    private final CalloutManager calloutManager;

    public CalloutCommand(UClansAddons plugin, CalloutManager calloutManager) {
        this.plugin = plugin;
        this.calloutManager = calloutManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly Players can execute this command");
            return true;
        }
        Player player = (Player) sender;

        if (!calloutManager.isInClan(player)) {
            player.sendMessage("§cJoin a clan to send callout messages!");
            return true;
        }
        if (!calloutManager.isLeaderOrCoLeader(player)) {
            player.sendMessage("§cOnly clan leaders and co-leaders can send callouts!");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage("§cUsage: /callout <message>");
            return true;
        }
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }
        String message = messageBuilder.toString();
        if (message.length() > 100) {
            player.sendMessage("§cCallout message is too long! Maximum 100 characters.");
            return true;
        }

        Optional<ClanData> clanDataOp = calloutManager.getPlayerClan(player);
        if (!clanDataOp.isPresent()) {
            player.sendMessage("§cError: no clan data found!");
            return true;
        }
        ClanData clanData = clanDataOp.get();
        if (clanData.getOnlineMembers().size() <= 1) {
            player.sendMessage("§cNo other clan members are online to receive the callout!");
            return true;
        }

        int sentCount = calloutManager.sendCalloutMsg(player, clanData, message);
        for (String line : plugin.getConfig().getStringList("messages.callout-sent")) {
            player.sendMessage(line.replace("%count%", String.valueOf(sentCount))
                    .replace("&", "§"));
        }

        return true;
    }
}