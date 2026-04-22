package io.github.HarryPotato986.Gases_and_Wormholes.util;

/*
import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryDirectionWrapper {
    public Map<Direction, LazyOptional<DirectionWrappedHandler>> directionsMap;

    public InventoryDirectionWrapper(IItemHandlerModifiable handler, InventoryDirectionEntry... entries) {
        directionsMap = new HashMap<>();
        for (var x : entries) {
            directionsMap.put(x.direction,
                    LazyOptional.of(() -> new DirectionWrappedHandler(handler, (i) -> Objects.equals(i, x.slotIndex), (i, s) -> x.canInsert)));
        }
    }
}

 */