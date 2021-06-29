package net.iceyleagons.bingo.texture;

import lombok.*;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.utils.Resources;
import net.iceyleagons.icicle.annotations.Autowired;
import net.iceyleagons.icicle.annotations.Service;
import org.apache.commons.io.IOUtils;
import org.bukkit.Material;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@Getter
@Service
@RequiredArgsConstructor
public class Texture {

    public static final Map<Material, Texture> textures = new HashMap<>(1100);
    private static int id = 0;

    private @NonNull BufferedImage image;
    private @NonNull int difficultyWeight;
    private @NonNull int x, y;
    private @NonNull Material baseMaterial;

    @Autowired
    @SneakyThrows
    public Texture(Main main) {
        textures.clear();
        val atlas = Resources.getItemAtlas();
        val atlasJson = main.getResource("output.json");

        // fuck of intellij
        val atlasReader = new InputStreamReader(atlasJson);
        val json = new JSONObject(IOUtils.toString(atlasReader));

        json.keys().forEachRemaining(key -> {
            id++;
            int x = id % 16 * 16;
            int y = ((int) (id / 16f)) * 16;
            try {
                Material mat = Material.valueOf(key.toUpperCase());

                int diffWeight = 1;
                switch (json.getJSONObject(key).getString("difficulty").toLowerCase()) {
                    default:
                    case "free":
                        break;
                    case "easy":
                        diffWeight += 2;
                        break;
                    case "normal":
                        diffWeight += 5;
                        break;
                    case "hard":
                        diffWeight += 7;
                        break;
                    case "extreme":
                        diffWeight += 15;
                        break;
                }

                textures.put(mat, new Texture(atlas.getSubimage(x, y, 16, 16), diffWeight, x, y, mat));
            } catch (IllegalArgumentException ignored) {
                // skip.
            }
        });
    }

}
