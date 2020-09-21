package net.iceyleagons.bingo.game.voting;

import lombok.Data;
import lombok.SneakyThrows;
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
    private Map<Player,InventoryFactory> inventories;
    private boolean closed;

    public Vote() {
        setMenus(new HashMap<>());
        setInventoryFactory(new InventoryFactory("Voting",27,new ItemStack(Material.AIR),true));
        setInventories(new HashMap<>());;
    }


    public void open(Player player) {
        if (isClosed()) return;
        if (inventories.containsKey(player)) inventories.get(player).openInventory(player);
        else {
            InventoryFactory playerFactory =  inventoryFactory.makeCopy();
            inventories.put(player,playerFactory);
            playerFactory.openInventory(player);
        }
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
                Player player = (Player) e.getWhoClicked();
                if (voteMenu.getInventories().containsKey(player)) voteMenu.getInventories().get(player).openInventory(player);
                else {
                    InventoryFactory playerFactory = voteMenu.getInventoryFactory().makeCopy();
                    voteMenu.getInventories().put(player,playerFactory);
                    playerFactory.openInventory(player);
                }
            }
        });
    }

    public VoteMenu getVoteMenu(int voteMenuId) {
        return menus.get(voteMenuId);
    }


}
