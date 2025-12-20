package dev.murqin.advancedafk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * TabManager - Tab list AFK prefix management
 * Uses Adventure API to safely prepend AFK prefix without breaking other plugins
 * 
 * @author Murqin
 */
public class TabManager implements Listener {

    private final AdvancedAFK plugin;
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

    public TabManager(AdvancedAFK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Update tab after a short delay to let other plugins set their names first
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            updatePlayerTab(event.getPlayer());
        }, 10L);
    }

    /**
     * Updates a player's tab list name with AFK prefix if applicable.
     * This method PRESERVES existing styling from other plugins by:
     * 1. Getting the player's current display name (set by other plugins)
     * 2. Only prepending the AFK prefix, not overwriting the entire name
     */
    public void updatePlayerTab(Player player) {
        if (!plugin.getConfigManager().isTabListEnabled()) return;
        
        UUID uuid = player.getUniqueId();
        boolean isAFK = plugin.getAFKManager().isAFK(uuid);
        
        // Get the player's current display name (preserves other plugins' formatting)
        Component currentName = player.displayName();
        
        if (isAFK) {
            // Prepend AFK prefix to existing name
            String prefixRaw = plugin.getConfigManager().getAfkPrefix();
            Component prefix = serializer.deserialize(prefixRaw);
            
            Component newName = Component.text()
                    .append(prefix)
                    .append(currentName)
                    .build();
            
            player.playerListName(newName);
        } else {
            // Restore original display name (what other plugins set)
            player.playerListName(currentName);
        }
    }

    /**
     * Updates all online players' tab list names
     */
    public void updateAllPlayerTabs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerTab(player);
        }
    }
}
