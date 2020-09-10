package net.iceyleagons.bingo.game.voting;

import lombok.Data;
import net.iceyleagons.bingo.utils.InventoryFactory;
import net.iceyleagons.bingo.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TOTHTOMI
 */
@Data
public class Vote {

    private Map<Integer,VoteMenu> menus;
    private InventoryFactory inventoryFactory;
    private boolean closed;

    public Vote() {
        setMenus(new HashMap<>());
        setInventoryFactory(new InventoryFactory("Voting",27,new ItemStack(Material.AIR),true));
    }

    public void open(Player player) {
        if (isClosed()) return;
        inventoryFactory.openInventory(player);
    }

    public void closeAll() {
        inventoryFactory.getOpened().forEach(HumanEntity::closeInventory);
    }

    public void addMenu(VoteMenu voteMenu, int voteMenuId, int slot) {
        menus.put(voteMenuId,voteMenu);
        ItemStack placeholder = ItemFactory.newFactory(voteMenu.getPlaceholder()).setDisplayName(voteMenu.getName()).setLore(voteMenu.getDescription()).hideAttributes().build();
        inventoryFactory.setItem(placeholder, slot, new InventoryFactory.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent e) {
                e.getWhoClicked().closeInventory();
                voteMenu.getInventoryFactory().openInventory((Player) e.getWhoClicked());
            }
        });
    }

    public VoteMenu getVoteMenu(int voteMenuId) {
        return menus.get(voteMenuId);
    }


}
