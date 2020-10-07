package net.kyrptonaught.ridablepearl;


import net.kyrptonaught.ridablepearl.entity.RidablePearlEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RidablePearlItem extends EnderPearlItem {
    public RidablePearlItem(Settings settings) {
        super(settings);
        Registry.register(Registry.ITEM, new Identifier(RidablepearlMod.MOD_ID, "ridablepearl"), this);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            RidablePearlEntity ridablePearlEntity = new RidablePearlEntity(world, user);
            ridablePearlEntity.setItem(itemStack);
            ridablePearlEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);
            Entity primary = user.getRootVehicle();
            if (primary instanceof RidablePearlEntity) {
                primary.getPassengerList().get(0).stopRiding();
                primary.remove();
            }
            user.getRootVehicle().startRiding(ridablePearlEntity, true);
            world.spawnEntity(ridablePearlEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.abilities.creativeMode) {
            itemStack.damage(1, user, playerEntity -> playerEntity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.getItem().equals(Items.ENDER_PEARL);
    }
}
