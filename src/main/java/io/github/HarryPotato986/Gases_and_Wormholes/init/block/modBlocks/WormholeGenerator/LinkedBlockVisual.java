package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.*;

public class LinkedBlockVisual extends KineticBlockEntityVisual<LinkedBlockEntity> {

    protected RotatingInstance model;
    final Direction direction;
    private final Direction opposite;

    public LinkedBlockVisual(VisualizationContext context, LinkedBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();

        updateModel();
    }

    @Override
    public void update(float pt) {
        updateModel();
    }

    @Override
    public void updateLight(float partialTick) {
        if (this.model != null) {
            BlockPos behind = pos.relative(direction);
            relight(behind, model);
        }
    }

    private void updateModel() {
        //boolean shouldRender = blockEntity.shouldRenderShaft();
        LinkedBlockTypes type = blockEntity.getBlockState().getValue(TYPE);
        boolean hasShaft = type == LinkedBlockTypes.SHAFT;
        //boolean hasCog = type == LinkedBlockTypes.COG; Code is here in case I change my mind about supporting cogs.
        //They just render really stupidly, sticking out the sides and z-fighting.
        boolean shouldRender = hasShaft; //|| hasCog;

        // If it should render and doesn't exist yet, create it
        if (shouldRender) {
            if (model == null) {
                if (hasShaft) {
                    model = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                            .createInstance();

                    model.setup(blockEntity)
                            .setPosition(getVisualPosition())
                            .rotateToFace(Direction.NORTH, opposite)
                            .setChanged();
                } /*else {
                    model = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.COGWHEEL))
                            .createInstance();

                    model.setup(blockEntity)
                            .setPosition(getVisualPosition())
                            .rotateToFace(Direction.NORTH, opposite)
                            .setChanged();
                }*/

                BlockPos behind = pos.relative(direction);
                relight(behind, model);
            } else {
                model.setup(blockEntity).setChanged();
            }
        }
        // If it shouldn't render, but it does exist, delete it
        else if (model != null) {
            model.delete();
            model = null;
        }
    }

    @Override
    protected void _delete() {
        if (model != null) {
            model.delete();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(model);
    }
}
