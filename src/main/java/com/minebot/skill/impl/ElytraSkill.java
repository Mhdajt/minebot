package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.item.Items; import net.minecraft.entity.EquipmentSlot;
public class ElytraSkill implements BotSkill {private boolean enabled=true; public String getName(){return "Elytra";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(target!=null&&bot.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)&&bot.isOnGround()&&bot.distanceTo(target)>5)bot.jump();}
}
