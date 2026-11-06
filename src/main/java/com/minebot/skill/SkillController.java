package com.minebot.skill;

import com.minebot.bot.MinebotEntity;
import com.minebot.skill.impl.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds and ticks all active skills / techniques for one bot.
 *
 * To add a new technique:
 * 1. Create a class implementing BotSkill
 * 2. Register it in the constructor below
 * 3. Implement the logic (movement, item use, block placement, etc.)
 */
public class SkillController {

    private final MinebotEntity bot;
    private final List<BotSkill> skills = new ArrayList<>();

    public SkillController(MinebotEntity bot) {
        this.bot = bot;

        // === Core movement & combat skills ===
        // These are stubs / basic implementations. Expand them into full techniques.
        skills.add(new StrafeSkill());
        skills.add(new WTapSkill());
        skills.add(new CriticalHitSkill());
        skills.add(new ShieldSkill());
        skills.add(new GappleSkill());
        skills.add(new TotemSkill());

        // Advanced techniques are registered independently; incomplete modes remain disabled by design.
        skills.add(new CrystalSkill());
        skills.add(new AnchorSkill());
        skills.add(new MaceSkill());
        skills.add(new ElytraSkill());
        skills.add(new PearlSkill());
        skills.add(new BridgingSkill());
        skills.add(new BlockHittingSkill());
        skills.add(new JumpResetSkill());
        skills.add(new STapSkill());
        skills.add(new ComboSkill());

        // Utility
        skills.add(new PathingSkill());
        skills.add(new InventorySkill());
    }

    public void tick(LivingEntity target) {
        for (BotSkill skill : skills) {
            if (skill.isEnabled()) {
                skill.tick(bot, target);
            }
        }
    }

    public void onDamaged(DamageSource source, float amount) {
        for (BotSkill skill : skills) {
            if (skill.isEnabled()) {
                skill.onDamaged(bot, source, amount);
            }
        }
    }

    public <T extends BotSkill> T getSkill(Class<T> clazz) {
        for (BotSkill s : skills) {
            if (clazz.isInstance(s)) {
                return clazz.cast(s);
            }
        }
        return null;
    }

    public List<BotSkill> getAllSkills() {
        return skills;
    }
}