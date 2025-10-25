package me.rexyiscool.uclansAddons.manager;

import me.ulrich.clans.Clans;
import me.ulrich.clans.data.ClanData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanOnlineManager {

    private final Clans clansPlugin;
    private static final int CLANS_PER_PAGE = 10;

    public ClanOnlineManager(Clans clansPlugin) {
        this.clansPlugin = clansPlugin;
    }


    public void displayPage(Player player,int page){
        List<ClanData> allClans = clansPlugin.getClanAPI().getAllClansData();

        if (allClans == null || allClans.isEmpty()){
            player.sendMessage(ChatColor.RED+"No Clans Found");
            return;
        }

        List<ClanData> clansOnline = getclanMember(allClans);

        if (clansOnline.isEmpty()){
            player.sendMessage(ChatColor.RED+"No Clans have online members right now");
            return;
        }
        int totalPage = calculatePages(clansOnline.size());
        if (page > totalPage){
            player.sendMessage(ChatColor.RED+"Page "+page+" does not exist Maximum Pages : "+totalPage);
            return;
        }
        sendHeader(player,page,totalPage);
        displayClans(player,clansOnline,page);
        sendNavButton(player,page,totalPage);

    }
    private void sendHeader(Player player,int page,int tPages){
        player.sendMessage(ChatColor.GREEN+"● "+ChatColor.WHITE+
                "ᴏɴʟɪɴᴇ ᴄʟᴀɴꜱ "+ChatColor.GRAY + "(" + page + "/" + tPages + ")");
        player.sendMessage("");
    }
    private void displayClans(Player player,List<ClanData> clanWithOnline,int page){
        int start = (page-1) * CLANS_PER_PAGE;
        int stop = Math.min(start+CLANS_PER_PAGE,clanWithOnline.size());

        for (int i = start;i<stop;i++){
            ClanData clan = clanWithOnline.get(i);
            int counter = getActualOnlineCount(clan);
            int totalMembers = clan.getMembers() != null ? clan.getMembers().size() : 0;

            player.sendMessage(ChatColor.AQUA +" "+ clan.getTagNoColor() +
                    ChatColor.DARK_GRAY + " • " +
                    ChatColor.AQUA + counter +
                    ChatColor.AQUA + "/" +
                    ChatColor.AQUA + totalMembers +
                    ChatColor.AQUA + " online");
        }
        player.sendMessage("");
    }
    private void sendNavButton(Player player,int cPage,int tPage){
        TextComponent navBar = new TextComponent("");

        TextComponent left = new TextComponent("← ");
        if (cPage > 1){
            left.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            left.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/clanonline "+ (cPage-1) ));
        }else {
            left.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        }
        navBar.addExtra(left);
        navBar.addExtra(new TextComponent(
                net.md_5.bungee.api.ChatColor.YELLOW + String.valueOf(cPage) +
                        net.md_5.bungee.api.ChatColor.GRAY + "/" + tPage + " "
        ));

        TextComponent right = new TextComponent(" →");
        if (cPage < tPage){
            right.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            right.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/clanonline "+ (cPage+1) ));
        }else {
            right.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        }
        navBar.addExtra(right);
        player.spigot().sendMessage(navBar);
    }
    private int getActualOnlineCount(ClanData clan) {
        if (clan.getMembers() == null || clan.getMembers().isEmpty()) {
            return 0;
        }

        int count = 0;
        for (UUID memberUUID : clan.getMembers()) {
            if (Bukkit.getPlayer(memberUUID) != null) {
                count++;
            }
        }
        return count;
    }

    private List<ClanData> getclanMember(List<ClanData> allClans){
        List<ClanData> clanOnline = new ArrayList<>();
        for (ClanData clan: allClans){
            int actualOnline = getActualOnlineCount(clan);
            if (actualOnline > 0) {
                clanOnline.add(clan);
            }
        }
        clanOnline.sort((c1, c2) -> Integer.compare(getActualOnlineCount(c2), getActualOnlineCount(c1)));
        return clanOnline;
    }

    private int calculatePages(int totalClans){
        return (int) Math.ceil((double) totalClans / CLANS_PER_PAGE);
    }
}
