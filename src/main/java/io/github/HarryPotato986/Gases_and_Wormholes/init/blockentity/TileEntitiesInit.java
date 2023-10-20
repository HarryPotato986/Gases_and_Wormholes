package io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntitiesInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Gases_and_Wormholes.MODID);


    public static final RegistryObject<BlockEntityType<AtmosphereExtractorEntity>> ATMOSPHERE_EXTRACTOR_ENTITY =
            BLOCK_ENTITIES.register("atmosphere_extractor_entity", () ->
                BlockEntityType.Builder.of(AtmosphereExtractorEntity::new,
                        BlockInit.ATMOSPHERE_EXTRACTOR.get()).build(null));
}
