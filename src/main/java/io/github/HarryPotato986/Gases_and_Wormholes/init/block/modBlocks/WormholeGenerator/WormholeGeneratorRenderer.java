package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.WormholeGeneratorCore.FACING;

public class WormholeGeneratorRenderer implements BlockEntityRenderer<WormholeGeneratorCoreEntity> {

    public WormholeGeneratorRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(WormholeGeneratorCoreEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        VertexConsumer pConsumer = pBuffer.getBuffer(RenderType.endPortal());
        Matrix4f pPose = pPoseStack.last().pose();
        Direction facing = pBlockEntity.getBlockState().getValue(FACING);

        switch (facing) {
            case NORTH -> this.renderFace(pPose, pConsumer, -1.0F, 2.0F, -1.0F, 2.0F, 1.876F, 1.876F, 1.876F, 1.876F);
            case SOUTH -> this.renderFace(pPose, pConsumer, -1.0F, 2.0F, 2.0F, -1.0F, -0.876F, -0.876F, -0.876F, -0.876F);
            case EAST -> this.renderFace(pPose, pConsumer, -0.876F, -0.876F, -1.0F, 2.0F, -1.0F, 2.0F, 2.0F, -1.0F);
            case WEST -> this.renderFace(pPose, pConsumer, 1.876F, 1.876F, 2.0F, -1.0F, -1.0F, 2.0F, 2.0F, -1.0F);
        }

        //this.renderFace(pPose, pConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F); //South
        //this.renderFace(pPose, pConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F); //North
        //this.renderFace(pPose, pConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F); //East
        //this.renderFace(pPose, pConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F); //West
    }

    private void renderFace(Matrix4f pPose, VertexConsumer pConsumer, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3) {
        pConsumer.vertex(pPose, pX0, pY0, pZ0).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).endVertex();
    }
}
