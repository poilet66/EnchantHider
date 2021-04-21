package me.poilet66.enchanthider.Listener;

import javafx.util.Pair;
import me.poilet66.enchanthider.EnchantHider;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;

public class EnchantListener implements Listener {

    private HashMap<UUID, Enchantment> playerEnchantMap = new HashMap<>();
    private final EnchantHider main;
    public static List<Enchantment> bannedEnchants = new ArrayList<Enchantment>();

    public EnchantListener(EnchantHider main) {
        this.main = main;
        this.bannedEnchants = getBannedEnchantments();
    }

    @EventHandler
    public void enchantEvent(PrepareItemEnchantEvent event) {
        List<Pair<Enchantment, Integer>> replacedEnchants = new ArrayList<Pair<Enchantment, Integer>>();
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

                offer.setEnchantment(newEnchant);



                replacedEnchants.add(i, new Pair(newEnchant, offerLevel));

                //event.getOffers()[i] = new EnchantmentOffer(newEnchant, offerLevel, offerCost);
                event.getEnchanter().updateInventory();
            }
            else {
                replacedEnchants.add(i, null);
            }
        }
        main.getPH().addPlayerEnchant(event.getEnchanter().getUniqueId(), replacedEnchants);
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if(main.getPH().getPlayerReplaceEnchants(event.getEnchanter().getUniqueId()) == null) { //if theres no replacement enchants for them
            for(Enchantment bannedEnch : getBannedEnchantments()) {
                if(event.getEnchantsToAdd().containsKey(bannedEnch)) {
                    event.getEnchantsToAdd().remove(bannedEnch);
                }
            }
            return;
        }
        //if theyre in the player handler
        for(Enchantment bannedEnch : getBannedEnchantments()) {
            if(event.getEnchantsToAdd().containsKey(bannedEnch)) {
                Pair<Enchantment, Integer> newEnchantAndLevel = main.getPH().getPlayerReplaceEnchants(event.getEnchanter().getUniqueId()).get(event.whichButton());
                event.getEnchantsToAdd().remove(bannedEnch);
                if(newEnchantAndLevel != null) {
                    event.getEnchantsToAdd().put(newEnchantAndLevel.getKey(), newEnchantAndLevel.getValue());
                }
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
