package io.github.HarryPotato986.Gases_and_Wormholes.init.fluid;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ItemInit;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidInit {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Gases_and_Wormholes.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_LIQUID_NITROGEN = FLUIDS.register("liquid_nitrogen_fluid",
            () -> new ForgeFlowingFluid.Source(FluidInit.LIQUID_NITROGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_LIQUID_NITROGEN = FLUIDS.register("flowing_liquid_nitrogen_fluid",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.LIQUID_NITROGEN_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties LIQUID_NITROGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypesInit.LIQUID_NITROGEN_FLUID_TYPE, SOURCE_LIQUID_NITROGEN,FLOWING_LIQUID_NITROGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(BlockInit.LIQUID_NITROGEN_BLOCK)
            .bucket(ItemInit.LIQUID_NITROGEN_BUCKET);


}
