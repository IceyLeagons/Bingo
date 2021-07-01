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
import java.awt.image.RasterFormatException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@Service
public class Texture {

    public static final Map<Material, GECISCLASS> textures = new HashMap<>(1100);
    public static boolean init = false; //here because of an Icicle issue, so we do not create it twice unnecessarily
    private static int id = 0;

    //TODO Icicle todo: because of enhancer this class is created twice!
    //Edit: removed issue from icicle temporarily by removing bytecode editing
    @SneakyThrows
    public Texture() {
        if (init) return;

        init = true;
        System.out.println("clearing");
        textures.clear();
        val atlas = Resources.getItemAtlas();
        val atlasJson = Main.instance.getResource("output.json");

        // fuck of intellij
        val atlasReader = new InputStreamReader(atlasJson);
        val json = new JSONObject(IOUtils.toString(atlasReader));

        System.out.println("Atlas W: " + atlas.getWidth() + " H: " + atlas.getHeight());

        for (Object materials : json.getJSONArray("materials")) {
            JSONObject material = (JSONObject) materials;

            //comment to Gábe: megoldom (local), addig azt csinálsz amit akarsz
            //if (id > 5) break; //to test more easily


            id++;
            int x = id % 16 * 16;
            int y = ((int) Math.floor(id / 16f)) * 16;
            try {
                //System.out.println("================");
                Material mat = Material.valueOf(material.getString("name").toUpperCase());

                //System.out.println("found " + material.getString("name").toUpperCase());
               // System.out.println("X: " + x + " Y:" + y);

                int diffWeight = 1;
                switch (material.getString("difficulty").toLowerCase()) {
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

                //System.out.println("putting gecisclass " + material.getString("name").toUpperCase());
                textures.put(mat, new GECISCLASS(atlas.getSubimage(x, y, 16, 16), diffWeight, x, y, mat));
            } catch (IllegalArgumentException | RasterFormatException ignored) {
                System.out.println("not found or raster outside " + material.getString("name").toUpperCase());
                // skip.
            }
            //System.out.println("=======================");
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class GECISCLASS {
        private @NonNull BufferedImage image;
        private @NonNull int difficultyWeight;
        private @NonNull int x, y;
        private @NonNull Material baseMaterial;


    }

}
