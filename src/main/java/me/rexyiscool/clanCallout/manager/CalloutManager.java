package me.rexyiscool.clanCallout.manager;

import me.rexyiscool.clanCallout.ClanCallout;
import me.ulrich.clans.data.ClanData;
import me.ulrich.clans.data.PlayerData;
import me.ulrich.clans.interfaces.ClanAPI;
import me.ulrich.clans.interfaces.PlayerAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Optional;
import java.util.UUID;

public class CalloutManager {
    private final ClanCallout plugin;
    private final ClanAPI clanAPI;
    private final PlayerAPI playerAPI;

    public CalloutManager(ClanCallout plugin, ClanAPI clanAPI, PlayerAPI playerAPI) {
        this.plugin = plugin;
        this.clanAPI = clanAPI;
        this.playerAPI = playerAPI;
    }
    public boolean isInClan(Player player) {
        return playerAPI.hasClan(player.getUniqueId());
    }
    public Optional<ClanData> getPlayerClan(Player player){
        return playerAPI.getPlayerClan(player.getUniqueId());
    }
    public boolean isLeader(Player player){
        return playerAPI.isLeader(player.getUniqueId());
    }
    public boolean isLeaderOrCoLeader(Player player) {
        Optional<PlayerData> playerDataOpt = playerAPI.getPlayerData(player.getUniqueId());
        if (!playerDataOpt.isPresent()) {
            return false;
        }
        PlayerData playerData = playerDataOpt.get();
        if (playerAPI.isLeader(player.getUniqueId())) {
            return true;
        }
        return playerData.getRole().toString().equalsIgnoreCase("COLEADER");
    }
    public int sendCalloutMsg(Player player,ClanData clanData,String message){
        String calloutTitle = plugin.getConfig().getString("callout-title", "§c⚠ CLAN CALLOUT ⚠");
        String messageColor = plugin.getConfig().getString("message-color", "§e");
        int fadeIn = plugin.getConfig().getInt("title-fade-in", 10);
        int stay = plugin.getConfig().getInt("title-stay", 70);
        int fadeOut = plugin.getConfig().getInt("title-fade-out", 20);
        String clanTag = clanData.getTag();
        int sentCount = 0;
        for(UUID uuid : clanData.getOnlineMembers()){
            Player member = Bukkit.getPlayer(uuid);
            if (member!=null && member.isOnline()){
                member.sendTitle(
                        calloutTitle,
                        messageColor + message,
                        fadeIn, stay, fadeOut
                );
                try {
                    member.playSound(member.getLocation(),
                            org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                }catch (Exception e){
                }
                sentCount++;
            }
        }
        return sentCount;
    }
    public ClanAPI getClanAPI() {
        return clanAPI;
    }

    public PlayerAPI getPlayerAPI() {
        return playerAPI;
    }
}
