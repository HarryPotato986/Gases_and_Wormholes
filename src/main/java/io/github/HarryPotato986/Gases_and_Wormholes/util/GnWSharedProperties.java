package io.github.HarryPotato986.Gases_and_Wormholes.util;

import com.simibubi.create.foundation.data.SharedProperties;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

@MethodsReturnNonnullByDefault
public class GnWSharedProperties extends SharedProperties {

    public static Block bedrock() {
        return Blocks.BEDROCK;
    }
}
