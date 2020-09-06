package net.iceyleagons.bingo.game.voting;

import lombok.Data;
import net.iceyleagons.bingo.InventoryFactory;
import net.iceyleagons.bingo.ItemFactory;
import net.iceyleagons.bingo.game.BingoPlayer;
import net.iceyleagons.bingo.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 */
@Data
public class VoteMenu {

    private Material placeholder;
    private String name;
    private List<String> description;
    private InventoryFactory inventoryFactory;
    private Map<BingoPlayer,Integer> votes;
    private Map<Integer,Integer> voteOptions;

    public VoteMenu(Material placeholder, String name, List<String> description) {
        setPlaceholder(placeholder);
        setName(name);
        setDescription(description);
        setInventoryFactory(new InventoryFactory(name,27,new ItemStack(Material.AIR),true));
        setVotes(new HashMap<>());
        setVoteOptions(new HashMap<>());
        //setVotesForVotes(new HashMap<>());
    }

    public int getIdOfWinningOption() {
        int latestHigh = 0;
        int latestId = -1;

        for(Map.Entry<Integer, Integer> entry : voteOptions.entrySet())
            if (entry.getValue() > latestHigh) {
                latestHigh = entry.getValue();
                latestId = entry.getKey();
            }

        return latestId;
    }

    public void addVoteOption(Material placeholder, String name, List<String> description, int slot, int id) {
        ItemStack is = ItemFactory.newFactory(placeholder).setDisplayName(name).setLore(description).build();
        voteOptions.put(id,0);
        inventoryFactory.setItem(is, slot, new InventoryFactory.ClickRunnable() {
            @Override
            public void run(InventoryClickEvent e) {
                Bukkit.broadcastMessage("RUUUUUUUUUUUUUUUN");
                System.out.println(e.toString());
                //if (GameManager.isInGame((Player)e.getWhoClicked())) {
                    BingoPlayer bingoPlayer = GameManager.getBingoPlayer((Player)e.getWhoClicked());
                    if (votes.containsKey(bingoPlayer)) {
                        int optionId = votes.get(bingoPlayer);
                        if ((voteOptions.get(optionId) != 0) &&(voteOptions.get(optionId)-1) >= 0)
                          voteOptions.put(optionId,voteOptions.get(optionId)-1);
                    }
                    votes.put(bingoPlayer,id);
                    voteOptions.put(id,voteOptions.get(id)+1);
                    Bukkit.broadcastMessage(String.valueOf(voteOptions.get(id)));
                }
            //}
        });
    }
    
}
