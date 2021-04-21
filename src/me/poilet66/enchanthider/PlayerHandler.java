package me.poilet66.enchanthider;

import javafx.util.Pair;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerHandler {

    private final EnchantHider main;
    private HashMap<UUID, List<Pair<Enchantment, Integer>>> playerEnchantReplaceMap = new HashMap<>();

    public PlayerHandler(EnchantHider main) {
        this.main = main;
    }

    public void addPlayerEnchant(UUID player, List<Pair<Enchantment, Integer>> enchants) {
        this.playerEnchantReplaceMap.put(player, enchants);
    }

    public List<Pair<Enchantment, Integer>> getPlayerReplaceEnchants(UUID player) {
        if(!playerEnchantReplaceMap.containsKey(player)) {
            return null;
        }
        return playerEnchantReplaceMap.get(player);
    }
}
