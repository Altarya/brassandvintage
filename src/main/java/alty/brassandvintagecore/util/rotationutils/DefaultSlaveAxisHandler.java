package alty.brassandvintagecore.util.rotationutils;

import java.util.HashSet;

import net.minecraft.util.EnumFacing;

public class DefaultSlaveAxisHandler implements ISlaveAxisHandler{

	@Override
	public void trigger(EnumFacing side){
		
	}

	@Override
	public HashSet<ISlaveAxisHandler> getContainedAxes(){
		return null;
	}

	@Override
	public boolean isInvalid(){
		return true;
	}
}
