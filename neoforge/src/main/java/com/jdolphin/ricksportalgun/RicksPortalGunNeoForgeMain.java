package com.jdolphin.ricksportalgun;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(PGConstants.MODID)
public class RicksPortalGunNeoForgeMain {

    public RicksPortalGunNeoForgeMain(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        PGConstants.LOGGER.info("Hello NeoForge world!");
        RicksPortalGunCommonMain.init();

    }
}