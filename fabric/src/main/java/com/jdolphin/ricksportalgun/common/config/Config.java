package com.jdolphin.ricksportalgun.common.config;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jdolphin.ricksportalgun.Constants;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

public class Config {

	public boolean ALLOW_PLAYER_LOCATING;
	public int RANDOMIZER_MAX;
	public ArrayList<String> BLACKLISTED_DIMS;

	public Config() {
		this.ALLOW_PLAYER_LOCATING = true;
		this.RANDOMIZER_MAX = 1000;
	}

	private static Config instance;

	public static Config getInstance() {
		if (instance == null) {
			instance = fromDefaults();
		}
		return instance;
	}

	public static void load(File file) {
		instance = fromFile(file);

		if (instance == null) {
			instance = fromDefaults();
		}
	}

	public static void load(String file) {
		load(new File(file));
	}

	private static Config fromDefaults() {
		Config config = new Config();
		config.save();
		return config;
	}

	public ArrayList<ResourceLocation> getBlacklistedDimensions() {
		ArrayList<ResourceLocation> idList = new ArrayList<>();
		for (String s : BLACKLISTED_DIMS) {
			String replace = s.replaceAll("\"", "");
			idList.add(ResourceLocation.parse(replace));
		}
		return idList;
	}

	public void save() {
		File configDir = FabricLoader.getInstance().getConfigDir().resolve("ricksportalgun").toFile();
		if (!configDir.exists()) {
			if (!configDir.mkdir()) Constants.LOGGER.warn("Couldn't create config directory: " + configDir.getAbsolutePath());
		}
		File configFile = new File(configDir, "ricksportalgun-common.properties");
		Properties properties = new Properties();

		if (configFile.exists()) {
			try (FileInputStream stream = new FileInputStream(configFile)) {
				properties.load(stream);
			} catch (IOException e) {
				Constants.LOGGER.warn("Couldn't read config file '" + configFile.getAbsolutePath() + "'", e);
			}
		}
		ALLOW_PLAYER_LOCATING = asBoolean((String) properties.computeIfAbsent("allow_player_locating", (a) -> "true"), true);
		RANDOMIZER_MAX = asInt((String) properties.computeIfAbsent("randomizer_max", (a) -> "1000"), 1000);
		BLACKLISTED_DIMS = asList((String) properties.computeIfAbsent("blacklisted_dims", (a) -> "[]"), Lists.newArrayList());
		try (FileOutputStream stream = new FileOutputStream(configFile)) {
			properties.store(stream, "Rick's Portal Gun configuration file");
		} catch (IOException e) {
			Constants.LOGGER.warn("Couldn't save config file '" + configFile.getAbsolutePath() + "'", e);
		}
	}

	private static Config fromFile(File configFile) {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
			return gson.fromJson(reader, Config.class);
		} catch (Exception e) {
			Constants.LOGGER.warn("Exception loading config file: " + e.getLocalizedMessage());
			return null;
		}
	}

	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	private static TriState asTriState(String property) {
		if (property == null || property.isEmpty()) {
			return TriState.DEFAULT;
		} else {
			switch (property.toLowerCase(Locale.ROOT)) {
				case "true":
					return TriState.TRUE;
				case "false":
					return TriState.FALSE;
				case "auto":
				default:
					return TriState.DEFAULT;
			}
		}
	}

	public static ArrayList asList(String list, ArrayList<String> defValue) {
		if (list == null || list.isEmpty()) return defValue;
		else try {
			String replace = list.replace("[","");
			String replace1 = replace.replace("]","");
            return new ArrayList<>(Arrays.asList(replace1.split(",")));
		} catch (Exception e) {
			Constants.LOGGER.warn("Exception getting blacklisted dimension list: " + e.getLocalizedMessage());
		}
		return defValue;
	}

	private static int asInt(String property, int defValue) {
		if (property == null || property.isEmpty()) return defValue;
		else try {
				return Integer.parseInt(property);
			} catch (NumberFormatException exception) {
			Constants.LOGGER.warn("Error reading config value: " + exception.getLocalizedMessage());
			return defValue;
		}
	}

	private static boolean asBoolean(String property, boolean defValue) {
        return switch (asTriState(property)) {
            case TRUE -> true;
            case FALSE -> false;
            default -> defValue;
        };
	}
}