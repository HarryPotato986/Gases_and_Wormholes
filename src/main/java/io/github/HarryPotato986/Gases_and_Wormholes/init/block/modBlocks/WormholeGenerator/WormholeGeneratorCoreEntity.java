package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.ModFluids;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ModItems;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.WormholeGeneratorMenu;
import io.github.HarryPotato986.Gases_and_Wormholes.networking.packet.WormholeData;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.WormholeGeneratorCore.FACING;
import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.WormholeGeneratorCore.RUNNING;

public class WormholeGeneratorCoreEntity extends KineticBlockEntity implements MenuProvider {
    private final ItemStackHandler tempItemHandler = new ItemStackHandler(2) {
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
                case LIQUID_NITROGEN_SLOT -> stack.getItem() == ModFluids.LIQUID_NITROGEN_BUCKET.get() || stack.getItem() == Items.BUCKET;
                case BEDROCK_DUST_INPUT -> stack.getItem() == ModItems.BEDROCK_DUST.get();
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    public boolean running;

    public BlockPos itemInput;
    public BlockPos fluidInput;
    private BlockPos kineticInput;


    private static final int LIQUID_NITROGEN_SLOT = 0;
    private static final int BEDROCK_DUST_INPUT = 1;

    private boolean ACQUIRED_FLUID = false;
    private boolean DISTRIBUTED_FLUID = false;

    private final FluidTank tempTank = createFluidTank(10000);


    public String X1 = "";
    public String Y1 = "";
    public String Z1 = "";
    public String X2 = "";
    public String Y2 = "";
    public String Z2 = "";
    public int Wormhole1Facing = 0;
    public int Wormhole2Facing = 0;
    public int WormholeSize = 0;


    //public BlockPos[] masterList;
    //public Map<BlockPos, BlockPos> lookUpTable = new HashMap<>();
    //private List<BlockPos> setupQueue = new ArrayList<>();
    //private List<BlockPos[]> placementPosQueue = new ArrayList<>();
    //private List<BlockState[]> placementStateQueue = new ArrayList<>();
    //private int setupDelay = 0;
    //private int placementDelay = 0;




    public Boolean isRunning() {
        return getLevel().getBlockState(getBlockPos()).getValue(RUNNING);
        //return getBlockState().getValue(RUNNING);
    }

    public void setRunning(Boolean running) {
        if (getLevel() != null && running != isRunning()) {
            BlockState state = getBlockState().setValue(RUNNING, running);
            getLevel().setBlock(getBlockPos(), state, 3);
            setBlockState(state);
        }
    }

    private BlockPos findItemInput() {
        Direction facing = this.getBlockState().getValue(FACING);
        BlockPos pos = this.getBlockPos();
        BlockPos offset = switch (facing) {
            case NORTH -> new BlockPos(-1, 0, 0);
            case SOUTH -> new BlockPos(1, 0, 0);
            case WEST -> new BlockPos(0, 0, 1);
            case EAST -> new BlockPos(0, 0, -1);
            default -> null;
        };
        if (offset == null) {
            return null;
        }
        return pos.offset(offset);
    }

    private BlockPos findFluidInput() {
        Direction facing = this.getBlockState().getValue(FACING);
        BlockPos pos = this.getBlockPos();
        BlockPos offset = switch (facing) {
            case NORTH -> new BlockPos(1, 0, 0);
            case SOUTH -> new BlockPos(-1, 0, 0);
            case WEST -> new BlockPos(0, 0, -1);
            case EAST -> new BlockPos(0, 0, 1);
            default -> null;
        };
        if (offset == null) {
            return null;
        }
        return pos.offset(offset);
    }

    private BlockPos findKineticInput() {
        Direction facing = this.getBlockState().getValue(FACING);
        BlockPos pos = this.getBlockPos();
        BlockPos offset = switch (facing) {
            case NORTH -> new BlockPos(0, 0, -1);
            case SOUTH -> new BlockPos(0, 0, 1);
            case WEST -> new BlockPos(-1, 0, 0);
            case EAST -> new BlockPos(1, 0, 0);
            default -> null;
        };
        if (offset == null) {
            return null;
        }
        return pos.offset(offset);
    }

    private ItemStackHandler getLocalItemHandler() {
        if (itemInput == null) {
            itemInput = findItemInput();
            //System.out.println("ItemHandlerPOS: " + itemInput.toString());
        }

        if (!this.hasLevel()) {
            //System.out.println("Used tempItemHandler because no level");
            return tempItemHandler;
        }

        BlockEntity BE = this.getLevel().getBlockEntity(itemInput);
        if (BE instanceof WormholeGeneratorItemEntity) {
            return ((WormholeGeneratorItemEntity) BE).itemHandler;
        }
        System.out.println("Used tempItemHandler because no BE");
        return tempItemHandler;
    }

    public IItemHandler getItemHandler() {
        if (itemInput == null) {
            itemInput = findItemInput();
            System.out.println("ItemHandlerPOS: " + itemInput.toString());
        }

        if (!this.hasLevel()) {
            //System.out.println("Used tempItemHandler because no level");
            return tempItemHandler;
        }

        BlockEntity BE = this.getLevel().getBlockEntity(itemInput);
        if (BE instanceof WormholeGeneratorItemEntity) {
            return ((WormholeGeneratorItemEntity) BE).itemHandler;
        }
        System.out.println("Used tempItemHandler because no BE");
        return tempItemHandler;
    }

    private FluidTank getFluidTank() {
        if (fluidInput == null) {
            fluidInput = findFluidInput();
            //System.out.println("FluidTankPOS: " + fluidInput.toString());
        }

        if (!this.hasLevel()) {
            //System.out.println("Used tempTank because no level");
            return tempTank;
        }

        BlockEntity BE = this.getLevel().getBlockEntity(fluidInput);
        if (BE instanceof WormholeGeneratorFluidEntity) {
            return ((WormholeGeneratorFluidEntity) BE).LIQUID_NITROGEN_TANK;
        }
        System.out.println("Used tempTank because no BE");
        return tempTank;
    }

    /*
    private final Map<Direction, LazyOptional<DirectionWrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper(getItemHandler(),
                    new InventoryDirectionEntry(Direction.DOWN, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.NORTH, BEDROCK_DUST_INPUT, true),
                    new InventoryDirectionEntry(Direction.SOUTH, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.EAST, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.WEST, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.UP, BEDROCK_DUST_INPUT, false)).directionsMap;
    */

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;

    private FluidTank createFluidTank(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    blockUpdateAll(level, getBlockPos(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }
        };
    }

    public void blockUpdateAll(Level level, BlockPos pos, BlockState state, int flags) {
        level.sendBlockUpdated(pos, state, state, flags);
        for(int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    BlockState offsetState = level.getBlockState(pos.offset(offset));
                    level.sendBlockUpdated(pos.offset(offset), offsetState, offsetState, flags);
                }
            }
        }
    }

    public WormholeGeneratorCoreEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> WormholeGeneratorCoreEntity.this.progress;
                    case 1 -> WormholeGeneratorCoreEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> WormholeGeneratorCoreEntity.this.progress = pValue;
                    case 1 -> WormholeGeneratorCoreEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        this.itemInput = findItemInput();
        this.fluidInput = findFluidInput();
        this.kineticInput = findKineticInput();
    }


    public void tick(Level level, BlockPos pPos, BlockState pState) {
        super.tick();
        fillUpOnFluid();
        fillUpOnDust();

        //System.out.println("is running: " + isRunning());
        //System.out.println("is running: " + pState.getValue(RUNNING));
        //System.out.println("is running: " + (level.getBlockState(pPos).getValue(RUNNING)));
        if (isRunning()) {
            System.out.println("it do be running");
            consumeFluid();
            consumeDust();

            /*
            //System.out.println(placementPosQueue.get(0)[0] + " " + placementPosQueue.get(0)[1]);
            if (!level.isClientSide) {
                if (placementDelay <= 0) {
                    while (!placementPosQueue.isEmpty()) {
                        BlockPos[] pairPos = placementPosQueue.removeFirst();
                        BlockState[] pairState = placementStateQueue.removeFirst();
                        BlockPos partner1Pos = pairPos[0];
                        BlockPos partner2Pos = pairPos[1];
                        BlockState partner1State = pairState[0];
                        BlockState partner2State = pairState[1];

                        level.setBlockAndUpdate(partner1Pos, partner1State);
                        level.setBlockAndUpdate(partner2Pos, partner2State);
                    }
                } else {
                    placementDelay--;
                }

                if (setupDelay <= 0) {
                    while (!setupQueue.isEmpty()) {
                        System.out.println("Queue is working");
                        BlockPos partner1 = setupQueue.removeFirst();
                        System.out.println("partner 1: " + partner1);
                        BlockPos partner2 = lookUpTable.get(partner1);
                        System.out.println("partner 2: " + partner2);

                        if (level.getBlockEntity(partner1) instanceof LinkedBlockEntity partner1BE && level.getBlockEntity(partner2) instanceof LinkedBlockEntity partner2BE) {
                            partner1BE.setLinkedPartner(partner2);
                            System.out.println("set partner for block at: " + partner1);
                            partner2BE.setLinkedPartner(partner1);
                            System.out.println("set partner for block at: " + partner2);
                        }

                    }
                } else {
                    setupDelay--;
                }
            }
            */
        }
    }

    private void consumeFluid() {
        FluidTank fluidTank = getFluidTank();
        if (fluidTank.isEmpty()) {
            //beginShutdown();
            return;
        }
        int drainAmount = 2 * (WormholeSize * WormholeSize);
        fluidTank.drain(2, IFluidHandler.FluidAction.EXECUTE);  //120 every 3 sec : 2 every tick
    }

    private void consumeDust() {
        ItemStackHandler itemStack = getLocalItemHandler();
        if (progress <= 0) {
            //beginShutdown();
            return;
        }
        int consumeAmount = (WormholeSize * WormholeSize);
        itemStack.extractItem(BEDROCK_DUST_INPUT, consumeAmount, false);
    }

    private void updateMaxProgress() {
        //float newProductionSpeed = Math.max(((3 * 128) / Math.abs(getSpeed())), 1.0f);
        //maxProgress = Math.round(newProductionSpeed * 20.0f);
    }

    private void fillUpOnDust() {
        if(this.progress <= 100) {
            if(!getItemHandler().getStackInSlot(BEDROCK_DUST_INPUT).isEmpty()) {
                getItemHandler().extractItem(BEDROCK_DUST_INPUT, 1, false);
                this.progress += 100;
            }
        }
    }

    private void fillUpOnFluid() {
        if(this.getItemHandler().getStackInSlot(LIQUID_NITROGEN_SLOT).isEmpty()) {
            ACQUIRED_FLUID = false;
            DISTRIBUTED_FLUID = false;
        }else if(hasFluidSourceInSlot(LIQUID_NITROGEN_SLOT)) {
            transferItemFluidToTank(LIQUID_NITROGEN_SLOT, getFluidTank(), ModFluids.SOURCE_LIQUID_NITROGEN.get());
        }
    }

    private void transferItemFluidToTank(int fluidInputSlot, FluidTank fluidTank, Fluid fluid) {
        if(!ACQUIRED_FLUID && getItemHandler().getStackInSlot(fluidInputSlot).getItem() == Items.BUCKET){
            FluidActionResult result = FluidUtil.tryFillContainer(getItemHandler().getStackInSlot(fluidInputSlot), fluidTank, Integer.MAX_VALUE, null, true);
            if (result.result != ItemStack.EMPTY) {
                getLocalItemHandler().setStackInSlot(fluidInputSlot, result.result);
                DISTRIBUTED_FLUID = true;
            }
        } else if(!DISTRIBUTED_FLUID) {
            FluidActionResult result = FluidUtil.tryEmptyContainer(getItemHandler().getStackInSlot(fluidInputSlot), fluidTank, Integer.MAX_VALUE, null, true);
            if (result.result != ItemStack.EMPTY) {
                getLocalItemHandler().setStackInSlot(fluidInputSlot, result.result);
                ACQUIRED_FLUID = true;
            }
        }
    }

    private void fillTankWithFluid(FluidStack stack, ItemStack container, FluidTank fluidTank, int fluidInputSlot) {
        fluidTank.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);

        getItemHandler().extractItem(fluidInputSlot, 1, false);
        getItemHandler().insertItem(fluidInputSlot, container, false);
    }

    private boolean hasFluidSourceInSlot(int fluidInputSlot) {
        return getItemHandler().getStackInSlot(fluidInputSlot).getCount() > 0 &&
                getItemHandler().getStackInSlot(fluidInputSlot).getCapability(Capabilities.FluidHandler.ITEM) != null;
    }

    public FluidStack getFluid() {
        return getFluidTank().getFluid();
    }


    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(getItemHandler().getSlots());
        for(int i = 0; i < getItemHandler().getSlots(); i++) {
            inventory.setItem(i, getItemHandler().getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.gasesandwormholes.wormhole_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WormholeGeneratorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void write(CompoundTag pTag, HolderLookup.Provider registries, boolean clientPacket) {
        //pTag.putBoolean("running", running);
        //System.out.println("after write(): " + running);

        pTag.put("item_input", NbtUtils.writeBlockPos(this.itemInput));
        pTag.put("fluid_input", NbtUtils.writeBlockPos(this.fluidInput));
        pTag.put("kinetic_input", NbtUtils.writeBlockPos(this.kineticInput));

        //pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("wormhole_generator.progress", progress);
        //pTag.put("OxygenTank", LIQUID_NITROGEN_TANK.writeToNBT(new CompoundTag()));

        pTag.putString("x1", X1);
        pTag.putString("y1", Y1);
        pTag.putString("z1", Z1);
        pTag.putString("x2", X2);
        pTag.putString("y2", Y2);
        pTag.putString("z2", Z2);

        pTag.putInt("wormhole_facing_1", Wormhole1Facing);
        pTag.putInt("wormhole_facing_2", Wormhole2Facing);
        pTag.putInt("wormhole_size", WormholeSize);

        /*
        if (isRunning() && masterList != null && lookUpTable != null && masterList.length > 0 && !lookUpTable.isEmpty()) {
            pTag.put("wormholeLocationData", writeWormholeLocationData());
        }
        pTag.put("queues", writeQueueData());
        */

        super.write(pTag, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag pTag, HolderLookup.Provider registries, boolean clientPacket) {
        //running = pTag.getBoolean("running");
        //System.out.println("after read(): " + running);

        super.read(pTag, registries, clientPacket);

        itemInput = NbtUtils.readBlockPos(pTag, "item_input").orElse(null);
        fluidInput = NbtUtils.readBlockPos(pTag, "fluid_input").orElse(null);
        kineticInput = NbtUtils.readBlockPos(pTag, "kinetic_input").orElse(null);

        //itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("wormhole_generator.progress");
        //LIQUID_NITROGEN_TANK.readFromNBT(pTag.getCompound("OxygenTank"));

        X1 = pTag.getString("x1");
        Y1 = pTag.getString("y1");
        Z1 = pTag.getString("z1");
        X2 = pTag.getString("x2");
        Y2 = pTag.getString("y2");
        Z2 = pTag.getString("z2");

        Wormhole1Facing = pTag.getInt("wormhole_facing_1");
        Wormhole2Facing = pTag.getInt("wormhole_facing_2");
        WormholeSize = pTag.getInt("wormhole_size");

        //readWormholeLocationData(pTag);
        //readQueueData(pTag);
    }

    /*
    private CompoundTag writeWormholeLocationData() {
        int masterListLength = masterList.length;
        int lookUpTableLength = lookUpTable.size();
        List<BlockPos> saved = new ArrayList<>();
        CompoundTag locationData = new CompoundTag();

        int i = 1;
        for (BlockPos pos : masterList) {
            if (saved.contains(pos)) {
                continue;
            }
            CompoundTag linkedPair = new CompoundTag();
            linkedPair.put("partner1", NbtUtils.writeBlockPos(pos));
            linkedPair.put("partner2", NbtUtils.writeBlockPos(lookUpTable.get(pos)));
            saved.add(pos);
            saved.add(lookUpTable.get(pos));
            locationData.put("pair" + i, linkedPair);
        }
        locationData.putInt("numberOfPairs", i);
        return locationData;
    }

    private void readWormholeLocationData(CompoundTag pTag) {
        CompoundTag locationData = pTag.getCompound("wormholeLocationData");
        int numberOfPairs = locationData.getInt("numberOfPairs");
        List<BlockPos> tempList = new ArrayList<>();
        lookUpTable.clear();

        for (int i = 0; i < numberOfPairs; i++) {
            CompoundTag linkedPair = locationData.getCompound("pair" + (i + 1));
            BlockPos partner1 = NbtUtils.readBlockPos(linkedPair, "partner1").orElse(null);
            BlockPos partner2 = NbtUtils.readBlockPos(linkedPair, "partner2").orElse(null);
            tempList.add(partner1);
            tempList.add(partner2);
            lookUpTable.put(partner1, partner2);
            lookUpTable.put(partner2, partner1);
        }
        masterList = new BlockPos[tempList.size()];
        masterList = tempList.toArray(masterList);
    }

    private CompoundTag writeQueueData() {
        CompoundTag queueData = new CompoundTag();
        CompoundTag placementQueues = new CompoundTag();
        CompoundTag setupQueueTag = new CompoundTag();

        int placementQueueSize = placementPosQueue.size();
        int setupQueueSize = setupQueue.size();
        placementQueues.putInt("placementQueueSize", placementQueueSize);
        setupQueueTag.putInt("setupQueueSize", setupQueueSize);
        placementQueues.putInt("placementDelay", placementDelay);
        setupQueueTag.putInt("setupDelay", setupDelay);

        for (int i = 0; i < placementQueueSize; i++) {
            CompoundTag linkedPair = new CompoundTag();
            BlockPos partner1Pos = placementPosQueue.get(i)[0];
            BlockPos partner2Pos = placementPosQueue.get(i)[1];
            BlockState partner1State = placementStateQueue.get(i)[0];
            BlockState partner2State = placementStateQueue.get(i)[1];

            linkedPair.put("partner1Pos", NbtUtils.writeBlockPos(partner1Pos));
            linkedPair.put("partner2Pos", NbtUtils.writeBlockPos(partner2Pos));
            linkedPair.put("partner1State", NbtUtils.writeBlockState(partner1State));
            linkedPair.put("partner2State", NbtUtils.writeBlockState(partner2State));

            placementQueues.put("queuedPair" + i, linkedPair);
        }

        for (int i = 0; i < setupQueueSize; i++) {
            setupQueueTag.put("pos" + i, NbtUtils.writeBlockPos(setupQueue.get(i)));
        }

        queueData.put("placementQueues", placementQueues);
        queueData.put("setupQueue", setupQueueTag);
        return queueData;
    }

    private void readQueueData(CompoundTag pTag) {
        HolderGetter<Block> getter = new HolderGetter<Block>() {
            @Override
            public Optional<Holder.Reference<Block>> get(ResourceKey<Block> pResourceKey) {
                return Optional.empty();
            }

            @Override
            public Optional<HolderSet.Named<Block>> get(TagKey<Block> pTagKey) {
                return Optional.empty();
            }
        };
        
        placementPosQueue.clear();
        placementStateQueue.clear();
        setupQueue.clear();
        
        CompoundTag queueData = pTag.getCompound("queues");
        CompoundTag placementQueues = queueData.getCompound("placementQueues");
        CompoundTag setupQueueTag = queueData.getCompound("setupQueue");

        int placementQueueSize = placementQueues.getInt("placementQueueSize");
        int setupQueueSize = setupQueueTag.getInt("setupQueueSize");
        placementDelay = placementQueues.getInt("placementDelay");
        setupDelay = setupQueueTag.getInt("setupDelay");

        for (int i = 0; i < placementQueueSize; i++) {
            CompoundTag linkedPair = placementQueues.getCompound("queuedPair" + i);

            BlockPos partner1Pos = NbtUtils.readBlockPos(linkedPair, "partner1Pos").orElse(null);
            BlockPos partner2Pos = NbtUtils.readBlockPos(linkedPair, "partner2Pos").orElse(null);
            BlockState partner1State = NbtUtils.readBlockState(getter, linkedPair.getCompound("partner1State"));
            BlockState partner2State = NbtUtils.readBlockState(getter, linkedPair.getCompound("partner2State"));
            
            placementPosQueue.add(new BlockPos[]{partner1Pos, partner2Pos});
            placementStateQueue.add(new BlockState[]{partner1State, partner2State});
        }

        for (int i = 0; i < setupQueueSize; i++) {
            setupQueue.add(NbtUtils.readBlockPos(setupQueueTag, "pos" + i).orElse(null));
        }

    }
     */


    public void updateScreenData(String x1, String y1, String z1, String x2, String y2, String z2, int facing1Index, int facing2Index, int sizeIndex) {
        System.out.println("yerp");
        System.out.println(x1);
        X1 = x1;
        Y1 = y1;
        Z1 = z1;
        X2 = x2;
        Y2 = y2;
        Z2 = z2;
        Wormhole1Facing = facing1Index;
        Wormhole2Facing = facing2Index;
        WormholeSize = sizeIndex;
    }

    public String[] getEditBoxData() {
        return new String[]{X1,Y1,Z1,X2,Y2,Z2};
    }

    public int[] getButtonData() {
        return new int[]{Wormhole1Facing, Wormhole2Facing, WormholeSize};
    }


    public void beginShutdown() {
        if (!isRunning()) {
            return;
        }
        setRunning(false);
    }

    public void beginStartup(Level level, Player player) {
        if (isRunning()) {
            return;
        }
        level.playSound(player, this.getBlockPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS);
        setRunning(true);

        placeWormholes();
    }

    private void placeWormholes() {
        //placeLinkedPair();
        BlockPos partner1Pos = this.getBlockPos().offset(-5,0,0);
        BlockPos partner2Pos = this.getBlockPos().offset(-10,0,0);
        PacketDistributor.sendToServer(new WormholeData(partner1Pos, partner2Pos));
    }

    /*
    private void placeLinkedPair() {
        System.out.println("after beginStartup() but in linked pair: " + isRunning());
        //BlockState defaultState = BlockInit.LINKED_BLOCK.getDefaultState();
        //BlockState partner1 = defaultState.setValue(FACING,Direction.NORTH);
        //BlockState partner2 = defaultState.setValue(FACING,Direction.SOUTH);
        BlockPos partner1Pos = this.getBlockPos().offset(-5,0,0);
        BlockPos partner2Pos = this.getBlockPos().offset(-10,0,0);

        //level.setBlockAndUpdate(partner1Pos, partner1);
        //level.setBlockAndUpdate(partner2Pos, partner2);
        //masterList = new BlockPos[]{partner1Pos, partner2Pos};
        //lookUpTable.put(partner1Pos, partner2Pos);
        //lookUpTable.put(partner2Pos, partner1Pos);

        //setupQueue.add(partner1Pos);
        //setupDelay = 4;

        //this.getLevel().setBlockAndUpdate(partner1Pos, Blocks.OAK_PLANKS.defaultBlockState());
        //this.getLevel().setBlockAndUpdate(partner2Pos, Blocks.OAK_PLANKS.defaultBlockState());

        BlockState tempState = Blocks.OAK_PLANKS.defaultBlockState();
        placementPosQueue.add(new BlockPos[]{partner1Pos, partner2Pos});
        placementStateQueue.add(new BlockState[]{tempState, tempState});
        placementDelay = 20;
    }
    */
}
