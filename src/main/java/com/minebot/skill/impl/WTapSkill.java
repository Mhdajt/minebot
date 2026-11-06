package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.entity.damage.DamageSource;
public class WTapSkill implements BotSkill {
 private boolean enabled=true; private int pulse;
 public String getName(){return "WTap";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(pulse>0){pulse--;bot.setSprinting(false);}else if(target!=null&&target.isAlive()&&bot.distanceTo(target)<=3.2)bot.setSprinting(true);}
 public void onDamaged(MinebotEntity bot,DamageSource source,float amount){if(source.getAttacker() instanceof LivingEntity&&amount>0)pulse=2;}
}
