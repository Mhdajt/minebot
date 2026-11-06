package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.item.Items;
public class CrystalSkill implements BotSkill {private boolean enabled=true; public String getName(){return "Crystal";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(target!=null&&target.isAlive()&&bot.getMainHandStack().isOf(Items.END_CRYSTAL)){bot.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES,target.getEyePos());}}
}
