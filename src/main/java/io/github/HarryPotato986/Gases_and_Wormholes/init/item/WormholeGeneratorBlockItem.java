package io.github.HarryPotato986.Gases_and_Wormholes.init.item;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.waterwheel.LargeWaterWheelBlock;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> showBounds(ctx));
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

        CreateClient.OUTLINER.showAABB(Pair.of("wormhole_generator", pos.offset(offset)), new AABB(pos.offset(offset)).inflate(1))
                .colored(0xFF_ff5d6c);
        Lang.translate("large_water_wheel.not_enough_space")
                .color(0xFF_ff5d6c)
                .sendStatus(localPlayer);
    }
}
