package dev.murqin.advancedafk;

import dev.murqin.advancedafk.commands.AFKCommand;
import dev.murqin.advancedafk.commands.AFKReloadCommand;
import dev.murqin.advancedafk.commands.AFKTabCompleter;
import dev.murqin.advancedafk.hooks.AFKPlaceholders;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * AdvancedAFK - Advanced AFK plugin with god mode protection
 * 
 * @author Murqin
 */
public class AdvancedAFK extends JavaPlugin {

    private static AdvancedAFK instance;
    
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private AFKManager afkManager;
    private TabManager tabManager;
    
    // bStats plugin ID (https://bstats.org/plugin/bukkit/AdvancedAFK)
    private static final int BSTATS_PLUGIN_ID = 28448;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        configManager = new ConfigManager(this);
        languageManager = new LanguageManager(this);
        afkManager = new AFKManager(this);
        tabManager = new TabManager(this);
        
        // Register events
        Bukkit.getPluginManager().registerEvents(afkManager, this);
        Bukkit.getPluginManager().registerEvents(tabManager, this);
        
        // Register commands
        getCommand("afk").setExecutor(new AFKCommand(this));
        getCommand("afk").setTabCompleter(new AFKTabCompleter(this));
        getCommand("afkreload").setExecutor(new AFKReloadCommand(this));
        
        // Initialize bStats
        new Metrics(this, BSTATS_PLUGIN_ID);
        
        // Hook into PlaceholderAPI if available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AFKPlaceholders(this).register();
            getLogger().info("PlaceholderAPI hook registered!");
        }
        
        getLogger().info("AdvancedAFK v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (afkManager != null) {
            afkManager.shutdown();
        }
        getLogger().info("AdvancedAFK disabled!");
    }
    
    public void reload() {
        configManager.reload();
        languageManager.reload();
        getLogger().info("Configuration reloaded!");
    }
    
    // ==================== GETTERS ====================
    
    public static AdvancedAFK getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
    
    public AFKManager getAFKManager() {
        return afkManager;
    }
    
    public TabManager getTabManager() {
        return tabManager;
    }
}
