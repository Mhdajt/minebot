package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.item.ItemStack; import net.minecraft.item.Items; import net.minecraft.util.Hand;
public class GappleSkill implements BotSkill {
 private boolean enabled=true; private int cooldown;
 public String getName(){return "Gapple";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(cooldown>0)cooldown--;if(cooldown>0||bot.getHealth()>bot.getMaxHealth()*.55f||bot.isUsingItem())return;
  Hand hand=Hand.MAIN_HAND; ItemStack stack=bot.getStackInHand(hand); if(!stack.isOf(Items.GOLDEN_APPLE)&&!stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)){hand=Hand.OFF_HAND;stack=bot.getStackInHand(hand);}
  if(stack.isOf(Items.GOLDEN_APPLE)||stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)){bot.setCurrentHand(hand);cooldown=40;}}
}
