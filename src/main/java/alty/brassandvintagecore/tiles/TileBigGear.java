package alty.brassandvintagecore.tiles;

import javax.annotation.Nullable;

import alty.brassandvintagecore.items.GearFactory;
import alty.brassandvintagecore.network.ISpinReceiver;
import alty.brassandvintagecore.network.IStringReceiver;
import alty.brassandvintagecore.network.NetworkPacketManager;
import alty.brassandvintagecore.network.SendSpinToClient;
import alty.brassandvintagecore.network.SendStringToClient;
import alty.brassandvintagecore.util.Properties;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.SuperMath;
import alty.brassandvintagecore.util.rotationutils.DefaultAxleHandler;
import alty.brassandvintagecore.util.rotationutils.GearTypes;
import alty.brassandvintagecore.util.rotationutils.IAxisHandler;
import alty.brassandvintagecore.util.rotationutils.IAxleHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileBigGear extends TileEntity implements ISpinReceiver, ITickable, IStringReceiver{
	
	private GearTypes type;
	private double[] motionData = new double[4];
	private double inertia = 0;
	private boolean borken = false;
	private float[] angleW = new float[2];

	public void initSetup(GearTypes typ){
		type = typ;

		if(!world.isRemote){
			SendStringToClient msg = new SendStringToClient("memb", type == null ? "" : type.name(), pos);
			NetworkPacketManager.network.sendToAllAround(msg, new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 512));
		}

		inertia = type == null ? 0 : SuperMath.betterRound(type.getDensity() * 4.5D * 1.125D, 2);
		//1.125 because r*r/2 so 1.5*1.5/2
	}

	public GearTypes getMember(){
		return type == null ? GearTypes.BRASS : type;
	}

	private static final AxisAlignedBB RENDER_BOX = new AxisAlignedBB(-1, -1, -1, 2, 2, 2);

	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return RENDER_BOX.offset(pos);
	}

	public void breakGroup(EnumFacing side, boolean drop){
		if(borken){
			return;
		}
		borken = true;
		for(int i = -1; i < 2; ++i){
			for(int j = -1; j < 2; ++j){
				world.setBlockToAir(pos.offset(side.getAxis() == Axis.X ? EnumFacing.UP : EnumFacing.EAST, i).offset(side.getAxis() == Axis.Z ? EnumFacing.UP : EnumFacing.NORTH, j));
			}
		}
		if(drop){
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(GearFactory.BASIC_GEARS.get(type), 1)));
		}
	}

	@Override
	public void update(){
		if(world.isRemote){
			angleW[0] += angleW[1] * 9D / Math.PI;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);

		// motionData
		NBTTagCompound innerMot = nbt.getCompoundTag("motionData");
		for(int j = 0; j < 4; j++){
			motionData[j] = (innerMot.hasKey(j + "motion")) ? innerMot.getDouble(j + "motion") : 0;
		}
		// member
		type = nbt.hasKey("memb") ? GearTypes.valueOf(nbt.getString("memb")) : null;
		inertia = type == null ? 0 : SuperMath.betterRound(type.getDensity() * 4.5D * 1.125D, 2);
		//1.125 because r*r/2 so 1.5*1.5/2

		angleW[0] = nbt.getFloat("angle");
		angleW[1] = nbt.getFloat("clientW");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		// motionData
		NBTTagCompound motionTags = new NBTTagCompound();
		for(int j = 0; j < 3; j++){
			if(motionData[j] != 0)
				motionTags.setDouble(j + "motion", motionData[j]);
		}
		nbt.setTag("motionData", motionTags);

		// member
		if(type != null){
			nbt.setString("memb", type.name());
		}

		nbt.setBoolean("new", true);
		nbt.setFloat("angle", angleW[0]);
		nbt.setFloat("clientW", angleW[1]);
		return nbt;
	}

	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt = super.getUpdateTag();
		if(type != null){
			nbt.setString("memb", type.name());
		}
		nbt.setBoolean("new", true);
		nbt.setFloat("angle", angleW[0]);
		nbt.setFloat("clientW", angleW[1]);
		return nbt;
	}

	@Override
	public void receiveSpin(int identifier, float clientW, float angle){
		if(identifier == 0){
			angleW[0] = Math.abs(angle - angleW[0]) > 5F ? angle : angleW[0];
			angleW[1] = clientW;
		}
	}

	@Override
	public void receiveString(String context, String message, EntityPlayerMP player){
		if(context.equals("memb")){
			type = message.equals("") ? null : GearTypes.valueOf(message);
		}
	}

	private final AxleHandler handlerMain = new AxleHandler();

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
		if(capability == RotaryCapabilities.AXLE_HANDLER_CAPABILITY && facing == world.getBlockState(pos).getValue(Properties.FACING)){
			return type != null;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		if(capability == RotaryCapabilities.AXLE_HANDLER_CAPABILITY && facing == world.getBlockState(pos).getValue(Properties.FACING)){
			return (T) handlerMain;
		}
		return super.getCapability(capability, facing);
	}

	private class AxleHandler implements IAxleHandler{

		private byte updateKey;
		private double rotRatio;

		@Override
		public double[] getMotionData(){
			return motionData;
		}

		@Override
		public void propogate(IAxisHandler masterIn, byte key, double rotRatioIn, double lastRadius){
			if(type == null){
				return;
			}

			EnumFacing sid = world.getBlockState(pos).getValue(Properties.FACING);

			if(lastRadius != 0){
				rotRatioIn *= -lastRadius / 1.5D;
			}else if(rotRatioIn == 0){
				rotRatioIn = 1D;
			}else if(sid.getAxisDirection() == AxisDirection.POSITIVE){
				rotRatioIn *= -1D;
			}

			//If true, this has already been checked.
			if(key == updateKey){
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

			if(updateKey == 0){
				resetAngle();
			}
			updateKey = key;

			TileEntity connectTE = world.getTileEntity(pos.offset(sid));
			if(connectTE != null){
				if(connectTE.hasCapability(RotaryCapabilities.AXIS_HANDLER_CAPABILITY, sid.getOpposite())){
					connectTE.getCapability(RotaryCapabilities.AXIS_HANDLER_CAPABILITY, sid.getOpposite()).trigger(masterIn, key);
				}
				if(connectTE.hasCapability(RotaryCapabilities.SLAVE_AXIS_HANDLER_CAPABILITY, sid.getOpposite())){
					masterIn.addAxisToList(connectTE.getCapability(RotaryCapabilities.SLAVE_AXIS_HANDLER_CAPABILITY, sid.getOpposite()), sid.getOpposite());
				}

				if(connectTE.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, sid.getOpposite())){
					connectTE.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, sid.getOpposite()).propogate(masterIn, key, sid.getAxisDirection() == AxisDirection.POSITIVE ? -rotRatio : rotRatio, 0);
				}
			}

			for(EnumFacing sideN : EnumFacing.values()){
				if(sideN != sid && sideN != sid.getOpposite()){
					// Adjacent gears
					TileEntity adjTE = world.getTileEntity(pos.offset(sideN, 2));
					if(adjTE != null && adjTE.hasCapability(RotaryCapabilities.COG_HANDLER_CAPABILITY, sid)){
						adjTE.getCapability(RotaryCapabilities.COG_HANDLER_CAPABILITY, sid).connect(masterIn, key, rotRatio, 1.5D);
					}

					// Diagonal gears
					TileEntity diagTE = world.getTileEntity(pos.offset(sideN, 2).offset(sid));
					if(diagTE != null && diagTE.hasCapability(RotaryCapabilities.COG_HANDLER_CAPABILITY, sideN.getOpposite()) && DefaultAxleHandler.canConnectThrough(world, pos.offset(sideN, 2), sideN.getOpposite(), sid) && diagTE != null){
						diagTE.getCapability(RotaryCapabilities.COG_HANDLER_CAPABILITY, sideN.getOpposite()).connect(masterIn, key, rotRatio, 1.5D);
					}
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
				angleW[1] = 0;
				angleW[0] = Math.signum(rotRatio) == -1 ? 7.5F : 0F;
				SendSpinToClient msg = new SendSpinToClient(0, angleW[1], angleW[0], pos);
				NetworkPacketManager.network.sendToAllAround(msg, new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 512));
			}
		}

		@Override
		public float getAngle(){
			return angleW[0];
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public float getNextAngle(){
			return angleW[0] + (angleW[1] * 9F / (float) Math.PI);
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
			angleW[0] = angleIn;
		}

		@Override
		public float getClientW(){
			return angleW[1];
		}

		@Override
		public void syncAngle(){
			angleW[1] = (float) motionData[0];
			SendSpinToClient msg = new SendSpinToClient(0, angleW[1], angleW[0], pos);
			NetworkPacketManager.network.sendToAllAround(msg, new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 512));
		}
}

}
