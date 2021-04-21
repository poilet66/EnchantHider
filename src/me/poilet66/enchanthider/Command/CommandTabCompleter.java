package me.poilet66.enchanthider.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String lbl, String[] args) {

        if(args.length == 1 && sender.hasPermission("enchanthider.config")) {
            return Arrays.asList("add", "remove", "list");
        }
        if(args.length == 2 && sender.hasPermission("enchanthider.config")) {
            return Stream.of(Enchantment.values())
                    .map(s -> s.getKey().toString())
                    .map(s -> s.split(":")[1])
                    .map(s -> s.toLowerCase())
                    .filter(s -> s.startsWith(args[1]))
                    .collect(Collectors.toList());
        }

        return null;
    }

}
