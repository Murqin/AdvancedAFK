package dev.murqin.advancedafk.commands;

import dev.murqin.advancedafk.AdvancedAFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * AFKTabCompleter - Tab completion for /afk command
 * 
 * @author Murqin
 */
public class AFKTabCompleter implements TabCompleter {

    private final AdvancedAFK plugin;

    public AFKTabCompleter(AdvancedAFK plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                 @NotNull String alias, @NotNull String[] args) {
        
        if (args.length == 1 && args[0].isEmpty()) {
            // Show [reason] placeholder when user types "/afk "
            return Collections.singletonList("[reason]");
        }
        
        return Collections.emptyList();
    }
}
