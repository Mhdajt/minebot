package com.minebot.skill;

import com.minebot.bot.MinebotEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

/**
 * Base interface for every bot technique / skill.
 * Keep implementations focused and side-effect free where possible.
 */
public interface BotSkill {

    String getName();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    /**
     * Called every time this bot is selected for a full AI tick.
     * target may be null.
     */
    void tick(MinebotEntity bot, LivingEntity target);

    /**
     * Optional reaction to taking damage.
     */
    default void onDamaged(MinebotEntity bot, DamageSource source, float amount) {}
}