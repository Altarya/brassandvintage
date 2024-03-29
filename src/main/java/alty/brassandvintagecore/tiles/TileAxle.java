package alty.brassandvintagecore.tiles;

import javax.annotation.Nullable;

import alty.brassandvintagecore.network.ISpinReceiver;
import alty.brassandvintagecore.network.NetworkPacketManager;
import alty.brassandvintagecore.network.SendSpinToClient;
import alty.brassandvintagecore.util.Properties;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.rotationutils.IAxisHandler;
import alty.brassandvintagecore.util.rotationutils.IAxle;
import alty.brassandvintagecore.util.rotationutils.IAxleHandler;
import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class TileAxle extends TileEntity implements ITickable, ISpinReceiver, IAxle{

	private double[] motionData = new double[4];
	private float angle;
	private float clientW;
	private double inertia;

	public TileAxle(){
		this(false);
	}

	public TileAxle(boolean massless){
		inertia = massless ? 0 : 0.25D;
	}

	public boolean isMassless(){
		return inertia < 0.001D;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock() != newState.getBlock();
	}
	

	
	@Override
	public void update(){
		if(world.isRemote){
			// it's 9 / PI instead of 180 / PI because 20 ticks/second
			angle += clientW * 9D / Math.PI;
		}
		for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity acc = Utils.getExistingTileEntity(world, pos.offset(facing.getOpposite()));
            if (acc instanceof IRotationAcceptor) {
                if (!world.isRemote) {
                    IRotationAcceptor dynamo = (IRotationAcceptor) acc;
                    
                    double used = Math.abs(motionData[1]);
            		axleHandler.addEnergy(-used, false, false);
                    
                    dynamo.inputRotation(used, facing.getOpposite());
                }
            }
        }

	}

	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setFloat("clientW", clientW);
		nbt.setFloat("angle", angle);
		return nbt;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		// motionData
		NBTTagCompound motionTags = new NBTTagCompound();
		for(int i = 0; i < 3; i++){
			if(motionData[i] != 0)
				motionTags.setDouble(i + "motion", motionData[i]);
		}
		nbt.setTag("motionData", motionTags);

		nbt.setFloat("clientW", clientW);
		nbt.setFloat("angle", angle);
		nbt.setDouble("inert", inertia);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);

		// motionData
		NBTTagCompound innerMot = nbt.getCompoundTag("motionData");
		for(int i = 0; i < 4; i++){
			this.motionData[i] = (innerMot.hasKey(i + "motion")) ? innerMot.getDouble(i + "motion") : 0;
		}

		clientW = nbt.getFloat("clientW");
		angle = nbt.getFloat("angle");
		inertia = nbt.hasKey("inert") ? nbt.getDouble("inert") : 0.25D;//Backward compatability
	}

	@Override
	public void receiveSpin(int identifier, float clientW, float angle){
		if(identifier == 0){
			this.clientW = clientW;
			this.angle = Math.abs(angle - this.angle) > 15F ? angle : this.angle;
		}
	}

	private final IAxleHandler axleHandler = new AxleHandler();

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
		if(capability == RotaryCapabilities.AXLE_HANDLER_CAPABILITY && facing != null && facing.getAxis() == world.getBlockState(pos).getValue(Properties.AXIS)){
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		if(capability == RotaryCapabilities.AXLE_HANDLER_CAPABILITY && facing != null && facing.getAxis() == world.getBlockState(pos).getValue(Properties.AXIS)){
			return (T) axleHandler;
		}
		return super.getCapability(capability, facing);
	}

	private class AxleHandler implements IAxleHandler{

		private byte key;
		private double rotRatio;

		@Override
		public double[] getMotionData(){
			return motionData;
		}

		@Override
		public void propogate(IAxisHandler masterIn, byte keyIn, double rotRatioIn, double lastRadius){
			if(rotRatioIn == 0){
				rotRatioIn = 1;
			}
			//If true, this has already been checked.
			if(key == keyIn){
				//If true, there is rotation conflict.
				if(rotRatio != rotRatioIn){
					masterIn.lock();
				}
				return;
			}

			if(masterIn.addToList(this)){
				return;
			}

			rotRatio = rotRatioIn;

			if(key == 0){
				resetAngle();
			}
			key = keyIn;

			EnumFacing endPos = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, world.getBlockState(pos).getValue(Properties.AXIS));
			EnumFacing endNeg = endPos.getOpposite();

			TileEntity posTE = world.getTileEntity(pos.offset(endPos));

			if(posTE != null){
				if(posTE.hasCapability(RotaryCapabilities.AXIS_HANDLER_CAPABILITY, endNeg)){
					posTE.getCapability(RotaryCapabilities.AXIS_HANDLER_CAPABILITY, endNeg).trigger(masterIn, key);
				}

				if(posTE.hasCapability(RotaryCapabilities.SLAVE_AXIS_HANDLER_CAPABILITY, endNeg)){
					masterIn.addAxisToList(posTE.getCapability(RotaryCapabilities.SLAVE_AXIS_HANDLER_CAPABILITY, endNeg), endNeg);
				}

				if(posTE.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, endNeg)){
					posTE.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, endNeg).propogate(masterIn, key, rotRatio, 0);
				}
			}

			TileEntity negTE = world.getTileEntity(pos.offset(endNeg));

			if(negTE != null){
				if(negTE.hasCapability(RotaryCapabilities.AXIS_HANDLER_CAPABILITY, endPos)){
					negTE.getCapability(RotaryCapabilities.AXIS_HANDLER_CAPABILITY, endPos).trigger(masterIn, key);
				}

				if(negTE.hasCapability(RotaryCapabilities.SLAVE_AXIS_HANDLER_CAPABILITY, endPos)){
					masterIn.addAxisToList(negTE.getCapability(RotaryCapabilities.SLAVE_AXIS_HANDLER_CAPABILITY, endPos), endPos);
				}

				if(negTE.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, endPos)){
					negTE.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, endPos).propogate(masterIn, key, rotRatio, 0);
				}
			}
		}

		@Override
		public double getMoInertia(){
			return inertia;
		}

		@Override
		public void resetAngle(){
			if(!world.isRemote){
				clientW = 0;
				angle = Math.signum(rotRatio) == -1 ? 22.5F : 0F;
				SendSpinToClient msg = new SendSpinToClient(0, clientW, angle, pos);
				NetworkPacketManager.network.sendToAllAround(msg, new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 512));
			}
		}

		@Override
		public float getAngle(){
			return angle;
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
				motionData[1] += energy * ((double) sign);
				if(Math.signum(motionData[1]) != sign){
					motionData[1] = 0;
				}
			}
			markDirty();
		}

		@Override
		public double getRotationRatio(){
			return rotRatio;
		}

		@Override
		public void markChanged(){
			markDirty();
		}

		@Override
		public boolean shouldManageAngle(){
			return true;
		}

		@Override
		public void setAngle(float angleIn){
			angle = angleIn;
		}

		@Override
		public float getClientW(){
			return clientW;
		}

		@Override
		public void syncAngle(){
			clientW = (float) motionData[0];
			SendSpinToClient msg = new SendSpinToClient(0, clientW, angle, pos);
			NetworkPacketManager.network.sendToAllAround(msg, new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 512));
		}
}

}
