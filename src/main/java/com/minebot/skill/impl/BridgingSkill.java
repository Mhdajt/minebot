package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity;
public class BridgingSkill implements BotSkill {private boolean enabled=true; public String getName(){return "Bridging";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;} public void tick(MinebotEntity bot,LivingEntity target){} }
