package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum WormholeGeneratorBlockTypes implements StringRepresentable {
    HELPER("helper"),
    CORE("core"),
    FLUID_INPUT("fluid_input"),
    ITEM_INPUT("item_input"),
    KINETIC_INPUT("kinetic_input");


    private final String name;

    WormholeGeneratorBlockTypes(String pName) {
        this.name = pName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
