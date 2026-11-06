package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity;
public class CriticalHitSkill implements BotSkill {
 private boolean enabled=true; private int cooldown;
 public String getName(){return "CriticalHit";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(cooldown>0)cooldown--; if(target==null||!target.isAlive()||cooldown>0||bot.distanceTo(target)>3.2)return;
  if(bot.isOnGround()&&!bot.isTouchingWater()&&!bot.isInLava()&&!bot.hasVehicle()){bot.setVelocity(bot.getVelocity().x,.42,bot.getVelocity().z);bot.velocityModified=true;cooldown=10;}}
}
