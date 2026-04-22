package io.github.HarryPotato986.Gases_and_Wormholes.init.item;


import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.data.Pair;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class WormholeGeneratorBlockItem extends BlockItem {

    public WormholeGeneratorBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext ctx) {
        InteractionResult result = super.place(ctx);
        if (result != InteractionResult.FAIL)
            return result;
        if (ctx.getLevel().isClientSide())
            CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> showBounds(ctx));
        return result;
    }

    @OnlyIn(Dist.CLIENT)
    public void showBounds(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        if (!(ctx.getPlayer()instanceof LocalPlayer localPlayer))
            return;

        Direction facing =  ctx.getHorizontalDirection().getOpposite();
        BlockPos offset;
        switch (facing) {
            case NORTH -> {offset = new BlockPos(0, 1, 1);}
            case SOUTH -> {offset = new BlockPos(0, 1, -1);}
            case EAST -> {offset = new BlockPos(-1, 1, 0);}
            case WEST -> {offset = new BlockPos(1, 1, 0);}
            default -> {return;}
        }

        Outliner.getInstance().showAABB(Pair.of("wormhole_generator", pos.offset(offset)), new AABB(pos.offset(offset)).inflate(1))
                .colored(0xFF_ff5d6c);
        CreateLang.translate("large_water_wheel.not_enough_space")
                .color(0xFF_ff5d6c)
                .sendStatus(localPlayer);
    }


}
