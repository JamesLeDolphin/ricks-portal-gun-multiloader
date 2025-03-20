package com.jdolphin.ricksportalgun.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.util.platform.Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public abstract class ConfigBase {
    public static ConfigBase INSTANCE = null;
    private final Map<String, String> VALUE_MAP = new TreeMap<>();
    private final Map<String, String> DEFAULT_MAP = new TreeMap<>();
    public static File CONFIG_FILE_PATH;

    protected ConfigBase(String name) {
        INSTANCE = this;
        CONFIG_FILE_PATH = new File(Services.PLATFORM.getConfigPath(), name + ".json");
    }

    protected void addConfig(String name, String defaultValue) {
        DEFAULT_MAP.put(name, defaultValue);
        VALUE_MAP.put(name, defaultValue);
    }

    protected String get(String str) {
        JsonObject object = getJson();
        String defValue = DEFAULT_MAP.get(str);
        if (object != null) {
            if (!object.has(str)) {
                try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {

                    object.addProperty(str, defValue);
                    writer.write(PGConstants.GSON.toJson(object));
                    return defValue;
                } catch (IOException e) {
                    PGConstants.LOGGER.error("Couldn't update config file: ", e);
                }
            }
            return object.get(str).getAsString();
        }
        return defValue;
    }

    protected void getOrCreateConfig() {
        if (!CONFIG_FILE_PATH.exists()) {
            try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
                JsonObject json = new JsonObject();
                for (Map.Entry<String, String> entry : DEFAULT_MAP.entrySet()) {
                    json.addProperty(entry.getKey(), entry.getValue());
                }
                writer.write(PGConstants.GSON.toJson(json));
            } catch (IOException e) {
                PGConstants.LOGGER.error("Couldn't create config file: ", e);
            }
        } else loadConfig();
    }

    public static void save() {
        INSTANCE.saveInternal();
    }

    protected void saveInternal() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
            JsonObject json = new JsonObject();
            for (Map.Entry<String, String> entry : VALUE_MAP.entrySet()) {
                json.addProperty(entry.getKey(), entry.getValue());
            }
            writer.write(PGConstants.GSON.toJson(json));
        } catch (IOException e) {
            PGConstants.LOGGER.error("Couldn't save config file: ", e);
        }
    }

    protected static JsonObject getJson() {
        try (Reader reader = Files.newBufferedReader(Paths.get(CONFIG_FILE_PATH.getAbsolutePath()))) {
            return PGConstants.GSON.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            PGConstants.LOGGER.error("Couldn't get config file: ", e);
        }
        return JsonNull.INSTANCE.getAsJsonObject();
    }

    public static void loadConfig() {
        INSTANCE.loadConfigInternal();
    }

    protected void loadConfigInternal() {
        if (CONFIG_FILE_PATH.exists()) {
            JsonObject json = getJson();
            if (json != null) {
                for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                    VALUE_MAP.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        } else getOrCreateConfig();
    }
}
