package me.rexyiscool.uclansAddons.commands;
import me.rexyiscool.uclansAddons.UClansAddons;
import me.rexyiscool.uclansAddons.manager.ClanOnlineManager;
import me.rexyiscool.uclansAddons.manager.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanOnlineCommand implements CommandExecutor{
    private final UClansAddons plugin;
    private final ClanOnlineManager clanOnlineManager;

    public ClanOnlineCommand(UClansAddons plugin, ClanOnlineManager clanOnlineManager){
        this.plugin = plugin;
        this.clanOnlineManager = clanOnlineManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("§cOnly Players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        int cooldownSeconds = plugin.getConfig().getInt("clanonline-cooldown-seconds", 30);
        long timeLeft = CooldownManager.getCooldown(player, "clanonline", cooldownSeconds);
        if (timeLeft > 0) {
            player.sendMessage(ChatColor.RED + "Wait " + timeLeft + " seconds before using this command again!");
            return true;
        }

        int page = 1;

        if (args.length>0){
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1){
                    player.sendMessage(ChatColor.RED+"Page number must be greater than 0");
                    return true;
                }
            }catch (NumberFormatException e){
                player.sendMessage(ChatColor.RED+"Invalid page number");
                return true;
            }
        }
        clanOnlineManager.displayPage(player,page);
        CooldownManager.setCooldown(player, "clanonline");
        return true;
    }
}
