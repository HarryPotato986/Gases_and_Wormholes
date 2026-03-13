package io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractorEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractorVisual;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractorRenderer;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;


import static io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes.REGISTRATE;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Gases_and_Wormholes.MODID);

    /*
    public static final RegistryObject<BlockEntityType<AtmosphereExtractorEntity>> ATMOSPHERE_EXTRACTOR_ENTITY =
            BLOCK_ENTITIES.register("atmosphere_extractor_entity", () ->
                BlockEntityType.GnWBuilder.of(AtmosphereExtractorEntity::new,
                        BlockInit.ATMOSPHERE_EXTRACTOR.get()).build(null));
     */

    public static final BlockEntityEntry<AtmosphereExtractorEntity> ATMOSPHERE_EXTRACTOR_ENTITY = REGISTRATE
            .blockEntity("atmosphere_extractor_entity", AtmosphereExtractorEntity::new)
            .visual(() -> AtmosphereExtractorVisual::new, false)
            .validBlocks(ModBlocks.ATMOSPHERE_EXTRACTOR)
            .renderer(() -> AtmosphereExtractorRenderer::new)
            .register();

    public static final BlockEntityEntry<LinkedBlockEntity> LINKED_BLOCK_ENTITY = REGISTRATE
            .blockEntity("linked_block_entity", LinkedBlockEntity::new)
            //.instance(() -> LinkedBlockInstance::new)
            .validBlocks(ModBlocks.LINKED_BLOCK)
            .renderer(() -> LinkedBlockRenderer::new)
            .register();

    public static final BlockEntityEntry<WormholeGeneratorCoreEntity> WORMHOLE_GENERATOR_CORE_ENTITY = REGISTRATE
            .blockEntity("wormhole_generator_core_entity", WormholeGeneratorCoreEntity::new)
            .validBlocks(ModBlocks.WORMHOLE_GENERATOR_CORE)
            .renderer(() -> WormholeGeneratorRenderer::new)
            .register();

    public static final BlockEntityEntry<WormholeGeneratorFluidEntity> WORMHOLE_GENERATOR_FLUID_ENTITY = REGISTRATE
            .blockEntity("wormhole_generator_fluid_entity", WormholeGeneratorFluidEntity::new)
            .validBlocks(ModBlocks.WORMHOLE_GENERATOR_FLUID)
            .register();

    public static final BlockEntityEntry<WormholeGeneratorItemEntity> WORMHOLE_GENERATOR_ITEM_ENTITY = REGISTRATE
            .blockEntity("wormhole_generator_item_entity", WormholeGeneratorItemEntity::new)
            .validBlocks(ModBlocks.WORMHOLE_GENERATOR_ITEM)
            .register();

    public static final BlockEntityEntry<WormholeGeneratorKineticEntity> WORMHOLE_GENERATOR_KINETIC_ENTITY = REGISTRATE
            .blockEntity("wormhole_generator_kinetic_entity", WormholeGeneratorKineticEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF), false)
            .validBlocks(ModBlocks.WORMHOLE_GENERATOR_KINETIC)
            .renderer(() -> WormholeGeneratorShaftRenderer::new)
            .register();
}
