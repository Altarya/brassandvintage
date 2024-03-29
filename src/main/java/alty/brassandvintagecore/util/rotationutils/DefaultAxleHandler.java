package alty.brassandvintagecore.util.rotationutils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultAxleHandler implements IAxleHandler {
	public static boolean canConnectThrough(World world, BlockPos pos, EnumFacing fromDir, EnumFacing toDir){
		IBlockState state = world.getBlockState(pos);
		return !state.getBlock().isNormalCube(state, world, pos);
	}
	
	private double[] motionData = new double[4];
	private double physData = 0;

	@Override
	public double[] getMotionData(){
		return motionData;
	}

	@Override
	public double getMoInertia(){
		return physData;
	}

	@Override
	public void addEnergy(double energy, boolean allowInvert, boolean absolute){
		if(allowInvert && absolute){
			motionData[1] += energy;
		}else if(allowInvert){
			motionData[1] += energy * Math.signum(motionData[1]);
		}else if(absolute){
			int sign = (int) Math.signum(motionData[1]);
			motionData[1] += energy;
			if(sign != 0 && Math.signum(motionData[1]) != sign){
				motionData[1] = 0;
			}
		}else{
			int sign = (int) Math.signum(motionData[1]);
			motionData[1] += energy * Math.signum(motionData[1]);
			if(Math.signum(motionData[1]) != sign){
				motionData[1] = 0;
			}
		}
	}

	@Override
	public void propogate(IAxisHandler masterIn, byte key, double rotationRatioIn, double lastRadius){
		
	}

	@Override
	public double getRotationRatio(){
		return 0;
	}
	
	@Override
	public void markChanged(){
		
	}

	@Override
	public boolean shouldManageAngle(){
		return false;
	}
}
