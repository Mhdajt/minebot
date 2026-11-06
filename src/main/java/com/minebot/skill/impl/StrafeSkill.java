package com.minebot.skill.impl;
import com.minebot.bot.MinebotEntity;
import com.minebot.skill.BotSkill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
public class StrafeSkill implements BotSkill {
 private boolean enabled=true; private int ticks; private int direction=1;
 public String getName(){return "Strafe";} public boolean isEnabled(){return enabled;} public void setEnabled(boolean e){enabled=e;}
 public void tick(MinebotEntity bot, LivingEntity target){
  if(target==null||!target.isAlive())return; double dist=bot.distanceTo(target); if(dist>8)return;
  ticks++; if(ticks%50==0)direction=-direction;
  double dx=bot.getX()-target.getX(), dz=bot.getZ()-target.getZ(); double a=Math.atan2(dz,dx);
  double radius=dist<1.8?2.8:(dist>3.5?2.0:2.5); double radial=Math.max(-.45,Math.min(.45,dist-radius)); double tangent=a+Math.PI/2*direction;
  Vec3d v=new Vec3d(Math.cos(tangent)-radial*Math.cos(a),0,Math.sin(tangent)-radial*Math.sin(a)).normalize().multiply(.20);
  bot.setVelocity(v.x,bot.getVelocity().y,v.z); bot.velocityModified=true;
  bot.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES,target.getEyePos());
 }
}
