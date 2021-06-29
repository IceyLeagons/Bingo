package net.iceyleagons.bingo.items;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.bingo.game.enums.Difficulty;
import net.iceyleagons.bingo.texture.Texture;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class ItemDictionary {

    private static final Random random = new Random(System.currentTimeMillis());

    private final ItemStack[] items;

    public static ItemDictionary generateRandomItems(Difficulty difficulty) {
        int targetWeight = 25;
        Map<Integer, List<ItemStack>> weightMap = new HashMap<>();

        if (Texture.textures.isEmpty())
            return new ItemDictionary(new ItemStack[0]);

        int max = Texture.textures.size();
        // 8 iterations.
        for (int i = 0; i < 8; i++) {
            final List<ItemStack> textures = new ArrayList<>(25);
            int weight = 0;

            while (textures.size() < 25) {
                Texture texture = Texture.textures.get(random.nextInt(max));
                weight += texture.getDifficultyWeight();
                textures.add(new ItemStack(texture.getBaseMaterial()));
            }

            weightMap.put(weight, textures);
        }

        return new ItemDictionary(weightMap.entrySet().stream().min(Comparator.comparingInt(a -> Math.abs(targetWeight - a.getKey()))).get().getValue().toArray(new ItemStack[0]));
    }
}
