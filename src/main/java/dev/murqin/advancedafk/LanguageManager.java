package dev.murqin.advancedafk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * LanguageManager - Multi-language support
 * 
 * @author Murqin
 */
public class LanguageManager {

    private final AdvancedAFK plugin;
    private final Map<String, String> messages = new HashMap<>();
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

    public LanguageManager(AdvancedAFK plugin) {
        this.plugin = plugin;
        reload();
    }
    
    public void reload() {
        messages.clear();
        
        String lang = plugin.getConfigManager().getLanguage();
        
        // Create lang folder if not exists
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }
        
        // Save default language files
        saveDefaultLang("en_US");
        saveDefaultLang("tr_TR");
        
        // Try to load the selected language
        File langFile = new File(langFolder, lang + ".yml");
        FileConfiguration langConfig;
        
        if (langFile.exists()) {
            langConfig = YamlConfiguration.loadConfiguration(langFile);
        } else {
            // Fallback to en_US from resources
            plugin.getLogger().warning("Language file not found: " + lang + ".yml, using en_US");
            InputStream defaultStream = plugin.getResource("lang/en_US.yml");
            if (defaultStream != null) {
                langConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
                );
            } else {
                plugin.getLogger().severe("Could not load any language file!");
                return;
            }
        }
        
        // Load all messages
        for (String key : langConfig.getKeys(false)) {
            messages.put(key, langConfig.getString(key, ""));
        }
        
        plugin.getLogger().info("Loaded language: " + lang);
    }
    
    private void saveDefaultLang(String lang) {
        File langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + lang + ".yml", false);
        }
    }
    
    /**
     * Get a raw message string (with color codes)
     */
    public String getRaw(String key) {
        return messages.getOrDefault(key, key);
    }
    
    /**
     * Get a message as Adventure Component
     */
    public Component get(String key) {
        return serializer.deserialize(getRaw(key));
    }
    
    /**
     * Get a message with placeholder replacements
     */
    public Component get(String key, String... replacements) {
        String message = getRaw(key);
        
        // Replace placeholders in pairs: key, value, key, value...
        for (int i = 0; i < replacements.length - 1; i += 2) {
            message = message.replace(replacements[i], replacements[i + 1]);
        }
        
        return serializer.deserialize(message);
    }
    
    /**
     * Get a message with prefix
     */
    public Component getWithPrefix(String key) {
        return serializer.deserialize(getRaw("prefix") + getRaw(key));
    }
    
    /**
     * Get a message with prefix and replacements
     */
    public Component getWithPrefix(String key, String... replacements) {
        String message = getRaw(key);
        
        for (int i = 0; i < replacements.length - 1; i += 2) {
            message = message.replace(replacements[i], replacements[i + 1]);
        }
        
        return serializer.deserialize(getRaw("prefix") + message);
    }
    
    /**
     * Format time duration
     */
    public String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        String format = getRaw("time-format");
        return format
            .replace("%minutes%", String.valueOf(minutes))
            .replace("%seconds%", String.valueOf(seconds));
    }
}
