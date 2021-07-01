package net.iceyleagons.bingo.items;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
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

        System.out.println("Is empty?");
        if (Texture.textures.isEmpty())
            return new ItemDictionary(new ItemStack[0]);

        System.out.println("empty");

        int max = Texture.textures.size();
        // 8 iterations.
        for (int i = 0; i < 8; i++) {
            final List<ItemStack> textures = new ArrayList<>(25);
            int weight = 0;

            while (textures.size() < 26) {
                Texture.GECISCLASS texture = (Texture.GECISCLASS) Texture.textures.entrySet().toArray(Map.Entry[]::new)[random.nextInt(max)].getValue();

                weight += texture.getDifficultyWeight();
                textures.add(new ItemStack(texture.getBaseMaterial()));

                //System.out.println(texture.getBaseMaterial().name());
                //System.out.println(weight);
            }

            System.out.println("new entry weight: " + weight + "; mat size: " + textures.size());
            weightMap.put(weight, textures);
        }

        val arr = weightMap.entrySet().stream().min(Comparator.comparingInt(a -> Math.abs(targetWeight - a.getKey()))).get().getValue().toArray(new ItemStack[0]);

        System.out.println("Winning items:");
        for (ItemStack itemStack : arr) {
            System.out.println(itemStack.getType().name());
        }

        return new ItemDictionary(arr);
    }
}
