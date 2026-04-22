package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.FACING;
import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.HAS_SHAFT;

public class LinkedBlockVisual extends KineticBlockEntityVisual<LinkedBlockEntity> {

    protected RotatingInstance shaft;
    final Direction direction;
    private final Direction opposite;

    public LinkedBlockVisual(VisualizationContext context, LinkedBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();

        updateShaftVisibility();
    }

    @Override
    public void update(float pt) {
        updateShaftVisibility();
    }

    @Override
    public void updateLight(float partialTick) {
        if (this.shaft != null) {
            BlockPos behind = pos.relative(direction);
            relight(behind, shaft);
        }
    }

    private void updateShaftVisibility() {
        //boolean shouldRender = blockEntity.shouldRenderShaft();
        boolean shouldRender = blockEntity.getBlockState().getValue(HAS_SHAFT);

        // If it should render and doesn't exist yet, create it
        if (shouldRender) {
            if (shaft == null) {
                shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                        .createInstance();

                shaft.setup(blockEntity)
                        .setPosition(getVisualPosition())
                        .rotateToFace(Direction.NORTH, opposite)
                        .setChanged();

                BlockPos behind = pos.relative(direction);
                relight(behind, shaft);
            } else {
                shaft.setup(blockEntity).setChanged();
            }
        }
        // If it shouldn't render, but it does exist, delete it
        else if (!shouldRender && shaft != null) {
            shaft.delete();
            shaft = null;
        }
    }

    @Override
    protected void _delete() {
        if (shaft != null) {
            shaft.delete();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(shaft);
    }
}
