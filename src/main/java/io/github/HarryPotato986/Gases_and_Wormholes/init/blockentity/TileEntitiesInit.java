package io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractorEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractorInstance;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractorRenderer;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlockInstance;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlockRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes.REGISTRATE;

public class TileEntitiesInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Gases_and_Wormholes.MODID);

    /*
    public static final RegistryObject<BlockEntityType<AtmosphereExtractorEntity>> ATMOSPHERE_EXTRACTOR_ENTITY =
            BLOCK_ENTITIES.register("atmosphere_extractor_entity", () ->
                BlockEntityType.Builder.of(AtmosphereExtractorEntity::new,
                        BlockInit.ATMOSPHERE_EXTRACTOR.get()).build(null));
     */

    public static final BlockEntityEntry<AtmosphereExtractorEntity> ATMOSPHERE_EXTRACTOR_ENTITY = REGISTRATE
            .blockEntity("atmosphere_extractor_entity", AtmosphereExtractorEntity::new)
            .instance(() -> AtmosphereExtractorInstance::new, false)
            .validBlocks(BlockInit.ATMOSPHERE_EXTRACTOR)
            .renderer(() -> AtmosphereExtractorRenderer::new)
            .register();

    public static final BlockEntityEntry<LinkedBlockEntity> LINKED_BLOCK_ENTITY = REGISTRATE
            .blockEntity("linked_block_entity", LinkedBlockEntity::new)
            //.instance(() -> LinkedBlockInstance::new)
            .validBlocks(BlockInit.LINKED_BLOCK)
            .renderer(() -> LinkedBlockRenderer::new)
            .register();
}
