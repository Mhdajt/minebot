package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity; import com.minebot.skill.BotSkill; import net.minecraft.entity.LivingEntity; import net.minecraft.entity.damage.DamageSource;
public class JumpResetSkill implements BotSkill {
 private boolean enabled=true,shouldJump; private int cooldown;
 public String getName(){return "JumpReset";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot,LivingEntity target){if(cooldown>0)cooldown--;if(shouldJump&&cooldown==0&&bot.isOnGround()){bot.jump();shouldJump=false;cooldown=12;}}
 public void onDamaged(MinebotEntity bot,DamageSource source,float amount){if(source.getAttacker() instanceof LivingEntity&&amount>0)shouldJump=true;}
}
