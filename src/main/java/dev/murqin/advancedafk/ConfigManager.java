package dev.murqin.advancedafk;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * ConfigManager - Configuration file management
 * 
 * @author Murqin
 */
public class ConfigManager {

    private final AdvancedAFK plugin;
    
    // Language
    private String language;
    
    // Auto AFK
    private boolean autoAfkEnabled;
    private int autoAfkMinutes;
    
    // Auto Kick
    private boolean autoKickEnabled;
    private int autoKickMinutes;
    private int autoKickMinPlayers;
    
    // Triggers
    private boolean triggerMouseMovement;
    private boolean triggerSneak;
    private boolean triggerInteract;
    private boolean triggerChat;
    private boolean triggerCommand;
    private boolean triggerInventory;
    
    // Protection
    private boolean protectionEnabled;
    private boolean preventDamage;
    private boolean preventKnockback;
    private boolean lockPosition;
    
    // Tab list
    private boolean tabListEnabled;
    private String afkPrefix;
    
    // Sounds
    private boolean afkEnterSoundEnabled;
    private Sound afkEnterSound;
    private float afkEnterVolume;
    private float afkEnterPitch;
    
    private boolean afkExitSoundEnabled;
    private Sound afkExitSound;
    private float afkExitVolume;
    private float afkExitPitch;
    
    // Command
    private int commandCooldown;

    public ConfigManager(AdvancedAFK plugin) {
        this.plugin = plugin;
        reload();
    }
    
    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        
        // Language
        language = config.getString("language", "en_US");
        
        // Auto AFK
        autoAfkEnabled = config.getBoolean("auto-afk.enabled", true);
        autoAfkMinutes = config.getInt("auto-afk.timeout-minutes", 5);
        
        // Auto Kick
        autoKickEnabled = config.getBoolean("auto-kick.enabled", false);
        autoKickMinutes = config.getInt("auto-kick.timeout-minutes", 30);
        autoKickMinPlayers = config.getInt("auto-kick.min-players-online", 0);
        
        // Triggers
        triggerMouseMovement = config.getBoolean("triggers.mouse-movement", true);
        triggerSneak = config.getBoolean("triggers.sneak", true);
        triggerInteract = config.getBoolean("triggers.interact", true);
        triggerChat = config.getBoolean("triggers.chat", true);
        triggerCommand = config.getBoolean("triggers.command", true);
        triggerInventory = config.getBoolean("triggers.inventory", true);
        
        // Protection
        protectionEnabled = config.getBoolean("protection.enabled", true);
        preventDamage = config.getBoolean("protection.prevent-damage", true);
        preventKnockback = config.getBoolean("protection.prevent-knockback", true);
        lockPosition = config.getBoolean("protection.lock-position", true);
        
        // Tab list
        tabListEnabled = config.getBoolean("tab-list.enabled", true);
        afkPrefix = config.getString("tab-list.afk-prefix", "&7[&cAFK&7] ");
        
        // Sounds
        afkEnterSoundEnabled = config.getBoolean("sounds.afk-enter.enabled", true);
        afkEnterSound = parseSound(config.getString("sounds.afk-enter.sound", "BLOCK_NOTE_BLOCK_PLING"));
        afkEnterVolume = (float) config.getDouble("sounds.afk-enter.volume", 0.5);
        afkEnterPitch = (float) config.getDouble("sounds.afk-enter.pitch", 0.5);
        
        afkExitSoundEnabled = config.getBoolean("sounds.afk-exit.enabled", true);
        afkExitSound = parseSound(config.getString("sounds.afk-exit.sound", "BLOCK_NOTE_BLOCK_PLING"));
        afkExitVolume = (float) config.getDouble("sounds.afk-exit.volume", 0.5);
        afkExitPitch = (float) config.getDouble("sounds.afk-exit.pitch", 1.0);
        
        // Command
        commandCooldown = config.getInt("command-cooldown", 3);
    }
    
    private Sound parseSound(String name) {
        try {
            return Sound.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound: " + name + ", using default.");
            return Sound.BLOCK_NOTE_BLOCK_PLING;
        }
    }
    
    // ==================== GETTERS ====================
    
    public String getLanguage() { return language; }
    
    public boolean isAutoAfkEnabled() { return autoAfkEnabled; }
    public int getAutoAfkMinutes() { return autoAfkMinutes; }
    
    public boolean isAutoKickEnabled() { return autoKickEnabled; }
    public int getAutoKickMinutes() { return autoKickMinutes; }
    public int getAutoKickMinPlayers() { return autoKickMinPlayers; }
    
    public boolean isTriggerMouseMovement() { return triggerMouseMovement; }
    public boolean isTriggerSneak() { return triggerSneak; }
    public boolean isTriggerInteract() { return triggerInteract; }
    public boolean isTriggerChat() { return triggerChat; }
    public boolean isTriggerCommand() { return triggerCommand; }
    public boolean isTriggerInventory() { return triggerInventory; }
    
    public boolean isProtectionEnabled() { return protectionEnabled; }
    public boolean isPreventDamage() { return protectionEnabled && preventDamage; }
    public boolean isPreventKnockback() { return protectionEnabled && preventKnockback; }
    public boolean isLockPosition() { return protectionEnabled && lockPosition; }
    
    public boolean isTabListEnabled() { return tabListEnabled; }
    public String getAfkPrefix() { return afkPrefix; }
    
    public boolean isAfkEnterSoundEnabled() { return afkEnterSoundEnabled; }
    public Sound getAfkEnterSound() { return afkEnterSound; }
    public float getAfkEnterVolume() { return afkEnterVolume; }
    public float getAfkEnterPitch() { return afkEnterPitch; }
    
    public boolean isAfkExitSoundEnabled() { return afkExitSoundEnabled; }
    public Sound getAfkExitSound() { return afkExitSound; }
    public float getAfkExitVolume() { return afkExitVolume; }
    public float getAfkExitPitch() { return afkExitPitch; }
    
    public int getCommandCooldown() { return commandCooldown; }
}
