package dev.murqin.advancedafk.hooks;

import dev.murqin.advancedafk.AdvancedAFK;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AFKPlaceholders - PlaceholderAPI expansion
 * 
 * Provides the following placeholders:
 * - %advancedafk_status% - Returns "AFK" if player is AFK, empty string otherwise
 * - %advancedafk_status_formatted% - Returns "[AFK]" if AFK, empty string otherwise
 * - %advancedafk_time% - Returns formatted AFK duration (e.g., "5m 30s")
 * - %advancedafk_time_minutes% - Returns AFK duration in minutes
 * - %advancedafk_time_seconds% - Returns AFK duration in seconds
 * - %advancedafk_reason% - Returns AFK reason if set
 * - %advancedafk_is_afk% - Returns "true" or "false"
 * 
 * @author Murqin
 */
public class AFKPlaceholders extends PlaceholderExpansion {

    private final AdvancedAFK plugin;

    public AFKPlaceholders(AdvancedAFK plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "advancedafk";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // Stay registered after PlaceholderAPI reload
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null || !player.isOnline()) {
            return "";
        }

        boolean isAFK = plugin.getAFKManager().isAFK(player.getUniqueId());

        switch (params.toLowerCase()) {
            case "status":
                return isAFK ? "AFK" : "";
                
            case "status_formatted":
                return isAFK ? "[AFK]" : "";
                
            case "time":
                if (!isAFK) return "";
                return plugin.getLanguageManager().formatTime(
                        plugin.getAFKManager().getAFKDuration(player.getUniqueId()));
                
            case "time_minutes":
                if (!isAFK) return "0";
                return String.valueOf(plugin.getAFKManager().getAFKMinutes(player.getUniqueId()));
                
            case "time_seconds":
                if (!isAFK) return "0";
                return String.valueOf(plugin.getAFKManager().getAFKDuration(player.getUniqueId()) / 1000);
                
            case "reason":
                if (!isAFK) return "";
                String reason = plugin.getAFKManager().getAFKReason(player.getUniqueId());
                return reason != null ? reason : "";
                
            case "is_afk":
                return String.valueOf(isAFK);
                
            default:
                return null;
        }
    }
}
