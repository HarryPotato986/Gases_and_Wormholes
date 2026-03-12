package io.github.HarryPotato986.Gases_and_Wormholes.init.datagen;

import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Gases_and_Wormholes.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.TEMP_BLOCK);
        horizontalBlock(ModBlocks.ATMOSPHERE_EXTRACTOR.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/atmosphere_extractor")));
    }

    private void blockWithItem(BlockEntry<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
