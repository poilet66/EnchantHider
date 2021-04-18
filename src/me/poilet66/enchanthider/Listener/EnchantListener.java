package me.poilet66.enchanthider.Listener;

import me.poilet66.enchanthider.EnchantHider;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantListener implements Listener {

    private final EnchantHider main;
    public static List<Enchantment> bannedEnchants = new ArrayList<Enchantment>();

    public EnchantListener(EnchantHider main) {
        this.main = main;
        this.bannedEnchants = getBannedEnchantments();
    }

    @EventHandler
    public void enchantEvent(PrepareItemEnchantEvent event) {
        for(int i=0; i < event.getOffers().length; i++) {
            EnchantmentOffer offer = event.getOffers()[i];
            int offerLevel = offer.getEnchantmentLevel();
            int offerCost = offer.getCost();
            if(offer == null) {
                continue;
            }

            if(bannedEnchants.contains(offer.getEnchantment())) { //if offer is banned

                Enchantment newEnchant;
                do { //keep looping until new valid enchant found (this could be optimised) TODO: make this better
                    newEnchant = randomEnchantment();
                } while (!newEnchant.canEnchantItem(event.getItem()) || bannedEnchants.contains(newEnchant));

                offerLevel = (offerLevel > newEnchant.getMaxLevel()) ? newEnchant.getMaxLevel() : offerLevel; //make sure we dont get unb 4 or some weird shit

                event.getEnchanter().sendMessage(String.format("Knockback detected, new offer: %s level %d", newEnchant.getKey(), offerLevel)); //debug

                event.getOffers()[i] = new EnchantmentOffer(newEnchant, offerLevel, offerCost);
                event.getEnchanter().updateInventory();
            }
        }
    }

    private Enchantment randomEnchantment() {
        int pick = new Random().nextInt(Enchantment.values().length);
        return Enchantment.values()[pick];
    }

    private List<Enchantment> getBannedEnchantments() {
        List<Enchantment> ret = new ArrayList<Enchantment>();
        for(String ench : main.getConfig().getStringList("bannedEnchants")) {
            if(main.isNameValid(ench)) {
                ret.add(Enchantment.getByKey(NamespacedKey.minecraft(ench)));
            }
        }
        return ret;
    }
}
