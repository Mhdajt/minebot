package com.minebot.command;

import com.minebot.Minebot;
import com.minebot.bot.BotManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

/**
 * /minebot commands
 *
 * /minebot spawn [count] [kit]
 * /minebot despawn [count|all]
 * /minebot count
 * /minebot info
 * /minebot reload
 */
public class MinebotCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("minebot")
                    .requires(source -> source.hasPermissionLevel(2));

            // /minebot spawn [count] [kit]
            root.then(CommandManager.literal("spawn")
                    .executes(ctx -> spawn(ctx.getSource(), Minebot.CONFIG.defaultSpawnCount, null))
                    .then(CommandManager.argument("count", IntegerArgumentType.integer(1, 2000))
                            .executes(ctx -> spawn(ctx.getSource(),
                                    IntegerArgumentType.getInteger(ctx, "count"), null))
                            .then(CommandManager.argument("kit", StringArgumentType.word())
                                    .executes(ctx -> spawn(ctx.getSource(),
                                            IntegerArgumentType.getInteger(ctx, "count"),
                                            StringArgumentType.getString(ctx, "kit"))))));

            // /minebot despawn [count|all]
            root.then(CommandManager.literal("despawn")
                    .executes(ctx -> despawn(ctx.getSource(), -1))
                    .then(CommandManager.literal("all")
                            .executes(ctx -> despawn(ctx.getSource(), -1)))
                    .then(CommandManager.argument("count", IntegerArgumentType.integer(1, 5000))
                            .executes(ctx -> despawn(ctx.getSource(),
                                    IntegerArgumentType.getInteger(ctx, "count")))));

            // /minebot count
            root.then(CommandManager.literal("count")
                    .executes(ctx -> {
                        int c = Minebot.BOT_MANAGER.getBotCount();
                        ctx.getSource().sendFeedback(() -> Text.literal("§a[Minebot] Active bots: §f" + c), false);
                        return c;
                    }));

            // /minebot info
            root.then(CommandManager.literal("info")
                    .executes(ctx -> {
                        BotManager bm = Minebot.BOT_MANAGER;
                        ctx.getSource().sendFeedback(() -> Text.literal(
                                "§a[Minebot] §fBots: " + bm.getBotCount() +
                                " / " + Minebot.CONFIG.maxBots +
                                " | Batch size: " + Minebot.CONFIG.botsPerTick), false);
                        return 1;
                    }));

            // /minebot reload
            root.then(CommandManager.literal("reload")
                    .executes(ctx -> {
                        Minebot.CONFIG = com.minebot.config.MinebotConfig.load();
                        ctx.getSource().sendFeedback(() -> Text.literal("§a[Minebot] Config reloaded"), false);
                        return 1;
                    }));

            dispatcher.register(root);
        });
    }

    private static int spawn(ServerCommandSource source, int count, String kit) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.literal("This command must be run by a player"));
            return 0;
        }

        Vec3d pos = player.getPos();
        int safeCount = Math.min(count, Minebot.CONFIG.maxSpawnPerCommand);
        int spawned = Minebot.BOT_MANAGER.spawnBots(
                player.getServerWorld(),
                pos,
                safeCount,
                kit,
                player
        );

        source.sendFeedback(() -> Text.literal(
                "§a[Minebot] Spawned §f" + spawned + " §abots" + (count > safeCount ? " (command cap " + safeCount + ")" : "") +
                (kit != null ? " with kit §f" + kit : "")), true);
        return spawned;
    }

    private static int despawn(ServerCommandSource source, int count) {
        if (count < 0) {
            int before = Minebot.BOT_MANAGER.getBotCount();
            Minebot.BOT_MANAGER.despawnAll();
            source.sendFeedback(() -> Text.literal("§a[Minebot] Despawned all §f" + before + " §abots"), true);
            return before;
        } else {
            int removed = Minebot.BOT_MANAGER.despawn(count);
            source.sendFeedback(() -> Text.literal("§a[Minebot] Despawned §f" + removed + " §abots"), true);
            return removed;
        }
    }
}