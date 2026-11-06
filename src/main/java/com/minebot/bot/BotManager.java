package com.minebot.bot;

import com.minebot.Minebot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Manages all Minebot entities and throttles decision-making using round-robin batches. */
public class BotManager {
    private final Map<UUID,MinebotEntity> bots=new ConcurrentHashMap<>();
    private final AtomicInteger nextBotId=new AtomicInteger(1);
    private int tickCounter;
    public void onServerStarted(MinecraftServer server){}
    public int getBotCount(){return bots.size();}
    public Collection<MinebotEntity> getAllBots(){return Collections.unmodifiableCollection(bots.values());}

    public int spawnBots(ServerWorld world,Vec3d center,int count,String kitName,ServerPlayerEntity owner){
        int available=Math.max(0,Minebot.CONFIG.maxBots-bots.size());
        int canSpawn=Math.min(Math.max(0,count),available);
        if(canSpawn==0)return 0;
        int spawned=0;
        for(int i=0;i<canSpawn;i++){
            int id=nextBotId.getAndIncrement();
            double angle=2*Math.PI*i/canSpawn;
            Vec3d pos=center.add(Math.cos(angle)*Minebot.CONFIG.spawnRadius,0,Math.sin(angle)*Minebot.CONFIG.spawnRadius);
            MinebotEntity bot=new MinebotEntity(world,UUID.randomUUID(),"Bot_"+id,id);
            bot.refreshPositionAndAngles(pos.x,pos.y,pos.z,0,0);
            bot.setOwner(owner==null?null:owner.getUuid());
            bot.applyKit(kitName==null?Minebot.CONFIG.defaultKit:kitName);
            if(world.spawnEntity(bot)){bots.put(bot.getBotId(),bot);spawned++;}
        }
        Minebot.LOGGER.info("Spawned {} Minebots (total {})",spawned,bots.size());
        return spawned;
    }
    public void despawnAll(){for(MinebotEntity bot:bots.values())bot.discard();bots.clear();}
    public int despawn(int count){int removed=0;for(MinebotEntity bot:bots.values()){if(removed>=count)break;if(bots.remove(bot.getBotId(),bot)){bot.discard();removed++;}}return removed;}
    public void removeBot(UUID id){MinebotEntity bot=bots.remove(id);if(bot!=null)bot.discard();}
    public MinebotEntity getBot(UUID id){return bots.get(id);}

    public void tick(MinecraftServer server){
        if(bots.isEmpty())return;
        tickCounter++;
        List<MinebotEntity> list=new ArrayList<>(bots.values());
        int n=list.size();
        int batch=Math.min(n,Math.max(1,Minebot.CONFIG.botsPerTick));
        int start=(int)(((long)tickCounter*batch)%n);
        for(int i=0;i<batch;i++){
            MinebotEntity bot=list.get((start+i)%n);
            if(bot.isRemoved()||!bot.isAlive()){bots.remove(bot.getBotId(),bot);continue;}
            if(!nearRealPlayer(bot,Minebot.CONFIG.botSimulationDistance))continue;
            bot.tickBot(server);
        }
    }
    private boolean nearRealPlayer(MinebotEntity bot,double range){
        double max=range*range;
        for(ServerPlayerEntity player:bot.getServer().getPlayerManager().getPlayerList()){
            if(player.getWorld()==bot.getWorld()&&bot.squaredDistanceTo(player)<=max)return true;
        }
        return false;
    }
}
