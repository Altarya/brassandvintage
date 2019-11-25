package alty.brassandvintagecore.init;

import java.io.File;

import alty.brassandvintagecore.materials.BaVCreativeTab;
import alty.brassandvintagecore.proxy.CommonProxy;
import alty.brassandvintagecore.util.BaVConfigHandler;
import alty.brassandvintagecore.util.RegistryHandler;
import alty.brassandvintagecore.util.SuperLogger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = BaVInitialization.MODID, name = BaVInitialization.NAME, version = BaVInitialization.VERSION)
public class BaVInitialization
{
    public static final String MODID = "brassandvintagecore";
    public static final String NAME = "Brass and Vintage - Machines";
    public static final String VERSION = "0.1";
    public static final String COMMON_PROXY_CLASS = "alty.brassandvintagecore.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "alty.brassandvintagecore.proxy.ClientProxy";
    
    public static final CreativeTabs BAV_TAB = new BaVCreativeTab();

    
    @Mod.Instance
    public static BaVInitialization instance;
    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    static { FluidRegistry.enableUniversalBucket(); }
	public static final SimpleNetworkWrapper net = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	SuperLogger.logger = event.getModLog();
        RegistryHandler.registerBaVCommon();
    }
    
    @EventHandler
    public void postInit(FMLPreInitializationEvent event)
    {
        proxy.initConfig(event.getSuggestedConfigurationFile());
    	BaVConfigHandler.initFuels();
    }
    
    @EventHandler
	public void postInit(FMLPostInitializationEvent event){
    	RegistryHandler.registerBaVMultiblocks();
		proxy.postInitEnd(event);
		BaVConfigHandler.wrapConfigToMTS();
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event)
    {
        
    }
    
}
