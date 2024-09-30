package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.FACING;

public class LinkedBlockRenderer extends KineticBlockEntityRenderer<LinkedBlockEntity> {
    public LinkedBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(LinkedBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //if (Backend.canUseInstancing(be.getLevel())) return;

        Direction direction = be.getBlockState().getValue(FACING);
        BlockPos pos = be.getBlockPos();
        boolean BEInFrontIsKinetic = be.isBEInFrontKinetic();

        LinkedBlockEntity linkedPartner = be.getLinkedPartnerBE();
        boolean BEInFrontOfPartnerIsKinetic;
        if(linkedPartner != null) {
            BEInFrontOfPartnerIsKinetic = linkedPartner.isBEInFrontKinetic();
        } else {BEInFrontOfPartnerIsKinetic = false;}

        if(BEInFrontIsKinetic || BEInFrontOfPartnerIsKinetic) {
            VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

            int lightInFront = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(direction));

            SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), direction);

            standardKineticRotationTransform(shaft, be, lightInFront).renderInto(ms, vb);
        }
    }
}
