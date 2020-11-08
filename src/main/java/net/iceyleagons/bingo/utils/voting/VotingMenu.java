package net.iceyleagons.bingo.utils.voting;

import lombok.Data;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author TOTHTOMI
 */
@Data
public class VotingMenu implements Listener {


    private Inventory inventory;
    private String title;
    private int size;
    private Map<String ,Integer> voteMap;
    private Map<Integer,String > itemMap;
    private Map<String,Integer> itemMapReverse;
    private Map<Player,String> voteMap2;


    private Voting parent;

    public VotingMenu(String title, int size, JavaPlugin javaPlugin) {
        this.title = title;
        this.size = size;
        this.voteMap = new HashMap<>();
        this.itemMap = new HashMap<>();
        this.voteMap2 = new HashMap<>();
        this.itemMapReverse = new HashMap<>();
        inventory = Bukkit.createInventory(null,size,title);
        Bukkit.getServer().getPluginManager().registerEvents(this, javaPlugin);
    }

    public void addVoteOption(Material material, int slot, String id, String name) {
        ItemStack itemStack = ItemFactory.newFactory(material).hideAttributes().setDisplayName("§9§l"+name).addLoreLine("Click to vote!").build();
        getInventory().setItem(slot,itemStack);
        voteMap.put(id,0);
        itemMap.put(slot,id);
        itemMapReverse.put(id,slot);
    }

    public void addVoteOption(ItemStack itemStack, int slot, String id) {
        getInventory().setItem(slot,itemStack);
        voteMap.put(id,0);
        itemMap.put(slot,id);
        itemMapReverse.put(id,slot);
    }

    public int getVotes(String id) {
        return voteMap.get(id);
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null,getSize(),getTitle());
        ItemStack back = ItemFactory.newFactory(Material.PAPER).hideAttributes().setDisplayName("§f§l< Back").build();
        inventory.setContents(getInventory().getContents());
        inventory.setItem(getSize()-1,back);
        if (voteMap2.containsKey(player)) {
            String id = voteMap2.get(player);
            int slot = itemMapReverse.get(id);
            ItemStack itemStack = inventory.getItem(slot);
            assert itemStack != null;
            inventory.setItem(slot,addGlow(itemStack,id));
        }
        player.openInventory(inventory);
    }

    private ItemStack addGlow(ItemStack itemStack, String id) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (itemMeta.getDisplayName().contains("Votes")) {
            String name = itemMeta.getDisplayName();
            String newname = name.replace(String.valueOf(voteMap.get(id)-1),String.valueOf(voteMap.get(id)));
            itemMeta.setDisplayName(newname);
        } else
        itemMeta.setDisplayName(itemMeta.getDisplayName()+String.format(" %s Votes",voteMap.get(id)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack removeGlow(ItemStack itemStack, String id) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (itemMeta.getDisplayName().contains("Votes")) {
            String name = itemMeta.getDisplayName();
            String newname = name.replace(String.valueOf(voteMap.get(id)-1),String.valueOf(voteMap.get(id)));
            itemMeta.setDisplayName(newname);
        } else
            itemMeta.setDisplayName(itemMeta.getDisplayName()+String.format(" %s Votes",voteMap.get(id)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(getTitle())) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        assert itemStack != null;
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
        if (event.getSlot() == getSize()-1) {
            event.setCancelled(true);
            getParent().open(player);
        }
        if (itemMap.containsKey(event.getSlot())) {
            String id = itemMap.get(event.getSlot());

            if (voteMap2.containsKey(player)) {
                //if (!voteMap2.get(player).equals(id)) return;
                String toRemove = voteMap2.get(player);
                voteMap.put(toRemove,voteMap.get(toRemove)-1);
                event.getInventory().setItem(event.getSlot(),removeGlow(itemStack,toRemove));
            }
            voteMap2.put(player,id);
            voteMap.put(id,voteMap.get(id)+1);
            event.getInventory().setItem(event.getSlot(),addGlow(itemStack,id));
        }
        event.setCancelled(true);
    }
}
