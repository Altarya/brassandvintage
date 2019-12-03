package alty.brassandvintagecore.util;
import org.apache.logging.log4j.Level;

import alty.brassandvintagecore.util.DefaultStorageHelper.DefaultStorage;
import alty.brassandvintagecore.util.rotationutils.DefaultAxisHandler;
import alty.brassandvintagecore.util.rotationutils.DefaultAxleHandler;
import alty.brassandvintagecore.util.rotationutils.DefaultCogHandler;
import alty.brassandvintagecore.util.rotationutils.DefaultSlaveAxisHandler;
import alty.brassandvintagecore.util.rotationutils.IAxisHandler;
import alty.brassandvintagecore.util.rotationutils.IAxleHandler;
import alty.brassandvintagecore.util.rotationutils.ICogHandler;
import alty.brassandvintagecore.util.rotationutils.ISlaveAxisHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class RotaryCapabilities {

	@CapabilityInject(IAxleHandler.class)
	public static Capability<IAxleHandler> AXLE_HANDLER_CAPABILITY = null;
	
	@CapabilityInject(ICogHandler.class)
	public static Capability<ICogHandler> COG_HANDLER_CAPABILITY = null;
	
	
	@CapabilityInject(IAxisHandler.class)
	public static Capability<IAxisHandler> AXIS_HANDLER_CAPABILITY = null;
	
	@CapabilityInject(ISlaveAxisHandler.class)
	public static Capability<ISlaveAxisHandler> SLAVE_AXIS_HANDLER_CAPABILITY = null;

	public static void register(){
		CapabilityManager.INSTANCE.register(IAxleHandler.class, new DefaultStorage<>(), DefaultAxleHandler.class);
		CapabilityManager.INSTANCE.register(ICogHandler.class, new DefaultStorage<>(), DefaultCogHandler.class);
		CapabilityManager.INSTANCE.register(IAxisHandler.class, new DefaultStorage<>(), DefaultAxisHandler.class);
		CapabilityManager.INSTANCE.register(ISlaveAxisHandler.class, new DefaultStorage<>(), DefaultSlaveAxisHandler.class);
		SuperLogger.logger.log(Level.INFO, "BaV Rotary Capabilities Registered");
	}
}
