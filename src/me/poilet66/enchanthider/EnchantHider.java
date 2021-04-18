package me.poilet66.enchanthider;

import me.poilet66.enchanthider.Command.CommandTabCompleter;
import me.poilet66.enchanthider.Command.EnchantCommand;
import me.poilet66.enchanthider.Listener.EnchantListener;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantHider extends JavaPlugin {

    public static final String ENCHANT_KEY_LIST_URL = "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new EnchantListener(this), this);
        getCommand("enchanthider").setExecutor(new EnchantCommand(this));
        getCommand("enchanthider").setTabCompleter(new CommandTabCompleter());
        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {

    }

    public boolean isNameValid(String name) {
        if(Enchantment.getByKey(NamespacedKey.minecraft(name)) == null) {
            getLogger().info(String.format("%s is an invalid enchantment name, find a full list here: %s", name, ENCHANT_KEY_LIST_URL));
            return false;
        }
        return true;
    }
}
