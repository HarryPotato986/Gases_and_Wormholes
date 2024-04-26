package io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.renderer;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.AtmosphereExtractorEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class AtmosphereExtractorRenderer extends KineticBlockEntityRenderer<AtmosphereExtractorEntity> {

    public AtmosphereExtractorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(AtmosphereExtractorEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel())) return;

        Direction direction = be.getBlockState()
                .getValue(FACING);

        SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), direction);

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        //shaft.renderInto(ms, vb);
        standardKineticRotationTransform(shaft, be, light).renderInto(ms, vb);

    }
}
