package com.minebot;

import com.minebot.bot.BotManager;
import com.minebot.bot.MinebotEntity;
import com.minebot.command.MinebotCommands;
import com.minebot.config.MinebotConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minebot - High performance PvP bot system for Minecraft 26.2 Fabric.
 *
 * Supports 1000+ bots through batched AI, distance LOD, and modular skills.
 */
public class Minebot implements ModInitializer {

    public static final String MOD_ID = "minebot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static MinebotConfig CONFIG;
    public static BotManager BOT_MANAGER;

    public static final EntityType<MinebotEntity> BOT_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "bot"),
            EntityType.Builder.create(MinebotEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6f, 1.8f)
                    .maxTrackingRange(8)
                    .build()
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Minebot for Minecraft 26.2");

        CONFIG = MinebotConfig.load();
        BOT_MANAGER = new BotManager();

        FabricDefaultAttributeRegistry.register(BOT_ENTITY_TYPE, MinebotEntity.createBotAttributes());

        MinebotCommands.register();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            BOT_MANAGER.onServerStarted(server);
            LOGGER.info("Minebot ready. Max bots: {}, Batch size: {}", CONFIG.maxBots, CONFIG.botsPerTick);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            BOT_MANAGER.despawnAll();
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!server.isStopped()) {
                BOT_MANAGER.tick(server);
            }
        });

        LOGGER.info("Minebot initialized successfully with custom EntityType registered.");
    }
}
