package io.github.HarryPotato986.Gases_and_Wormholes.init.block;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.CreativeTabInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractor;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.*;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.FluidInit;
import io.github.HarryPotato986.Gases_and_Wormholes.util.GnWSharedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes.REGISTRATE;

public class BlockInit {

    static {
        REGISTRATE.setCreativeTab(CreativeTabInit.GASES_AND_WORMHOLES_TAB);
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Gases_and_Wormholes.MODID);

    /*
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
    */
    public static final RegistryObject<LiquidBlock> LIQUID_NITROGEN_BLOCK = BLOCKS.register("liquid_nitrogen_block",
            () -> new LiquidBlock(FluidInit.SOURCE_LIQUID_NITROGEN, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));



    public static final BlockEntry<Block> TEMP_BLOCK = REGISTRATE.block("temp_block", Block::new)
            .properties(p -> p.mapColor(MapColor.COLOR_PINK))
            .properties(p -> p.strength(5.0f, 17f))
            .properties(p -> p.lightLevel(state -> 15))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(p -> p.pushReaction(PushReaction.IGNORE))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<AtmosphereExtractor> ATMOSPHERE_EXTRACTOR = REGISTRATE.block("atmosphere_extractor", AtmosphereExtractor::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.GOLD))
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setImpact(16.0))
            .register();

    public static final BlockEntry<LinkedBlock> LINKED_BLOCK = REGISTRATE.block("linked_block", LinkedBlock::new)
            .initialProperties(GnWSharedProperties::bedrock)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .register();

    /*
    public static final BlockEntry<WormholeGenerator> WORMHOLE_GENERATOR = REGISTRATE.block("wormhole_generator", WormholeGenerator::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(BlockInit::never))
            .transform(pickaxeOnly())
            .register();
     */

    public static final BlockEntry<WormholeGeneratorKinetic> WORMHOLE_GENERATOR_KINETIC = REGISTRATE.block("wormhole_generator_kinetic", WormholeGeneratorKinetic::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(BlockInit::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorFluid> WORMHOLE_GENERATOR_FLUID = REGISTRATE.block("wormhole_generator_fluid", WormholeGeneratorFluid::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(BlockInit::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorItem> WORMHOLE_GENERATOR_ITEM = REGISTRATE.block("wormhole_generator_item", WormholeGeneratorItem::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(BlockInit::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorCore> WORMHOLE_GENERATOR_CORE = REGISTRATE.block("wormhole_generator_core", WormholeGeneratorCore::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(BlockInit::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorHelper> WORMHOLE_GENERATOR_HELPER = REGISTRATE.block("wormhole_generator_helper", WormholeGeneratorHelper::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(BlockInit::never))
            .transform(pickaxeOnly())
            .register();





    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
        return (Boolean) false;
    }


}
