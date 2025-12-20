package dev.murqin.advancedafk;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AFKManager - Core AFK logic with god mode protection
 * 
 * @author Murqin
 */
public class AFKManager implements Listener {

    private final AdvancedAFK plugin;

    // Thread-safe AFK Data
    private final Map<UUID, Long> afkPlayers = new ConcurrentHashMap<>();
    private final Map<UUID, String> afkReasons = new ConcurrentHashMap<>();
    private final Map<UUID, Location> afkLocations = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastActivity = new ConcurrentHashMap<>();
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    private BukkitTask autoAfkTask;
    private BukkitTask autoKickTask;
    
    // Rotation tolerance - prevent server-side micro-changes from breaking AFK
    private static final float ROTATION_TOLERANCE = 0.5f;

    public AFKManager(AdvancedAFK plugin) {
        this.plugin = plugin;
        startAutoAfkTask();
        startAutoKickTask();
    }

    // ==================== TASKS ====================

    private void startAutoAfkTask() {
        autoAfkTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!plugin.getConfigManager().isAutoAfkEnabled()) return;

            long afkThreshold = plugin.getConfigManager().getAutoAfkMinutes() * 60 * 1000L;
            long now = System.currentTimeMillis();

            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                if (isAFK(uuid)) continue;

                Long lastMove = lastActivity.get(uuid);
                if (lastMove == null) {
                    lastActivity.put(uuid, now);
                    continue;
                }

                if (now - lastMove > afkThreshold) {
                    setAFK(player, null, true);
                }
            }
        }, 20 * 30L, 20 * 30L);
    }

    private void startAutoKickTask() {
        autoKickTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!plugin.getConfigManager().isAutoKickEnabled()) return;
            
            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            if (onlinePlayers < plugin.getConfigManager().getAutoKickMinPlayers()) return;

            long kickThreshold = plugin.getConfigManager().getAutoKickMinutes() * 60 * 1000L;

            for (UUID uuid : afkPlayers.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !player.isOnline()) continue;
                
                // Check bypass permission
                if (player.hasPermission("advancedafk.bypass.kick")) continue;

                long afkDuration = getAFKDuration(uuid);
                if (afkDuration >= kickThreshold) {
                    // Kick the player
                    String kickReason = plugin.getLanguageManager().getRaw("kick-reason");
                    player.kick(plugin.getLanguageManager().get("kick-reason"));
                    
                    // Broadcast kick message
                    Bukkit.broadcast(plugin.getLanguageManager().get("kick-broadcast",
                            "%player%", player.getName()));
                }
            }
        }, 20 * 60L, 20 * 60L); // Check every minute
    }

    // ==================== AFK METHODS ====================

    public void toggleAFK(Player player, String reason) {
        if (isAFK(player.getUniqueId())) {
            removeAFK(player, true);
        } else {
            setAFK(player, reason, false);
        }
    }

    public void setAFK(Player player, String reason, boolean isAuto) {
        UUID uuid = player.getUniqueId();

        Location afkLocation = player.getLocation().clone();

        afkPlayers.put(uuid, System.currentTimeMillis());
        afkLocations.put(uuid, afkLocation);

        if (reason != null && !reason.trim().isEmpty()) {
            afkReasons.put(uuid, reason);
        }

        player.setCollidable(false);
        player.setVelocity(new Vector(0, 0, 0));

        // Update tab list
        plugin.getTabManager().updatePlayerTab(player);

        // Play sound
        if (plugin.getConfigManager().isAfkEnterSoundEnabled()) {
            player.playSound(player.getLocation(),
                    plugin.getConfigManager().getAfkEnterSound(),
                    plugin.getConfigManager().getAfkEnterVolume(),
                    plugin.getConfigManager().getAfkEnterPitch());
        }

        // Broadcast message
        String reasonSuffix = "";
        if (reason != null && !reason.trim().isEmpty()) {
            reasonSuffix = plugin.getLanguageManager().getRaw("afk-enter-reason")
                    .replace("%reason%", reason);
        }
        
        Bukkit.broadcast(plugin.getLanguageManager().get("afk-enter",
                "%player%", player.getName(),
                "%reason%", reasonSuffix));

        // Message to player
        player.sendMessage(plugin.getLanguageManager().getWithPrefix("afk-self-enter"));
    }

    public void removeAFK(Player player, boolean broadcast) {
        UUID uuid = player.getUniqueId();

        afkPlayers.remove(uuid);
        afkReasons.remove(uuid);
        afkLocations.remove(uuid);

        player.setCollidable(true);

        // Update tab list
        plugin.getTabManager().updatePlayerTab(player);

        // Play sound
        if (plugin.getConfigManager().isAfkExitSoundEnabled()) {
            player.playSound(player.getLocation(),
                    plugin.getConfigManager().getAfkExitSound(),
                    plugin.getConfigManager().getAfkExitVolume(),
                    plugin.getConfigManager().getAfkExitPitch());
        }

        if (broadcast) {
            Bukkit.broadcast(plugin.getLanguageManager().get("afk-exit",
                    "%player%", player.getName()));
            player.sendMessage(plugin.getLanguageManager().getWithPrefix("afk-self-exit"));
        }
    }

    public void updateActivity(Player player, boolean triggerEnabled) {
        UUID uuid = player.getUniqueId();

        if (isAFK(uuid) && triggerEnabled) {
            removeAFK(player, true);
        }

        lastActivity.put(uuid, System.currentTimeMillis());
    }

    // ==================== GETTERS & HELPERS ====================

    public boolean isAFK(UUID uuid) { return afkPlayers.containsKey(uuid); }
    public String getAFKReason(UUID uuid) { return afkReasons.get(uuid); }

    public long getAFKDuration(UUID uuid) {
        Long start = afkPlayers.get(uuid);
        return start == null ? 0 : System.currentTimeMillis() - start;
    }

    public int getAFKMinutes(UUID uuid) {
        return (int) (getAFKDuration(uuid) / 60000);
    }

    public boolean isOnCooldown(UUID uuid) {
        if (plugin.getConfigManager().getCommandCooldown() <= 0) return false;
        Long last = cooldowns.get(uuid);
        long cooldownMs = plugin.getConfigManager().getCommandCooldown() * 1000L;
        return last != null && (System.currentTimeMillis() - last) < cooldownMs;
    }

    public long getRemainingCooldown(UUID uuid) {
        Long last = cooldowns.get(uuid);
        long cooldownMs = plugin.getConfigManager().getCommandCooldown() * 1000L;
        return last == null ? 0 : Math.max(0, cooldownMs - (System.currentTimeMillis() - last));
    }

    public void setCooldown(UUID uuid) {
        cooldowns.put(uuid, System.currentTimeMillis());
    }

    public void shutdown() {
        if (autoAfkTask != null) autoAfkTask.cancel();
        if (autoKickTask != null) autoKickTask.cancel();

        for (UUID uuid : afkPlayers.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setCollidable(true);
            }
        }
        afkPlayers.clear();
        afkReasons.clear();
        afkLocations.clear();
        lastActivity.clear();
        cooldowns.clear();
    }

    // ==================== EVENT HANDLERS ====================

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!plugin.getConfigManager().isPreventDamage()) return;
        if (event.getEntity() instanceof Player player && isAFK(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().isPreventDamage()) return;
        if (event.getEntity() instanceof Player player && isAFK(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityKnockback(EntityKnockbackByEntityEvent event) {
        if (!plugin.getConfigManager().isPreventKnockback()) return;
        if (event.getEntity() instanceof Player player && isAFK(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        if (!plugin.getConfigManager().isPreventKnockback()) return;
        if (isAFK(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    /**
     * GOD MODE LOGIC:
     * - Position change (X, Y, Z) -> CANCEL (Teleport back) -> AFK NOT BROKEN
     * - Rotation change (Yaw, Pitch) -> AFK IS BROKEN (if trigger enabled)
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        // If not AFK, just update activity time for position changes
        if (!isAFK(uuid)) {
            if (from.getBlockX() != to.getBlockX() ||
                    from.getBlockY() != to.getBlockY() ||
                    from.getBlockZ() != to.getBlockZ()) {
                lastActivity.put(uuid, System.currentTimeMillis());
            }
            return;
        }

        // AFK player checks
        
        // 1. Rotation Check (Mouse Movement) -> AFK BROKEN (if trigger enabled)
        if (plugin.getConfigManager().isTriggerMouseMovement()) {
            float yawDiff = Math.abs(from.getYaw() - to.getYaw());
            float pitchDiff = Math.abs(from.getPitch() - to.getPitch());
            
            if (yawDiff > ROTATION_TOLERANCE || pitchDiff > ROTATION_TOLERANCE) {
                updateActivity(player, true);
                return;
            }
        }

        // 2. Position Check (Walking, Jumping, Pushing) -> TELEPORT BACK (AFK NOT BROKEN)
        if (plugin.getConfigManager().isLockPosition()) {
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                Location target = from.clone();
                target.setYaw(to.getYaw());
                target.setPitch(to.getPitch());
                event.setTo(target);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        updateActivity(event.getPlayer(), plugin.getConfigManager().isTriggerSneak());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        updateActivity(event.getPlayer(), plugin.getConfigManager().isTriggerInteract());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player player) {
            updateActivity(player, plugin.getConfigManager().isTriggerInventory());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // Schedule on main thread
        Bukkit.getScheduler().runTask(plugin, () -> {
            updateActivity(event.getPlayer(), plugin.getConfigManager().isTriggerChat());
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        afkPlayers.remove(uuid);
        afkReasons.remove(uuid);
        afkLocations.remove(uuid);
        lastActivity.remove(uuid);
        cooldowns.remove(uuid);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        lastActivity.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/afk")) return;
        updateActivity(event.getPlayer(), plugin.getConfigManager().isTriggerCommand());
    }
}
