package alty.brassandvintagecore.tiles;

import alty.brassandvintagecore.util.Properties;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.rotationutils.IAxisHandler;
import alty.brassandvintagecore.util.rotationutils.IAxleHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileDynamo extends TileMultiblock{

	private EnumFacing facing;
	public TileDynamo(){
		this(EnumFacing.NORTH);
	}


	public TileDynamo(EnumFacing facingIn){
		super();
		facing = facingIn;
	
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock() != newState.getBlock();
	}
	
	private double[] motionData = new double[4];
	private static final int CHARGE_CAPACITY = 100;

	private static int efficiency = -1;
	
	/**
	 * For client side rendering. 0 on server side. 
	 */
	public double angle = 0;
	/**
	 * For client side rendering. 0 on server side. 
	 */
	public double nextAngle = 0;

	@Override
	public void update(){
		if (offset == null) {
			// Not formed yet.  World may tick before configure is called 
			return;
		}
		this.ticks += 1;
		this.getMultiblock().tick(offset);
		if(world.isRemote){
			TileEntity neighbor = world.getTileEntity(pos.offset(facing));
			if(neighbor != null && neighbor.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, facing.getOpposite())){
				IAxleHandler handler = neighbor.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, facing.getOpposite());
				angle = handler.getAngle();
				nextAngle = handler.getNextAngle();
			}else{
				angle = 0;
				nextAngle = 0;
			}
			
			return;
		}

		if(efficiency < 0){//TODO
			//efficiency = ModConfig.getConfigInt(ModConfig.electPerJoule, false);
			efficiency = 100;
		}

		int operations = (int) Math.abs(motionData[1]);
		if(operations > 0){
			motionData[1] -= operations * Math.signum(motionData[1]);
			energyHandler.setEnergy(energyHandler.getEnergyStored() + operations * efficiency);
			markDirty();
		}

		TileEntity neighbor = world.getTileEntity(pos.offset(facing.getOpposite()));
		if(neighbor != null && neighbor.hasCapability(CapabilityEnergy.ENERGY, facing)){
			IEnergyStorage handler = neighbor.getCapability(CapabilityEnergy.ENERGY, facing);
			if(handler.canReceive()){
				energyHandler.setEnergy(energyHandler.getEnergyStored() - handler.receiveEnergy(efficiency, false));
				markDirty();
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		name = nbt.getString("name");
		rotation = Rotation.values()[nbt.getInteger("rotation")];
		offset = NBTUtil.getPosFromTag(nbt.getCompoundTag("offset"));
		replaced = NBTUtil.readBlockState(nbt.getCompoundTag("replaced"));
		energyHandler.setEnergy(nbt.getInteger("charge"));
		NBTTagCompound innerMot = nbt.getCompoundTag("motionData");
		for(int i = 0; i < 4; i++){
			motionData[i] = (innerMot.hasKey(i + "motion")) ? innerMot.getDouble(i + "motion") : 0;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setString("name", name);
		nbt.setInteger("rotation", rotation.ordinal());
		nbt.setTag("offset", NBTUtil.createPosTag(offset));
		nbt.setTag("replaced", NBTUtil.writeBlockState(new NBTTagCompound(), replaced));
		nbt.setInteger("charge", energyHandler.getEnergyStored());
		NBTTagCompound motionTags = new NBTTagCompound();
		for(int i = 0; i < 3; i++){
			if(motionData[i] != 0)
				motionTags.setDouble(i + "motion", motionData[i]);
		}
		nbt.setTag("motionData", motionTags);

		return nbt;
	}

	private final InternalGear gear = new InternalGear();

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing side){
		if(cap == RotaryCapabilities.AXLE_HANDLER_CAPABILITY && side != null && side == world.getBlockState(pos).getValue(Properties.FACING)){
			return true;
		}
		if(cap == CapabilityEnergy.ENERGY && (side == null || side == world.getBlockState(pos).getValue(Properties.FACING).getOpposite())){
			return true;
		}
		return super.hasCapability(cap, side);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing side){
		if(cap == RotaryCapabilities.AXLE_HANDLER_CAPABILITY && side != null && side == world.getBlockState(pos).getValue(Properties.FACING)){
			return (T) gear;
		}
		if(cap == CapabilityEnergy.ENERGY && (side == null || side == world.getBlockState(pos).getValue(Properties.FACING).getOpposite())){
			return (T) energyHandler;
		}
		return super.getCapability(cap, side);
	}

	private final DynamoEnergyStorage energyHandler = new DynamoEnergyStorage(CHARGE_CAPACITY, 0, CHARGE_CAPACITY, 0);

	private class DynamoEnergyStorage extends EnergyStorage{

		public DynamoEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy){
			super(capacity, maxReceive, maxExtract, energy);
		}

		public void setEnergy(int energyIn){
			this.energy = Math.max(0, Math.min(energyIn, capacity));
		}
	}

	private class InternalGear implements IAxleHandler{

		private byte updateKey;
		private double rotRatio;

		@Override
		public double[] getMotionData(){
			return motionData;
		}

		@Override
		public void propogate(IAxisHandler masterIn, byte key, double rotRatioIn, double lastRadius){
			//If true, this has already been checked.
			if(key == updateKey || masterIn.addToList(this)){
				return;
			}

			rotRatio = rotRatioIn == 0 ? 1 : rotRatioIn;
			if(world.getBlockState(pos).getValue(Properties.FACING).getAxisDirection() == AxisDirection.NEGATIVE){
				rotRatio *= -1D;
			}
			updateKey = key;
		}

		@Override
		public double getMoInertia(){
			return 140.625D;
		}

		@Override
		public double getRotationRatio(){
			return rotRatio;
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
		public void markChanged(){
			markDirty();
		}

		@Override
		public boolean shouldManageAngle(){
			return false;
		}	
	}
}
