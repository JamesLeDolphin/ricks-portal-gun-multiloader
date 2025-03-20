package com.jdolphin.ricksportalgun;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class PGConstants {

	public static final String MODID = "ricksportalgun";
	public static final Random RANDOM = new Random();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public static final String MOD_NAME = "Rick's Portal Gun";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
}