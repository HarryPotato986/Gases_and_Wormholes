package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.ModFluids;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.AtmosphereExtractorMenu;
import io.github.HarryPotato986.Gases_and_Wormholes.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AtmosphereExtractorEntity extends KineticBlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case NITROGEN_SLOT, OXYGEN_SLOT -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
                //case OUTPUT_SLOT -> false;
                //case ENERGY_ITEM_SLOT -> stack.getItem() == ItemInit.BEDROCK_DUST.get();
                default -> super.isItemValid(slot, stack);
            };
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };



    private static final int OXYGEN_SLOT = 0;
    private static final int NITROGEN_SLOT = 1;
    //private static final int OUTPUT_SLOT = 2;
    //private static final int ENERGY_ITEM_SLOT = 3;

    private boolean[] ACQUIRED_FLUID = new boolean[]{false,false};
    private boolean[] DISTRIBUTED_FLUID = new boolean[]{false,false};


    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    private final FluidTank NITROGEN_TANK = createFluidTank(2000);
    private final FluidTank OXYGEN_TANK = createFluidTank(2000);


    private FluidTank createFluidTank(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }
        };
    }

    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(64000, 200) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };

    }


    public AtmosphereExtractorEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AtmosphereExtractorEntity.this.progress;
                    case 1 -> AtmosphereExtractorEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AtmosphereExtractorEntity.this.progress = pValue;
                    case 1 -> AtmosphereExtractorEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }


    public FluidStack getFluid(int index) {
        return switch (index) {
            case 0 -> NITROGEN_TANK.getFluid();
            case 1 -> OXYGEN_TANK.getFluid();
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
    }


    public IItemHandler getItemHandler(Direction side) {
        if(side == null) {
            return this.itemHandler;
        }
        Direction facing = this.getBlockState().getValue(AtmosphereExtractor.FACING);
        if (side == Direction.UP || side == facing || side == facing.getClockWise() || side == facing.getCounterClockWise()) {
            return null;
        }
        if (side == facing.getOpposite() || side == Direction.DOWN) {
            return this.itemHandler;
        }
        return null;
    }

    public IFluidHandler getFluidHandler(Direction side) {
        if(side == null) {
            return null;
        }
        Direction facing = this.getBlockState().getValue(AtmosphereExtractor.FACING);
        if (side == Direction.UP || side == Direction.DOWN || side == facing || side == facing.getOpposite()) {
            return null;
        }
        if (side == facing.getClockWise()) {
            return this.NITROGEN_TANK;
        }
        if (side == facing.getCounterClockWise()) {
            return this.OXYGEN_TANK;
        }
        return null;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.gasesandwormholes.atmosphere_extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AtmosphereExtractorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void read(CompoundTag pTag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(pTag, registries, clientPacket);
        itemHandler.deserializeNBT(registries, pTag.getCompound("atmosphere_extractor.inventory"));
        progress = pTag.getInt("atmosphere_extractor.progress");
        NITROGEN_TANK.readFromNBT(registries, pTag.getCompound("atmosphere_extractor.NitrogenTank"));
        OXYGEN_TANK.readFromNBT(registries, pTag.getCompound("atmosphere_extractor.OxygenTank"));
    }

    @Override
    protected void write(CompoundTag pTag, HolderLookup.Provider registries, boolean clientPacket) {
        pTag.put("atmosphere_extractor.inventory", itemHandler.serializeNBT(registries));
        pTag.putInt("atmosphere_extractor.progress", progress);
        pTag.put("atmosphere_extractor.NitrogenTank", NITROGEN_TANK.writeToNBT(registries, new CompoundTag()));
        pTag.put("atmosphere_extractor.OxygenTank", OXYGEN_TANK.writeToNBT(registries, new CompoundTag()));

        super.write(pTag, registries, clientPacket);
    }

    public void tick(Level level, BlockPos pPos, BlockState pState) {
        super.tick();
        fillUpOnFluid();

        updateMaxProgress();

        if(checkIfTankHadSpace(NITROGEN_TANK,78) && checkIfTankHadSpace(OXYGEN_TANK,21)) {
            progress++;
            setChanged(level, pPos, pState);

            if(progress >= maxProgress) {
                generateFluid(NITROGEN_TANK, ModFluids.NITROGEN_GAS.get(),78);
                generateFluid(OXYGEN_TANK, ModFluids.OXYGEN_GAS.get(),21);
                progress = 0;
            }
        }

        /*
        fillUpOnEnergy();
        fillUpOnFluid();

        if(isOutputSlotEmptyOrReceivable() && hasRecipe()) {
            increaseCraftingProgress();
            extractEnergy();
            setChanged(level, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                extractFluid();
                resetProgress();
            }
        }else{
            resetProgress();
        }
        */
    }

    private void generateFluid(FluidTank tank, Fluid fluid, int quantity) {
        tank.fill(new FluidStack(fluid, quantity), IFluidHandler.FluidAction.EXECUTE);
    }

    private boolean checkIfTankHadSpace(FluidTank tank, int inputQuantity) {
        return tank.getFluidAmount() + inputQuantity <= tank.getCapacity();
    }

    private void updateMaxProgress() {
        float newProductionSpeed = Math.max(((3 * 128) / Math.abs(getSpeed())), 1.0f);
        maxProgress = Math.round(newProductionSpeed * 20.0f);
    }


    private void fillUpOnFluid() {
        if(this.itemHandler.getStackInSlot(OXYGEN_SLOT).isEmpty()) {
            ACQUIRED_FLUID[OXYGEN_SLOT] = false;
            DISTRIBUTED_FLUID[OXYGEN_SLOT] = false;
        }else if(hasFluidSourceInSlot(OXYGEN_SLOT)) {
            transferItemFluidToTank(OXYGEN_SLOT, this.OXYGEN_TANK, ModFluids.OXYGEN_GAS.get());
        }

        if(this.itemHandler.getStackInSlot(NITROGEN_SLOT).isEmpty()) {
            ACQUIRED_FLUID[NITROGEN_SLOT] = false;
            DISTRIBUTED_FLUID[NITROGEN_SLOT] = false;
        }else if(hasFluidSourceInSlot(NITROGEN_SLOT)) {
            transferItemFluidToTank(NITROGEN_SLOT, this.NITROGEN_TANK, ModFluids.NITROGEN_GAS.get());
        }
    }

    private void transferItemFluidToTank(int fluidInputSlot, FluidTank fluidTank, Fluid fluid) {
        if(!ACQUIRED_FLUID[fluidInputSlot] && this.itemHandler.getStackInSlot(fluidInputSlot).getItem() == Items.BUCKET){
            FluidActionResult result = FluidUtil.tryFillContainer(this.itemHandler.getStackInSlot(fluidInputSlot), fluidTank, Integer.MAX_VALUE, null, true);
            if (result.result != ItemStack.EMPTY) {
                itemHandler.setStackInSlot(fluidInputSlot, result.result);
                DISTRIBUTED_FLUID[fluidInputSlot] = true;
            }
        } else if(!DISTRIBUTED_FLUID[fluidInputSlot]) {
            FluidActionResult result = FluidUtil.tryEmptyContainer(this.itemHandler.getStackInSlot(fluidInputSlot), fluidTank, Integer.MAX_VALUE, null, true);
            if (result.result != ItemStack.EMPTY) {
                itemHandler.setStackInSlot(fluidInputSlot, result.result);
                ACQUIRED_FLUID[fluidInputSlot] = true;
            }
        }
    }

    private boolean hasFluidSourceInSlot(int fluidInputSlot) {
        return !this.itemHandler.getStackInSlot(fluidInputSlot).isEmpty() &&
                this.itemHandler.getStackInSlot(fluidInputSlot).getCapability(Capabilities.FluidHandler.ITEM) != null;
    }


    @Override
    public @NotNull ClientboundBlockEntityDataPacket getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return super.getUpdateTag(registries);
    }
}


