package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.item.Items; import net.minecraft.item.ItemStack; import net.minecraft.entity.EquipmentSlot;
public class InventorySkill implements BotSkill {
 private boolean enabled=true;
 public String getName(){return "Inventory";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(bot.getOffHandStack().isEmpty()){for(int i=0;i<bot.getInventory().size();i++){ItemStack s=bot.getInventory().getStack(i);if(s.isOf(Items.TOTEM_OF_UNDYING)){bot.getInventory().setStack(i,ItemStack.EMPTY);bot.equipStack(EquipmentSlot.OFFHAND,s);break;}}}}
}
