package net.iceyleagons.bingo.utils.voting;

import lombok.Data;
import net.iceyleagons.bingo.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TOTHTOMI
 */
@Data
public class Voting implements Listener {

    private Inventory inventory;
    private Map<Integer, VotingMenu> subMenus;
    private Map<Integer, ClickRunnable> runnableMap;
    private Map<String, VotingMenu> voteIdToMenu;
    private String title;
    private int size;
    private boolean votingAllowed;

    public Voting(String title, int size, JavaPlugin javaPlugin) {
        this.title = title;
        this.size = size;
        this.runnableMap = new HashMap<>();
        this.subMenus = new HashMap<>();
        this.voteIdToMenu = new HashMap<>();

        this.inventory = Bukkit.createInventory(null, size, title);
        Bukkit.getServer().getPluginManager().registerEvents(this,javaPlugin);
    }

    public void addSubMenu(Material material, int slot, VotingMenu menu, String name) {
        ItemStack itemStack = ItemFactory.newFactory(material).hideAttributes().setDisplayName(name).addLoreLine("Click to open submenu!").build();
        addSubMenu(itemStack, slot, menu);
    }

    public void addSubMenu(ItemStack itemStack, int slot, VotingMenu voteMenu) {
        subMenus.put(slot, voteMenu);
        voteMenu.setParent(this);
        getInventory().setItem(slot, itemStack);
        voteMenu.getVoteMap().keySet().forEach(key -> {
            voteIdToMenu.put(key, voteMenu);
        });
    }

    public String getWinningFrom(String... ids) {
        int latestBig = 0;
        String latest = null;
        for (String id : ids) {
            int votes = getVotes(id);
            if (votes > latestBig) {
                latestBig = votes;
                latest = id;
            }
        }
        return latest;
    }

    public int getVotes(String id) {
        return voteIdToMenu.get(id).getVotes(id);
    }

    public VotingMenu getSubmenu(int id) {
        return subMenus.get(id);
    }

    public void open(Player player) {
        if (!isVotingAllowed()) return;
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(getTitle())) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
        if (subMenus.containsKey(event.getSlot())) {
            subMenus.get(event.getSlot()).open(player);
            event.setCancelled(true);
        }
    }

}
