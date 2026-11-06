package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity;
public class STapSkill implements BotSkill {
 private boolean enabled=true; private int cooldown;
 public String getName(){return "STap";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(cooldown>0)cooldown--; if(target!=null&&target.isAlive()&&cooldown==0&&bot.distanceTo(target)<2.2&&bot.isSprinting()){bot.setSprinting(false);cooldown=6;}}
}
