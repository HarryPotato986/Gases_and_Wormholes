package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;


public class WormholeGeneratorShaftInstance extends KineticBlockEntityInstance<WormholeGeneratorKineticEntity> {

    protected final RotatingData shaft;
    final Direction direction;
    private Direction opposite;

    public WormholeGeneratorShaftInstance(MaterialManager materialManager, WormholeGeneratorKineticEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockState.getValue(AtmosphereExtractor.FACING);

        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, blockState, direction).createInstance();

        setup(shaft);
    }

    @Override
    public void update() {
        updateRotation(shaft);
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(direction);
        relight(behind, shaft);
    }

    @Override
    protected void remove() {
        shaft.delete();
    }
}
