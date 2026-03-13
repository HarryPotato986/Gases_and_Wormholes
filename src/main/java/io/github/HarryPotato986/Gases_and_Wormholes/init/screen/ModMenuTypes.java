package io.github.HarryPotato986.Gases_and_Wormholes.init.screen;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Gases_and_Wormholes.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AtmosphereExtractorMenu>> ATMOSPHERE_EXTRACTOR_MENU =
            registerMenuType("atmosphere_extractor_menu", AtmosphereExtractorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<WormholeGeneratorMenu>> WORMHOLE_GENERATOR_MENU =
            registerMenuType("wormhole_generator_menu", WormholeGeneratorMenu::new);


    private static <T extends AbstractContainerMenu>  DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
