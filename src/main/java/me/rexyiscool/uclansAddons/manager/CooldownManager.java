package me.rexyiscool.uclansAddons.manager;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
    private static final HashMap<String, HashMap<UUID, Long>> cooldowns = new HashMap<>();

    public static long getCooldown(Player player, String commandName, int cooldownSeconds) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        cooldowns.putIfAbsent(commandName, new HashMap<>());
        HashMap<UUID, Long> commandCooldowns = cooldowns.get(commandName);

        if (commandCooldowns.containsKey(uuid)) {
            long lastUsed = commandCooldowns.get(uuid);
            long timeLeft = ((lastUsed / 1000) + cooldownSeconds) - (currentTime / 1000);
            return Math.max(timeLeft, 0);
        }
        return 0;
    }
    public static void setCooldown(Player player, String commandName) {
        UUID uuid = player.getUniqueId();
        cooldowns.putIfAbsent(commandName, new HashMap<>());
        cooldowns.get(commandName).put(uuid, System.currentTimeMillis());
    }
    public static void clearCooldown(Player player, String commandName) {
        HashMap<UUID, Long> commandCooldowns = cooldowns.get(commandName);
        if (commandCooldowns != null) {
            commandCooldowns.remove(player.getUniqueId());
        }
    }
    public static void clearAllCooldowns() {
        cooldowns.clear();
    }
}
