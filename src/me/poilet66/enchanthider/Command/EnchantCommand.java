package me.poilet66.enchanthider.Command;

import me.poilet66.enchanthider.EnchantHider;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EnchantCommand implements CommandExecutor{

    private final EnchantHider main;
    private List<String> bannedEnchants = new ArrayList<String>();

    public EnchantCommand(EnchantHider main) {
        this.main = main;
        bannedEnchants = main.getConfig().getStringList("bannedEnchants");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("enchanthider.config")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid subcommand");
            return false;
        }
        if(args[0].equalsIgnoreCase("add")) {
            if(!main.isNameValid(args[1].toLowerCase())) {
                sender.sendMessage(String.format(ChatColor.RED + "%s is not a valid enchantment name, check console for link to list of valid names", StringUtils.capitalize(args[1])));
                return true;
            }
            if(bannedEnchants.contains(args[1])) {
                sender.sendMessage(String.format(ChatColor.RED + "%s is already a banned enchantment", StringUtils.capitalize(args[1])));
                return true;
            }
            bannedEnchants.add(args[1].toLowerCase());
            main.getConfig().set("bannedEnchants", bannedEnchants);
            main.saveConfig();
            sender.sendMessage(String.format(ChatColor.GREEN + "%s added to banned enchants", StringUtils.capitalize(args[1])));
            return true;
        }
        if(args[0].equalsIgnoreCase("remove")) {
            if(!bannedEnchants.contains(args[1])) {
                sender.sendMessage(String.format(ChatColor.RED + "%s is not a banned enchantment", StringUtils.capitalize(args[1])));
                return true;
            }
            bannedEnchants.remove(args[1]);
            main.getConfig().set("bannedEnchants", bannedEnchants);
            main.saveConfig();
            sender.sendMessage(String.format(ChatColor.GREEN + "%s removed from banned enchants", StringUtils.capitalize(args[1])));
            return true;
        }
        if(args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GREEN + "List of all banned enchants:");
            for(String ench : bannedEnchants) {
                sender.sendMessage(String.format(ChatColor.GREEN + "- %s", StringUtils.capitalize(ench)));
            }
            return true;
        }
        sender.sendMessage(String.format(ChatColor.RED + "%s is an unrecognised subcommand", args[0]));
        return false;
    }

}
