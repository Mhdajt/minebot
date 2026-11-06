package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.entity.damage.DamageSource;
public class ComboSkill implements BotSkill {
 private boolean enabled=true; private int combo; private int timeout;
 public String getName(){return "Combo";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(timeout>0)timeout--;else combo=0; if(target!=null&&target.isAlive()&&bot.distanceTo(target)<3){bot.setSprinting(combo<4);}}
 public void onDamaged(MinebotEntity bot,DamageSource source,float amount){if(source.getAttacker() instanceof LivingEntity){combo++;timeout=30;}}
}
