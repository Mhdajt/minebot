package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.item.Items; import net.minecraft.util.Hand;
public class PearlSkill implements BotSkill {
 private boolean enabled=true; private int cooldown;
 public String getName(){return "Pearl";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(cooldown>0)cooldown--;if(cooldown>0||target==null||bot.getHealth()>bot.getMaxHealth()*.25f||bot.distanceTo(target)<6)return;
  Hand hand=bot.getMainHandStack().isOf(Items.ENDER_PEARL)?Hand.MAIN_HAND:(bot.getOffHandStack().isOf(Items.ENDER_PEARL)?Hand.OFF_HAND:null);
  if(hand!=null){bot.setCurrentHand(hand);cooldown=60;}}
}
