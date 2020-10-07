package net.kyrptonaught.ridablepearl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.kyrptonaught.ridablepearl.entity.RidablePearlEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RidablepearlMod implements ModInitializer {
    public static final String MOD_ID = "ridablepearl";
    public static Item riadablepearlitem;
    public static Identifier packetID = new Identifier(MOD_ID, "spawnridablepearl");
    public static final EntityType<RidablePearlEntity> ridablepearl = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "ridablepearl"),
            FabricEntityTypeBuilder.<RidablePearlEntity>create(SpawnGroup.MISC, RidablePearlEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackRangeBlocks(4).trackedUpdateRate(10).build()
    );

    @Override
    public void onInitialize() {
        riadablepearlitem = new RidablePearlItem(new Item.Settings().group(ItemGroup.MISC).maxCount(1).maxDamage(24));
    }
}