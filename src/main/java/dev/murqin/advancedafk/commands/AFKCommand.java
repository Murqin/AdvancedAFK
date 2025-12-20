package dev.murqin.advancedafk.commands;

import dev.murqin.advancedafk.AdvancedAFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * AFKCommand - /afk command executor
 * 
 * @author Murqin
 */
public class AFKCommand implements CommandExecutor {

    private final AdvancedAFK plugin;

    public AFKCommand(AdvancedAFK plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                            @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getLanguageManager().getWithPrefix("player-only"));
            return true;
        }

        // Permission check
        if (!player.hasPermission("advancedafk.use")) {
            player.sendMessage(plugin.getLanguageManager().getWithPrefix("no-permission"));
            return true;
        }

        // Cooldown check (unless has bypass permission)
        if (!player.hasPermission("advancedafk.bypass.cooldown") && 
                plugin.getAFKManager().isOnCooldown(player.getUniqueId())) {
            long remaining = plugin.getAFKManager().getRemainingCooldown(player.getUniqueId()) / 1000;
            player.sendMessage(plugin.getLanguageManager().getWithPrefix("cooldown",
                    "%seconds%", String.valueOf(remaining)));
            return true;
        }

        // Get optional reason
        String reason = args.length > 0 ? String.join(" ", args) : null;
        
        // Toggle AFK
        plugin.getAFKManager().toggleAFK(player, reason);
        plugin.getAFKManager().setCooldown(player.getUniqueId());

        return true;
    }
}
