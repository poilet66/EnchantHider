package me.poilet66.enchanthider.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if(args.length == 1 && sender.hasPermission("enchanthider.config")) {
            return Arrays.asList("add", "remove", "list");
        }

        return null;
    }
}
