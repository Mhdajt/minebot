# Minebot

Server-side Minecraft Fabric mod for configurable PvP sparring bots, targeting Minecraft 26.2 and Java 25.

## Build

```bash
./gradlew build
```

The mod jar is written to `build/libs/`.

## Commands

All commands require permission level 2:

- `/minebot spawn [count] [kit]`
- `/minebot despawn [count|all]`
- `/minebot count`
- `/minebot info`
- `/minebot reload`

## Configuration

`config/minebot.json` is generated on first run. Important settings include `maxBots`, `botsPerTick`, `botSimulationDistance`, and `defaultKit`.

Minebot uses custom living entities, scheduled AI updates, and range-based AI sleep. The actual sustainable bot count depends on server CPU, memory, tracking distance, and loaded chunks; 1000+ is a goal, not a performance guarantee.

## Status

This repository is an early playable foundation. Custom entity registration, kits, commands, batching, LOD, basic target selection, pathing, strafing, and defensive/consumable behaviors are being developed. Crystal/anchor, mace, elytra, and bridging are not complete PvP implementations yet.
