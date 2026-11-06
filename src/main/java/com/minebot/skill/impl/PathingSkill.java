package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.entity.ai.pathing.EntityNavigation;
public class PathingSkill implements BotSkill {
 private boolean enabled=true; private int cooldown;
 public String getName(){return "Pathing";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(target==null||!target.isAlive())return;if(cooldown>0)cooldown--;if(cooldown==0&&bot.distanceTo(target)>4){bot.getNavigation().startMovingTo(target,1.15);cooldown=10;}}
}
