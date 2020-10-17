package net.iceyleagons.bingo.texture;

import lombok.Getter;
import lombok.SneakyThrows;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.freezer.impl.json.JSON;
import org.bukkit.Material;
import org.bukkit.util.FileUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MaterialTexture {

    static File textureFolder = createFolder(new File(Main.main.getDataFolder(), "texture_atlases/"));

    private static File createFolder(File f) {
        f.mkdirs();

        return f;
    }

    public static class Texture {

        @Getter
        File atlasFile;
        @Getter
        String name;
        @Getter
        int startX, startY, endX, endY;
        @Getter
        boolean obtainable = false;
        Difficulty difficulty;

        public Texture(String name, String difficulty, File atlasFile) {
            this.name = name;
            this.atlasFile = atlasFile;
            endX = (startX = MaterialTexture.id % 16 * 16) + 16;
            endY = (startY = (int) Math.floor(id / 16) * 16) + 16;

            try {
                this.difficulty = Difficulty.valueOf(difficulty);
                this.obtainable = true;
            } catch (IllegalArgumentException exception) {
                this.difficulty = null;
            }

            MaterialTexture.id++;
        }
    }

    static int id = 0;
    static final List<Texture> values = new ArrayList<>();

    static Texture[] values() {
        return values.toArray(new Texture[0]);
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked"})
    public MaterialTexture() {
        // Loop thru objects
        File atlasJson = new File(Main.main.getDataFolder(), "atlas.json");

        if (!atlasJson.exists())
            Main.main.saveResource("atlas.json", true);

        try {
            JSONParser jsonParser = new JSONParser();
            Object objj = jsonParser.parse(new FileReader(atlasJson));
            //Object obj = new JSONParser(new FileReader(atlasJson)).parse();
            JSONObject jsonTree = (JSONObject) objj;
            File atlasFile = new File(textureFolder, ((String) jsonTree.get("atlas_file")));

            if (!atlasFile.exists() && ((String)jsonTree.get("atlas_file")).equals("default.png")) {
                Main.main.saveResource("items.png", true);
                FileUtil.copy(new File(Main.main.getDataFolder(), "items.png"), new File(textureFolder, "default.png"));
                new File(Main.main.getDataFolder(), "items.png").delete();
            }

            JSONArray array = (JSONArray) jsonTree.get("blocks");
            array.forEach(obj -> {
                JSONObject object = (JSONObject) obj;
                String set = (String) object.keySet().iterator().next();
                Texture texture = new Texture(set, (String) ((JSONObject) object.get(set)).get("difficulty"), atlasFile);

                if (texture.isObtainable()) {
                    synchronized (values) {
                        values.add(texture);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum Difficulty {
        FREE, EASY, MEDIUM, HARD, EXPERT
    }

    public static Map<Texture, Material> random(int free, int easy, int medium, int hard, int extreme) {
        int currentF = 0, currentEa = 0, currentM = 0, currentH = 0, currentEx = 0;
        HashMap<Texture, Material> objectHashMap = new HashMap<>(free + easy + medium + hard + extreme);

        boolean done = false;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        while (!done) {
            Texture randomTexture = MaterialTexture.values()[random.nextInt(Math.abs(MaterialTexture.values().length))];
            if (randomTexture.isObtainable()) {
                try {
                    Material mat = Material.valueOf(randomTexture.getName().toUpperCase());
                    if (!objectHashMap.containsKey(randomTexture))
                        switch (randomTexture.difficulty) {
                            case FREE:
                                if (!(currentF >= free)) {
                                    currentF++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case EASY:
                                if (!(currentEa >= easy)) {
                                    currentEa++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case MEDIUM:
                                if (!(currentM >= medium)) {
                                    currentM++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case HARD:
                                if (!(currentH >= hard)) {
                                    currentH++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                            case EXPERT:
                                if (!(currentEx >= extreme)) {
                                    currentEx++;
                                    objectHashMap.put(randomTexture, mat);
                                }
                                break;
                        }
                } catch (IllegalArgumentException ignored) {
                    // Ignored.
                }
            }

            if (currentF >= free && currentEa >= easy && currentM >= medium && currentH >= hard && currentEx >= extreme)
                done = true;
        }

        return objectHashMap;
    }

}
