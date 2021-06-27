package net.iceyleagons.bingo.items;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.bingo.game.enums.Difficulty;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;

@RequiredArgsConstructor
public class ItemDictionary {

    private final ItemStack[] items;

    public static ItemDictionary generateRandomItems(Difficulty difficulty) {
        //TODO
        return new ItemDictionary(new ItemStack[0]);
    }
}
