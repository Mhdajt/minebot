package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.entity.EquipmentSlot; import net.minecraft.item.Items; import net.minecraft.item.ItemStack;
public class TotemSkill implements BotSkill {
 private boolean enabled=true;
 public String getName(){return "Totem";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(bot.getHealth()<=bot.getMaxHealth()*.5f&&!bot.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)){for(int i=0;i<bot.getInventory().size();i++){ItemStack s=bot.getInventory().getStack(i);if(s.isOf(Items.TOTEM_OF_UNDYING)){bot.getInventory().setStack(i,bot.getOffHandStack());bot.equipStack(EquipmentSlot.OFFHAND,s);break;}}}}
}
