/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.iceyleagons.bingo.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.iceyleagons.bingo.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryFactory {

    private Inventory inv;
    private HashMap<Inventory, InventoryFactory> inventories = new HashMap<>();
    private HashMap<Integer, ClickRunnable> runs = new HashMap<>();
    private int currentOpen = 0;
    private boolean registered = false;
    private Listener listener;
    private List<Player> opened = new ArrayList<Player>();
    @Getter
    @Setter
    private List<Integer> denyList = new ArrayList<>();
    @Getter
    private Map<Player,InventoryFactory> playerInventories = new HashMap<>();

    private int size;
    @Getter
    private String name;
    @Getter
    private ItemStack placeholder;
    @Getter
    private boolean deny;

    public InventoryFactory(String name, int size, boolean deny) {
        this(name, size, null, true);
    }


    public InventoryFactory(String name, int size, ItemStack placeholder, boolean deny) {
        if (size == 0) {
            return;
        }

        this.name = name;
        this.size = size;
        this.placeholder = placeholder;
        this.deny = deny;

        debounce = false;
        listener = new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if (e.getWhoClicked() instanceof Player) {
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    if (inventories.containsKey(e.getInventory())) {
                        InventoryFactory current = inventories.get(e.getInventory());
                        if (denyList.contains(e.getSlot())) e.setCancelled(true);
                        if (deny)
                            e.setCancelled(true);
                        if (e.getCurrentItem().equals(placeholder)) {
                            e.setCancelled(true);
                        }
                        Player p = (Player) e.getWhoClicked();
                        if (current.runs.get(e.getSlot()) == null) {
                            if (deny)
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                            if (e.getCurrentItem().equals(placeholder)) {
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                            }
                        } else {
                            p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
                            if (current.runs.get(e.getSlot()) != null) {
                                current.runs.get(e.getSlot()).run(e);
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {
                if (e.getPlayer() instanceof Player) {
                    opened.remove(((Player) e.getPlayer()));
                    if (inventories.containsKey(e.getInventory())) {
                        InventoryFactory current = inventories.get(e.getInventory());

                        current.currentOpen--;
                        if (current.currentOpen == 0) {
                            current.unRegister();
                        }
                    }
                }
            }
        };
        Bukkit.getServer().getPluginManager().registerEvents(getListener(), Main.main);
        if (size == 5) {
            inv = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.BLUE + name);
        } else if (size == 9) {
            inv = Bukkit.createInventory(null, InventoryType.DROPPER, ChatColor.BLUE + name);
        } else {
            inv = Bukkit.createInventory(null, size, ChatColor.BLUE + name);
        }
        if (placeholder != null) {
            for (int i = 0; i < size; i++) {
                inv.setItem(i, placeholder);
            }
        }
        register();
    }

    public boolean isOpen() {
        return currentOpen > 0;
    }

    public Inventory getSourceInventory() {
        return inv;
    }

    public List<Player> getOpened() {
        return opened;
    }

    public void updateInventory() {
    }

    public int getSize() {
        return inv.getSize();
    }

    public void setItem(ItemStack itemstack, Integer slot, ClickRunnable executeOnClick) {
        setItem(itemstack, null, slot, executeOnClick);
    }

    public void setItem(ItemStack itemstack, String displayname, Integer slot, ClickRunnable executeOnClick, String... description) {
        ItemMeta im = itemstack.getItemMeta();
        assert im != null;
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);
        if (displayname != null) {
            im.setDisplayName(ChatColor.BLUE + displayname);
        }
        if (description != null) {
            List<String> lore = new ArrayList<String>();
            for (String s : description) {
                lore.add(ChatColor.GRAY + s);
            }
            im.setLore(lore);
        }
        itemstack.setItemMeta(im);
        System.out.println(inv);
        System.out.println(slot);
        System.out.println(itemstack);
        inv.setItem(slot, itemstack);
        runs.put(slot, executeOnClick);
    }

    public void removeItem(int slot) {
        inv.setItem(slot, new ItemStack(Material.AIR));
    }

    public void setItem(ItemStack itemstack, Integer slot) {
        inv.setItem(slot, itemstack);
    }

    public static boolean debounce = false;

    public Listener getListener() {
        return listener;
    }

    @SneakyThrows
    public void openInventory(Player player) {
        currentOpen++;
        register();
        opened.add(player);
        player.openInventory(getSourceInventory());
    }

    private void register() {
        if (!registered) {
            inventories.put(inv, this);
            registered = true;
        }
    }

    private void unRegister() {
        if (registered) {
            inventories.remove(inv);
            registered = false;
        }
    }

    @SneakyThrows
    public InventoryFactory makeCopy() {
        return (InventoryFactory) clone();
    }

    public static abstract class ClickRunnable {
        public abstract void run(InventoryClickEvent e);
    }

    public static abstract class CloseRunnable {
        public abstract void run(InventoryCloseEvent e);
    }
}