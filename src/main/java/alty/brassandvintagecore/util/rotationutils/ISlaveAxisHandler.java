package alty.brassandvintagecore.util.rotationutils;

import java.util.HashSet;

import net.minecraft.util.EnumFacing;

public interface ISlaveAxisHandler {
	/**
	 * Yes, the side parameter is intentional and should be supplied even though this is a capability. 
	 * @param side The side of the axis triggered. 
	 */
	public void trigger(EnumFacing side);
	
	/**
	 * @return Any ISlaveAxisHandlers controlled by this axis. 
	 */
	public HashSet<ISlaveAxisHandler> getContainedAxes();
	
	/**
	 * This must be implemented to return true if this block no longer exists, or if it is unavailable for any other reason.
	 */
public boolean isInvalid();
}
