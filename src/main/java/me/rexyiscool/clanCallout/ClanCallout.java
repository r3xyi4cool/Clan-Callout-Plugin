package me.rexyiscool.clanCallout;

import me.rexyiscool.clanCallout.commands.CalloutCommand;
import me.rexyiscool.clanCallout.commands.CalloutReloadCommand;
import me.rexyiscool.clanCallout.manager.CalloutManager;
import me.ulrich.clans.Clans;
import me.ulrich.clans.api.ClanAPIManager;
import me.ulrich.clans.api.PlayerAPIManager;
import me.ulrich.clans.interfaces.ClanAPI;
import me.ulrich.clans.interfaces.PlayerAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClanCallout extends JavaPlugin {

    private static ClanCallout instance;
    private CalloutManager calloutManager;
    private ClanAPI clanAPI;
    private PlayerAPI playerAPI;

    @Override
    public void onEnable() {
        instance = this;

        try {
            saveDefaultConfig();
            getLogger().info(ChatColor.DARK_GREEN + "Config File Loaded!");
        } catch (Exception e) {
            getLogger().severe(ChatColor.RED + "Failed to load config file: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            if (hookUClans()) {
                getLogger().info(ChatColor.DARK_GREEN + "UClans Hooked Successfully");
            } else {
                getLogger().severe(ChatColor.RED + "Failed to hook UClans! Disabling plugin...");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } catch (Exception e) {
            getLogger().severe(ChatColor.RED + "Error while hooking into UClans: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            setupManager();
            getLogger().info(ChatColor.DARK_GREEN + "Managers Connected");
        } catch (Exception e) {
            getLogger().severe(ChatColor.RED + "Error while connecting to the managers: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            registerCommands();
            getLogger().info(ChatColor.DARK_GREEN + "Commands registered");
        } catch (Exception e) {
            getLogger().severe(ChatColor.RED + "Error registering commands: " + e.getMessage());
            e.printStackTrace();
        }

        enableMessage();
    }

    @Override
    public void onDisable() {
        try {
            disableMessage();
        } catch (Exception e) {
            getLogger().severe(ChatColor.RED + "An error occurred while disabling ClanCallout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean hookUClans() {
        if (getServer().getPluginManager().getPlugin("UltimateClans") == null) {
            getLogger().warning("UltimateClans not found! This plugin requires UltimateClans to work.");
            return false;
        }

        try {
            Plugin uClansPlugin = getServer().getPluginManager().getPlugin("UltimateClans");

            if (uClansPlugin == null) {
                throw new IllegalStateException("UltimateClans plugin is null!");
            }

            if (uClansPlugin instanceof JavaPlugin) {

                Clans clans = (Clans) uClansPlugin;
                clanAPI = clans.getClanAPI();
                playerAPI = clans.getPlayerAPI();

                if (clanAPI == null || playerAPI == null) {
                    throw new IllegalStateException("Failed to retrieve APIs from UltimateClans!");
                }

                getLogger().info("Successfully hooked into UClans API!");
                return true;
            }

        } catch (Exception e) {
            getLogger().severe("Failed to get UltimateClans API: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private void setupManager() {
        this.calloutManager = new CalloutManager(this, clanAPI, playerAPI);
    }

    private void registerCommands() {
        this.getCommand("callout").setExecutor(new CalloutCommand(this, calloutManager));
        getCommand("calloutreload").setExecutor(new CalloutReloadCommand(this));
    }

    private void enableMessage() {
        consoleMessage(ChatColor.RED + "ClanCallout V 1.0.0");
        consoleMessage(ChatColor.GREEN + "Developed By rexyiscool");
    }

    private void disableMessage() {
        consoleMessage(ChatColor.RED + "Disabling ClanCallout...");
        consoleMessage(ChatColor.RED + "ClanCallout Disabled");
    }

    private void consoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public static ClanCallout getInstance() {
        return instance;
    }

    public CalloutManager getCalloutManager() {
        return calloutManager;
    }

    public ClanAPI getClanAPI() {
        return clanAPI;
    }

    public PlayerAPI getPlayerAPI() {
        return playerAPI;
    }
}