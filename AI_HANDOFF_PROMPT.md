# AI Handoff Prompt – Finish Minebot for Minecraft 26.2 Fabric

Copy everything below this line and give it to the AI that will continue the work.

---

You are an expert Minecraft Fabric mod developer. Your task is to take the existing **Minebot** project (a high-performance PvP bot mod skeleton for Minecraft **~26.2**) and turn it into a fully working, high-quality mod.

## Project Goals (in priority order)

1. **Scale to 1000+ bots** with low lag
2. Implement **every major Minecraft PvP skill/technique**
3. Keep the architecture clean and extensible
4. Target Fabric for Minecraft version **~26.2** (Java 25+, fabric-loader ≥ 0.19.3)

## What already exists

- Full Gradle + Loom project structure
- `Minebot` main class + client entrypoint
- `BotManager` with concurrent map, spawn/despawn, **batched ticking** (configurable bots-per-tick)
- `MinebotEntity` (currently extends PathAwareEntity using Zombie type as temporary base)
- Modular `BotSkill` interface + `SkillController`
- Many skill stubs already registered:
  - Strafe, W-Tap, S-Tap, Critical Hit, Jump Reset, Combo
  - Shield, Gapple, Totem
  - Crystal, Anchor, Mace, Elytra, Pearl
  - Bridging, Block Hitting
  - Pathing, Inventory
- Basic kits (diamond_pvp, netherite, crystal)
- Commands: `/minebot spawn [count] [kit]`, `despawn`, `count`, `info`, `reload`
- JSON config (`maxBots`, `botsPerTick`, etc.)

## Critical tasks you must do

### 1. Entity system (highest priority for 1k+ scale)
- Register a proper custom `EntityType` for the bot (do **not** keep using Zombie long-term)
- Decide on architecture:
  - Option A (simpler): Full LivingEntity-based bots (easier combat & equipment)
  - Option B (better for 1000+): Lightweight non-LivingEntity simulation + packet spoofing so clients see them as players (harder, much better performance)
- Implement proper attributes, equipment, health, death, etc.

### 2. Implement real combat skills
Fill in every stub in `com.minebot.skill.impl.*` with working logic. Minimum viable set:

**Movement / Sword**
- Proper W-tapping / S-tapping with sprint reset timing
- Adaptive strafing (circle, figure-8, random, distance control)
- Critical hits (jump timing)
- Jump resets / hit selects
- Block-hitting / shield use
- Combo tracking and reach control

**Consumables / Defense**
- Gapple / golden carrot usage with health thresholds
- Totem of Undying detection + auto-swap
- Shield blocking with timing

**Advanced**
- End Crystal PvP (place, detonate, timing, anti-crystal)
- Respawn Anchor PvP
- Mace smash attacks + wind charge / burst
- Elytra combat (firework boost, dive, etc.)
- Ender Pearl usage (escape, aggressive)
- Bridging / towering / mlg water / scaffold style movement
- Inventory management (hotbar swapping, totem refill, etc.)

Use state machines or behavior trees where complexity is high (especially crystal, mace, elytra).

### 3. Performance hardening
- Keep the existing batching system
- Add distance-based LOD / sleep mode for bots far from any real player
- Avoid heavy pathfinding every tick
- Profile and reduce allocations in the hot path
- Make max bots (default 1500) actually usable

### 4. Polish
- Proper custom names / nametags
- Better kits system (load from JSON or datapack style)
- Config for enabling/disabling individual skills globally
- Optional team / faction support
- Debug commands (`/minebot debug`, force target, etc.)

### 5. Compatibility & correctness for 26.2
- Use correct Yarn mappings / Mojang mappings for 26.2
- Correct Fabric API version
- Register everything properly (entities, commands, events)
- Make sure the mod loads and bots can be spawned without crashing

## Reference material the original author had
The user previously supplied these working mods (compiled):
- VexBot 3.1.0 (mc26.2) – excellent sword mechanics reference
- VexBot Ultra, VexMafia, VexNull, VexPlus
- HeroBot + PVP Bot (1.21.11 line) – advanced combat AI, crystal, anchor, kits, factions

Study patterns from those if you have access to decompiled code, but do **not** copy code directly (licenses vary). Re-implement cleanly.

## Deliverables expected from you
1. Fully compilable and runnable mod for 26.2 Fabric
2. Working `/minebot spawn 100` (and preferably 500–1000) without immediate server death
3. At least the core sword techniques + totem + gapple working well
4. Clear comments or a short ARCHITECTURE.md explaining the final design decisions
5. Updated README with build & usage instructions

Start by fixing the entity registration and making a basic bot that can spawn, move, and hit a player. Then iteratively implement skills, testing performance as bot count increases.

Work step by step. Prefer correct, maintainable code over hacks.

---

End of handoff prompt.