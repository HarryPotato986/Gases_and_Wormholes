package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.TickableInstance;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.FACING;

public class LinkedBlockInstance extends KineticBlockEntityInstance<LinkedBlockEntity> implements TickableInstance {

    protected final RotatingData shaft;
    final Direction direction;
    private Direction opposite;
    private final LinkedBlockEntity BE;
    private final LinkedBlockEntity linkedPartnerBE;
    private boolean active;

    public LinkedBlockInstance(MaterialManager materialManager, LinkedBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        BE = blockEntity;
        linkedPartnerBE = (LinkedBlockEntity) BE.getLinkedPartnerBlockEntity();

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, blockState, direction).createInstance();

        /*
        boolean isBlockEntityInFrontKinetic = BE.isBlockEntityInFrontKinetic();
        boolean isBlockEntityInFrontPartnerKinetic;
        if(linkedPartnerBE != null) {
            isBlockEntityInFrontPartnerKinetic = linkedPartnerBE.isBlockEntityInFrontKinetic();
        } else {isBlockEntityInFrontPartnerKinetic = false;}

        if(!(isBlockEntityInFrontKinetic || isBlockEntityInFrontPartnerKinetic)) {
            active = false;
        } else if((isBlockEntityInFrontKinetic || isBlockEntityInFrontPartnerKinetic) && !active) {
            setup(shaft);
            active = true;
        }
         */

        //setup(shaft);
    }

    /*
    @Override
    public boolean shouldReset() {
        boolean isBlockEntityInFrontKinetic = BE.isBlockEntityInFrontKinetic();
        boolean isBlockEntityInFrontPartnerKinetic;
        if(linkedPartnerBE != null) {
            isBlockEntityInFrontPartnerKinetic = linkedPartnerBE.isBlockEntityInFrontKinetic();
        } else {isBlockEntityInFrontPartnerKinetic = false;}

        if(!(isBlockEntityInFrontKinetic || isBlockEntityInFrontPartnerKinetic) && active) {
            return true;
        } else if((isBlockEntityInFrontKinetic || isBlockEntityInFrontPartnerKinetic) && !active) {
            return true;
        }

        return false;
    }
     */

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

    @Override
    public void tick() {

    }
}


