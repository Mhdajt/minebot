package com.minebot.bot;

import com.minebot.Minebot;
import com.minebot.skill.SkillController;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

/** Server-side combat entity with a custom Minebot entity type. */
public class MinebotEntity extends PathAwareEntity {

    private final UUID botId;
    private final String botName;
    private final int numericId;
    private UUID ownerUuid;
    private final SkillController skills;
    private LivingEntity currentTarget;
    private int attackCooldown;
    private int ticksAlive;
    private int targetRefreshTicks;
    private int strafeDirection = 1;
    private int hurtReactionTicks;

    public MinebotEntity(World world) {
        this(Minebot.BOT_ENTITY_TYPE, world, UUID.randomUUID(), "Minebot", 0);
    }

    public MinebotEntity(World world, UUID botId, String name, int numericId) {
        this(Minebot.BOT_ENTITY_TYPE, world, botId, name, numericId);
    }

    private MinebotEntity(net.minecraft.entity.EntityType<? extends PathAwareEntity> type, World world,
                          UUID botId, String name, int numericId) {
        super(type, world);
        this.botId = botId;
        this.botName = name;
        this.numericId = numericId;
        this.skills = new SkillController(this);
        this.setCustomName(net.minecraft.text.Text.literal(name));
        this.setCustomNameVisible(true);
        this.setPersistent();
    }

    public static DefaultAttributeContainer.Builder createBotAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0);
    }

    public UUID getBotId() { return botId; }
    public String getBotName() { return botName; }
    public int getNumericId() { return numericId; }
    public void setOwner(UUID owner) { this.ownerUuid = owner; }
    public UUID getOwnerUuid() { return ownerUuid; }
    public SkillController getSkills() { return skills; }
    public LivingEntity getCurrentTarget() { return currentTarget; }
    public int getTicksAlive() { return ticksAlive; }
    public void reverseStrafe() { strafeDirection *= -1; }
    public int getStrafeDirection() { return strafeDirection; }

    public void applyKit(String kitName) {
        this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
        this.equipStack(EquipmentSlot.CHEST, ItemStack.EMPTY);
        this.equipStack(EquipmentSlot.LEGS, ItemStack.EMPTY);
        this.equipStack(EquipmentSlot.FEET, ItemStack.EMPTY);

        switch (kitName.toLowerCase()) {
            case "diamond_pvp", "default" -> {
                equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                setStackInHand(net.minecraft.util.Hand.OFF_HAND, new ItemStack(Items.GOLDEN_APPLE, 4));
            }
            case "netherite" -> {
                equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
                equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
                equipStack(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
                equipStack(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
                equipStack(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
                equipStack(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
            }
            case "crystal" -> {
                equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.END_CRYSTAL));
                equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
                equipStack(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
                equipStack(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
                equipStack(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
                equipStack(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
            }
            default -> equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
    }

    /** Called on a batch schedule by BotManager. Vanilla entity ticks still run normally. */
    public void tickBot(MinecraftServer server) {
        if (getWorld().isClient || !isAlive()) return;
        ticksAlive++;

        if (currentTarget == null || !currentTarget.isAlive() || targetRefreshTicks-- <= 0) {
            currentTarget = findNearestPlayerTarget(Minebot.CONFIG.botSimulationDistance);
            targetRefreshTicks = 10;
        }

        if (currentTarget == null) {
            if (hurtReactionTicks > 0) hurtReactionTicks--;
            return;
        }

        skills.tick(currentTarget);
        if (attackCooldown > 0) attackCooldown--;

        double distance = squaredDistanceTo(currentTarget);
        if (attackCooldown <= 0 && distance <= 3.1 * 3.1 && canSee(currentTarget)) {
            lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES, currentTarget.getEyePos());
            tryAttack(currentTarget);
            attackCooldown = 12;
        }
    }

    private LivingEntity findNearestPlayerTarget(double range) {
        if (!(getWorld() instanceof ServerWorld world)) return null;
        PlayerEntity nearest = null;
        double best = range * range;
        for (PlayerEntity player : world.getPlayers()) {
            if (player.isSpectator() || player.isCreative() || !player.isAlive()) continue;
            if (ownerUuid != null && ownerUuid.equals(player.getUuid())) continue;
            double distance = squaredDistanceTo(player);
            if (distance < best) {
                best = distance;
                nearest = player;
            }
        }
        return nearest;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            currentTarget = attacker;
            hurtReactionTicks = 4;
            reverseStrafe();
        }
        skills.onDamaged(source, amount);
        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean isPersistent() { return true; }

    @Override
    public void checkDespawn() {}
}
