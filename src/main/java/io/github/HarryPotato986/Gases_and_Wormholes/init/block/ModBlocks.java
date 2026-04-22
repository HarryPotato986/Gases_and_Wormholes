package io.github.HarryPotato986.Gases_and_Wormholes.init.block;


import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.infrastructure.config.CStress;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractor;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.*;
import io.github.HarryPotato986.Gases_and_Wormholes.util.GnWSharedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes.REGISTRATE;

public class ModBlocks {

    //static {
    //    REGISTRATE.setCreativeTab(CreativeTabInit.GASES_AND_WORMHOLES_TAB);
    //}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Gases_and_Wormholes.MODID);

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
            .onRegister(b -> BlockStressValues.IMPACTS.register(b, () -> 16))
            //.transform(CStress.setImpact(16.0))
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
            .properties(p -> p.isViewBlocking(ModBlocks::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorFluid> WORMHOLE_GENERATOR_FLUID = REGISTRATE.block("wormhole_generator_fluid", WormholeGeneratorFluid::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(ModBlocks::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorItem> WORMHOLE_GENERATOR_ITEM = REGISTRATE.block("wormhole_generator_item", WormholeGeneratorItem::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(ModBlocks::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorCore> WORMHOLE_GENERATOR_CORE = REGISTRATE.block("wormhole_generator_core", WormholeGeneratorCore::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(ModBlocks::never))
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<WormholeGeneratorHelper> WORMHOLE_GENERATOR_HELPER = REGISTRATE.block("wormhole_generator_helper", WormholeGeneratorHelper::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .properties(p -> p.noOcclusion())
            .properties(p -> p.isViewBlocking(ModBlocks::never))
            .transform(pickaxeOnly())
            .register();





    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
        return (Boolean) false;
    }


}
