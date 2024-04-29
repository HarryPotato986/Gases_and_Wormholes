package io.github.HarryPotato986.Gases_and_Wormholes.init.fluid;

import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.data.ForgeFluidTagsProvider;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

import java.util.Vector;

import static io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes.REGISTRATE;

public class FluidTypesInit {

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Gases_and_Wormholes.MODID);

    public static final RegistryObject<FluidType> LIQUID_NITROGEN_FLUID_TYPE = registerFluidType("liquid_nitrogen_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xffc2e9ff,
                new Vector3f(194f / 255f,233f / 255f,255f / 255f),
                FluidType.Properties.create().viscosity(5).density(15)));

    public static final RegistryObject<FluidType> NITROGEN_GAS_FLUID_TYPE = registerFluidType("nitrogen_gas_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xffc1ced6,
                new Vector3f(193f / 255f,206f / 255f,214f / 255f),
                FluidType.Properties.create()));

    public static final RegistryObject<FluidType> OXYGEN_GAS_FLUID_TYPE = registerFluidType("oxygen_gas_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xff5eb4ff,
                    new Vector3f(94f / 255f,180f / 255f,255f / 255f),
                    FluidType.Properties.create()));



    private static RegistryObject<FluidType> registerFluidType(String name, FluidType fluidType) {
        return FLUID_TYPES.register(name, () -> fluidType);
    }
}
