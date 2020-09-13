package net.iceyleagons.freezer.impl.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.iceyleagons.freezer.Freezer;
import net.iceyleagons.freezer.FreezerType;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author TOTHTOMI
 */
public class JSON implements Freezer {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //In the object array the 1st element has to be an Integer when the update happened
    private HashMap<Integer, Object[]> cache;
    private final Type typeOfHashMap;
    private final Integer version;
    private Long lastUpdate;
    private final String name;
    private final File file;

    public JSON(File subFolder, String name, int version) {
        this.name = name;
        if (subFolder != null)
            this.file = new File(subFolder, name + ".json");
        else this.file = new File(name + ".json");
        this.cache = new HashMap<>();
        typeOfHashMap = new TypeToken<Map<String, String>>() {
        }.getType();
        this.version = version;
        this.lastUpdate = System.currentTimeMillis();
    }

    private HashMap<Integer, Object[]> loadFromFile() {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    return gson.fromJson(bufferedReader, typeOfHashMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean loadToFile() {
        String json = gson.toJson(cache);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json);
            fileWriter.flush();
            return true;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }

    private void updateLastUpdate() {
        this.lastUpdate = System.currentTimeMillis();
    }

    @Override
    public FreezerType getType() {
        return FreezerType.JSON;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getVersion() {
        return this.version;
    }

    @Override
    public Long getLastUpdate() {
        return this.lastUpdate;
    }

    @Override
    public CompletableFuture<Boolean> push() {
        updateLastUpdate();
        return CompletableFuture.supplyAsync(this::loadToFile);
    }

    @Override
    public CompletableFuture<Boolean> pull() {
        updateLastUpdate();
        return CompletableFuture.supplyAsync(() -> {
            HashMap<Integer, Object[]> loaded = loadFromFile();
            if (loaded != null) {
                this.cache = loaded;
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean update() {
        updateLastUpdate();
        try {
            if (push().get())
                return pull().get();
            else return false;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void add(int id, Object o) {
        cache.put(id, new Object[]{System.currentTimeMillis(), o});
    }

    @Override
    public void remove(int id) {
        cache.remove(id);
    }

    @Override
    public Object get(int id) {
        return cache.get(id)[1];
    }
}
