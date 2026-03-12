package io.github.HarryPotato986.Gases_and_Wormholes.init.fluid;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ModFluidTypes {

    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.parse("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.parse("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = ResourceLocation.parse("block/water_overlay");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Gases_and_Wormholes.MODID);

    public static final Supplier<FluidType> LIQUID_NITROGEN_FLUID_TYPE = registerFluidType("liquid_nitrogen_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xffc2e9ff,
                new Vector3f(194f / 255f,233f / 255f,255f / 255f),
                FluidType.Properties.create().viscosity(5).density(15)));

    public static final Supplier<FluidType> NITROGEN_GAS_FLUID_TYPE = registerFluidType("nitrogen_gas_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xffc1ced6,
                new Vector3f(193f / 255f,206f / 255f,214f / 255f),
                FluidType.Properties.create()));

    public static final Supplier<FluidType> OXYGEN_GAS_FLUID_TYPE = registerFluidType("oxygen_gas_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xff5eb4ff,
                    new Vector3f(94f / 255f,180f / 255f,255f / 255f),
                    FluidType.Properties.create()));



    private static Supplier<FluidType> registerFluidType(String name, FluidType fluidType) {
        return FLUID_TYPES.register(name, () -> fluidType);
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
