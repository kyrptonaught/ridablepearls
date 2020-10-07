package net.kyrptonaught.ridablepearl.entity;

import net.kyrptonaught.ridablepearl.RidablepearlMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class RidablePearlEntity extends ThrownItemEntity {

    public RidablePearlEntity(World world, LivingEntity owner) {
        super(RidablepearlMod.ridablepearl, owner, world);
    }

    public RidablePearlEntity(EntityType<? extends RidablePearlEntity> entityType, World world) {
        super(entityType, world);
    }

    public RidablePearlEntity(World world) {
        this(RidablepearlMod.ridablepearl, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RidablepearlMod.riadablepearlitem;
    }

    @Override
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, RidablepearlMod.packetID);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.0F);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        Entity entity = this.getOwner();

        for(int i = 0; i < 32; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.world.isClient && !this.removed) {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
                if (serverPlayerEntity.networkHandler.getConnection().isOpen() && serverPlayerEntity.world == this.world && !serverPlayerEntity.isSleeping()) {
                    if (this.random.nextFloat() < 0.05F && this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                        EndermiteEntity endermiteEntity = (EndermiteEntity)EntityType.ENDERMITE.create(this.world);
                        endermiteEntity.setPlayerSpawned(true);
                        endermiteEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.yaw, entity.pitch);
                        this.world.spawnEntity(endermiteEntity);
                    }
                    if (this.hasPassengers()) {
                        this.getPassengerList().get(0).stopRiding();
                    }
                    //entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                    entity.fallDistance = 0.0F;
                }
            } else if (entity != null) {
                //entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                entity.fallDistance = 0.0F;
            }

            this.remove();
        }

    }

    public void tick() {
        for (int i = 0; i < 5; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            this.remove();
        } else {
            super.tick();
        }
    }

    public Entity moveToWorld(ServerWorld destination) {
        Entity entity = this.getOwner();
        if (entity != null && entity.world.getRegistryKey() != destination.getRegistryKey()) {
            this.setOwner(null);
        }

        return super.moveToWorld(destination);
    }
}
