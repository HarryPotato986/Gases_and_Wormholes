package io.github.HarryPotato986.Gases_and_Wormholes.init.fluid;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, Gases_and_Wormholes.MODID);

    public static final Supplier<FlowingFluid> SOURCE_LIQUID_NITROGEN = FLUIDS.register("liquid_nitrogen_fluid",
            () -> new BaseFlowingFluid.Source(ModFluids.LIQUID_NITROGEN_FLUID_PROPERTIES));
    public static final Supplier<FlowingFluid> FLOWING_LIQUID_NITROGEN = FLUIDS.register("flowing_liquid_nitrogen_fluid",
            () -> new BaseFlowingFluid.Flowing(ModFluids.LIQUID_NITROGEN_FLUID_PROPERTIES));

    public static final Supplier<LiquidBlock> LIQUID_NITROGEN_BLOCK = ModBlocks.BLOCKS.register("liquid_nitrogen_block",
            () -> new LiquidBlock(ModFluids.SOURCE_LIQUID_NITROGEN.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).noLootTable()));
    public static final DeferredItem<Item> LIQUID_NITROGEN_BUCKET = ModItems.ITEMS.register("liquid_nitrogen_bucket",
            () -> new BucketItem(ModFluids.SOURCE_LIQUID_NITROGEN.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final BaseFlowingFluid.Properties LIQUID_NITROGEN_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.LIQUID_NITROGEN_FLUID_TYPE, SOURCE_LIQUID_NITROGEN,FLOWING_LIQUID_NITROGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(1)
            .block(ModFluids.LIQUID_NITROGEN_BLOCK).bucket(ModFluids.LIQUID_NITROGEN_BUCKET);

    public static final Supplier<FlowingFluid> NITROGEN_GAS = FLUIDS.register("nitrogen_gas",
            () -> new BaseFlowingFluid.Source(ModFluids.NITROGEN_GAS_PROPERTIES));

    public static final Supplier<FlowingFluid> FLOWING_NITROGEN_GAS = FLUIDS.register("flowing_nitrogen_gas",
            () -> new BaseFlowingFluid.Flowing(ModFluids.NITROGEN_GAS_PROPERTIES));

    public static final BaseFlowingFluid.Properties NITROGEN_GAS_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.NITROGEN_GAS_FLUID_TYPE, NITROGEN_GAS, FLOWING_NITROGEN_GAS)
            .bucket(ModItems.NITROGEN_GAS_BUCKET);

    public static final Supplier<FlowingFluid> OXYGEN_GAS = FLUIDS.register("oxygen_gas",
            () -> new BaseFlowingFluid.Source(ModFluids.OXYGEN_GAS_PROPERTIES));

    public static final Supplier<FlowingFluid> FLOWING_OXYGEN_GAS = FLUIDS.register("flowing_oxygen_gas",
            () -> new BaseFlowingFluid.Flowing(ModFluids.OXYGEN_GAS_PROPERTIES));

    public static final BaseFlowingFluid.Properties OXYGEN_GAS_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.OXYGEN_GAS_FLUID_TYPE, OXYGEN_GAS, FLOWING_OXYGEN_GAS)
            .bucket(ModItems.OXYGEN_GAS_BUCKET);



}
