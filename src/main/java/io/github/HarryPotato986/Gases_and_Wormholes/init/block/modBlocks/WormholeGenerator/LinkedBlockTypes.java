package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.AllBlocks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public enum LinkedBlockTypes implements StringRepresentable {
    NONE("none", () -> ItemStack.EMPTY),
    SHAFT("model", () -> new ItemStack(AllBlocks.SHAFT.get())),
    PIPE("pipe", () -> new ItemStack(AllBlocks.FLUID_PIPE.get()));
    //COG("cog", () -> new ItemStack(AllBlocks.COGWHEEL.get()));


    private final String name;
    private final Supplier<ItemStack> stackSupplier;

    LinkedBlockTypes(String name, Supplier<ItemStack> stackSupplier) {
        this.name = name;
        this.stackSupplier = stackSupplier;
    }

    public ItemStack getItemStack() {
        return this.stackSupplier.get();
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
