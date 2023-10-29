package io.github.HarryPotato986.Gases_and_Wormholes.init.datagen.loot;

import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
    this.dropSelf(BlockInit.TEMP_BLOCK.get());
    this.dropSelf(BlockInit.ATMOSPHERE_EXTRACTOR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
