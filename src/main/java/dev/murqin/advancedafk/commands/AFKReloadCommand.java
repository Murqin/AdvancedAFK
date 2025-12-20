package dev.murqin.advancedafk.commands;

import dev.murqin.advancedafk.AdvancedAFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * AFKReloadCommand - /afkreload command executor
 * 
 * @author Murqin
 */
public class AFKReloadCommand implements CommandExecutor {

    private final AdvancedAFK plugin;

    public AFKReloadCommand(AdvancedAFK plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                            @NotNull String label, @NotNull String[] args) {
        
        // Permission check
        if (!sender.hasPermission("advancedafk.reload")) {
            sender.sendMessage(plugin.getLanguageManager().getWithPrefix("no-permission"));
            return true;
        }

        // Reload configuration and language
        plugin.reload();
        
        sender.sendMessage(plugin.getLanguageManager().getWithPrefix("reload-success"));
        return true;
    }
}
