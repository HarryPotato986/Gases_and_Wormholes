package io.github.HarryPotato986.Gases_and_Wormholes.init.block;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.FluidInit;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Gases_and_Wormholes.MODID);

    public static final RegistryObject<Block> TEMP_BLOCK = BLOCKS.register("temp_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PINK)
                    .strength(5.0f, 17f)
                    .lightLevel(state -> 15)
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.IGNORE)
            ));
    public static final RegistryObject<Block> ATMOSPHERE_EXTRACTOR = BLOCKS.register("atmosphere_extractor",
            () -> new AtmosphereExtractor(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<LiquidBlock> LIQUID_NITROGEN_BLOCK = BLOCKS.register("liquid_nitrogen_block",
            () -> new LiquidBlock(FluidInit.SOURCE_LIQUID_NITROGEN, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
}
