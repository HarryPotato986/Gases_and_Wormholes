package io.github.HarryPotato986.Gases_and_Wormholes.init.fluid;

import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ItemInit;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes.REGISTRATE;

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

    public static final RegistryObject<FlowingFluid> NITROGEN_GAS = FLUIDS.register("nitrogen_gas",
            () -> new ForgeFlowingFluid.Source(FluidInit.NITROGEN_GAS_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_NITROGEN_GAS = FLUIDS.register("flowing_nitrogen_gas",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.NITROGEN_GAS_PROPERTIES));

    public static final ForgeFlowingFluid.Properties NITROGEN_GAS_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypesInit.NITROGEN_GAS_FLUID_TYPE, NITROGEN_GAS, FLOWING_NITROGEN_GAS)
            .bucket(ItemInit.NITROGEN_GAS_BUCKET);

    public static final RegistryObject<FlowingFluid> OXYGEN_GAS = FLUIDS.register("oxygen_gas",
            () -> new ForgeFlowingFluid.Source(FluidInit.OXYGEN_GAS_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_OXYGEN_GAS = FLUIDS.register("flowing_oxygen_gas",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.OXYGEN_GAS_PROPERTIES));

    public static final ForgeFlowingFluid.Properties OXYGEN_GAS_PROPERTIES = new ForgeFlowingFluid.Properties(
            FluidTypesInit.OXYGEN_GAS_FLUID_TYPE, OXYGEN_GAS, FLOWING_OXYGEN_GAS)
            .bucket(ItemInit.OXYGEN_GAS_BUCKET);



}
