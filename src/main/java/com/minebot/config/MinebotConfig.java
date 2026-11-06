package com.minebot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minebot.Minebot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple JSON config for Minebot.
 * Located at config/minebot.json
 */
public class MinebotConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "minebot.json");

    // === Tunable settings for high bot counts ===
    public int maxBots = 1500;                  // Hard cap
    public int botsPerTick = 64;
    public int maxSpawnPerCommand = 256;                // How many bots get full AI update per server tick (batching)
    public double botSimulationDistance = 128.0; // Beyond this, bots go into very cheap "sleep" mode
    public boolean enableAdvancedCombat = true;
    public boolean enablePathfinding = true;
    public boolean debugLogging = false;

    // Default kit / equipment
    public String defaultKit = "diamond_pvp";

    // Spawn defaults
    public int defaultSpawnCount = 10;
    public double spawnRadius = 8.0;

    public static MinebotConfig load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                MinebotConfig cfg = GSON.fromJson(json, MinebotConfig.class);
                if (cfg != null) {
                    Minebot.LOGGER.info("Loaded config from {}", CONFIG_PATH);
                    return cfg;
                }
            }
        } catch (Exception e) {
            Minebot.LOGGER.warn("Failed to load config, using defaults", e);
        }

        MinebotConfig defaults = new MinebotConfig();
        defaults.save();
        return defaults;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            Minebot.LOGGER.error("Failed to save config", e);
        }
    }
}