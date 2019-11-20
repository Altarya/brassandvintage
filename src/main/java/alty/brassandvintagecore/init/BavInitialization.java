package alty.brassandvintagecore.init;

import alty.brassandvintagecore.proxy.CommonProxy;
import alty.brassandvintagecore.util.RegistryHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = BavInitialization.MODID, name = BavInitialization.NAME, version = BavInitialization.VERSION)
public class BavInitialization
{
    public static final String MODID = "brassandvintagecore";
    public static final String NAME = "Brass and Vintage - Machines";
    public static final String VERSION = "0.1";
    public static final String COMMON_PROXY_CLASS = "alty.brassandvintagecore.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "alty.brassandvintagecore.proxy.ClientProxy";

    
    @Mod.Instance
    public static BavInitialization instance;
    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    static { FluidRegistry.enableUniversalBucket(); }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        RegistryHandler.registerBaVCommon();
    }
    
    @EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInitEnd(event);
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event)
    {
        RegistryHandler.registerBaVMultiblocks();
    }
    
}
