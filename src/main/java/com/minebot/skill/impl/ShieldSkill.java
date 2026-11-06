package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.item.Items; import net.minecraft.util.Hand; import net.minecraft.entity.damage.DamageSource;
public class ShieldSkill implements BotSkill {private boolean enabled=true; private int blockTicks;
 public String getName(){return "Shield";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(blockTicks>0){blockTicks--;if(bot.isUsingItem())return;} if(target!=null&&target.isAlive()&&bot.distanceTo(target)<4&&bot.getOffHandStack().isOf(Items.SHIELD)){bot.setCurrentHand(Hand.OFF_HAND);blockTicks=8;}}
 public void onDamaged(MinebotEntity bot,DamageSource source,float amount){if(source.getAttacker() instanceof LivingEntity)blockTicks=8;}
}
