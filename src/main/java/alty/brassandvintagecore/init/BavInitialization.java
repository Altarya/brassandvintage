package alty.brassandvintagecore.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.SidedProxy;
import alty.brassandvintagecore.proxy.CommonProxy;

@Mod(modid = BavInitialization.MODID, name = BavInitialization.NAME, version = BavInitialization.VERSION)
public class BavInitialization
{
    public static final String MODID = "brassandvintagecore";
    public static final String NAME = "Brass and Vintage - Machines";
    public static final String VERSION = "0.1";
    public static final String COMMON_PROXY_CLASS = "alty.brassandvintagecore.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "alty.brassandvintagecore.proxy.ClientProxy";

    private static Logger logger;
    
    @Mod.Instance
    public static BavInitialization instance;
    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
       
    }
}