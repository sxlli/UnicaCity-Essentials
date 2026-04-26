package de.asxka.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.labymod.api.Laby;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class AllianceCache {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static File getCacheFile() {
        return new File(Laby.labyAPI().labyModLoader().getGameDirectory().toFile(), "uce_buendnis.json");
    }

    private static List<String> memoryCache = null;

    public static List<String> getMembers() {
        if (memoryCache == null) {
            load();
        }
        return memoryCache;
    }

    public static boolean hasSyncedOnce() {
        return getCacheFile().exists();
    }

    public static void save(List<String> members) {
        memoryCache = new ArrayList<>(members);
        File file = getCacheFile();
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            JsonObject obj = new JsonObject();
            JsonArray arr = new JsonArray();
            for (String m : members) {
                arr.add(m);
            }
            obj.add("members", arr);

            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(obj, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void load() {
        memoryCache = new ArrayList<>();
        File file = getCacheFile();
        if (!file.exists()) {
            return;
        }
        try (FileReader reader = new FileReader(file)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            if (obj.has("members")) {
                JsonArray arr = obj.getAsJsonArray("members");
                for (int i = 0; i < arr.size(); i++) {
                    memoryCache.add(arr.get(i).getAsString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

