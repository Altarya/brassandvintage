package alty.brassandvintagecore.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.tuple.Pair;

import alty.brassandvintagecore.proxy.CommonProxy;
import alty.brassandvintagecore.util.BaVConfigHandler;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.rotationutils.IAxisHandler;
import alty.brassandvintagecore.util.rotationutils.IAxleHandler;
import alty.brassandvintagecore.util.rotationutils.ISlaveAxisHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class TileMasterAxis extends TileEntity implements ITickable{

	private ArrayList<IAxleHandler> rotaryMembers = new ArrayList<IAxleHandler>();

	private boolean locked = false;
	private double sumEnergy = 0;
	private int ticksExisted = 0;
	public EnumFacing facing;
	private byte key;

	
	public void disconnect(){
		for(IAxleHandler axle : rotaryMembers){
			//For 0-mass gears.
			axle.getMotionData()[0] = 0;
			axle.syncAngle();
		}
		CommonProxy.masterKey++;
	}
	
	public TileMasterAxis(){
		this(EnumFacing.NORTH);
	}

	public TileMasterAxis(EnumFacing facingIn){
		super();
		facing = facingIn;
	}

	private void runCalc(){
		double sumIRot = 0;
		sumEnergy = 0;
		// IRL you wouldn't say a gear spinning a different direction has
		// negative energy, but it makes the code easier.

		for(IAxleHandler gear : rotaryMembers){
			sumIRot += gear.getMoInertia() * Math.pow(gear.getRotationRatio(), 2);
		}

		if(sumIRot == 0 || sumIRot != sumIRot){
			return;
		}

		sumEnergy = runLoss(rotaryMembers, 1.001D);
		if(sumEnergy < 1 && sumEnergy > -1){
			sumEnergy = 0;
		}

		for(IAxleHandler gear : rotaryMembers){
			// set w
			double newSpeed = Math.signum(sumEnergy * gear.getRotationRatio()) * Math.sqrt(Math.abs(sumEnergy) * 2D * Math.pow(gear.getRotationRatio(), 2) / sumIRot);
			gear.getMotionData()[0] = newSpeed;
			// set energy
			double newEnergy = Math.signum(newSpeed) * Math.pow(newSpeed, 2) * gear.getMoInertia() / 2D;
			gear.getMotionData()[1] = newEnergy;
			// set power
			gear.getMotionData()[2] = (newEnergy - gear.getMotionData()[3]) * 20D;
			// set lastE
			gear.getMotionData()[3] = newEnergy;

			gear.markChanged();
		}
	}

	/**
	 * base should always be equal or greater than one. 1 means no loss. 
	 */
	private static double runLoss(ArrayList<IAxleHandler> gears, double base){
		double sumEnergy = 0;

		for(IAxleHandler gear : gears){
			sumEnergy += Math.signum(gear.getRotationRatio()) * gear.getMotionData()[1] * Math.pow(base, -Math.abs(gear.getMotionData()[0]));
		}

		return sumEnergy;
	}
	//TODO
	//private static final float CLIENT_SPEED_MARGIN = (float)  BaVConfigHandler.speedPrecision.getDouble();

	private static final float CLIENT_SPEED_MARGIN = 1F;
	
	private void runAngleCalc(){
		boolean syncSpin = false;
		boolean work = false;
		for(IAxleHandler axle : rotaryMembers){
			if(axle.shouldManageAngle()){
				syncSpin = Math.abs(axle.getMotionData()[0] - axle.getClientW()) >= CLIENT_SPEED_MARGIN * axle.getRotationRatio();
				work = true;
				break;
			}
		}
		if(!work){
			return;
		}

		for(IAxleHandler axle : rotaryMembers){
			if(axle.shouldManageAngle()){
				float axleSpeed = ((float) axle.getMotionData()[0]);
				axle.setAngle(axle.getAngle() + (axleSpeed * 9F / (float) Math.PI));
				if(syncSpin){
					axle.syncAngle();
				}
			}
		}
	}

	public NBTTagCompound writeToNBTMaster(NBTTagCompound nbt){
		nbt.setInteger("facing", this.facing.getIndex());
		return nbt;
	}

	public void readFromNBTMaster(NBTTagCompound nbt){
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
	}

	private int lastKey = 0;
	private boolean forceUpdate;

	//TODO
	//private static final int UPDATE_TIME = ModConfig.gearResetTime.getInt();
	private static final int UPDATE_TIME = 5;

	public void updateMaster(){
		if(world.isRemote){
			return;
		}

		ticksExisted++;

		if(ticksExisted % UPDATE_TIME == 20 || forceUpdate){
			handler.requestUpdate();
		}

		forceUpdate = CommonProxy.masterKey != lastKey;

		lastKey = CommonProxy.masterKey;

		if(!locked && !rotaryMembers.isEmpty()){
			runCalc();
			runAngleCalc();
			triggerSlaves();
		}
	}

	private void triggerSlaves(){
		HashSet<Pair<ISlaveAxisHandler, EnumFacing>> toRemove = new HashSet<Pair<ISlaveAxisHandler, EnumFacing>>();
		for(Pair<ISlaveAxisHandler, EnumFacing> slave : slaves){
			if(slave.getLeft().isInvalid()){
				toRemove.add(slave);
				continue;
			}
			slave.getLeft().trigger(slave.getRight());
		}
		slaves.removeAll(toRemove);
	}

	private final HashSet<Pair<ISlaveAxisHandler, EnumFacing>> slaves = new HashSet<Pair<ISlaveAxisHandler, EnumFacing>>();

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing side){
		if(cap == RotaryCapabilities.AXIS_HANDLER_CAPABILITY && (side == null || side == facing)){
			return true;
		}
		return super.hasCapability(cap, side);
	}

	private final IAxisHandler handler = new AxisHandler();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing side){
		if(cap == RotaryCapabilities.AXIS_HANDLER_CAPABILITY && (side == null || side == facing)){
			return (T) handler;
		}
		return super.getCapability(cap, side);
	}

	private class AxisHandler implements IAxisHandler{

		@Override
		public void trigger(IAxisHandler masterIn, byte keyIn){
			if(keyIn != key){
				locked = true;
			}
		}

		private final Random RAND = new Random();

		@Override
		public void requestUpdate(){//TODO
			/*if(world.isRemote || ModConfig.disableSlaves.getBoolean()){
				return;
			}*/
			
			if(world.isRemote){
				return;
			}
			
			ArrayList<IAxleHandler> memberCopy = new ArrayList<IAxleHandler>(rotaryMembers);
			rotaryMembers.clear();
			locked = false;
			TileEntity te = world.getTileEntity(pos.offset(facing));
			if(te != null && te.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, facing.getOpposite())){
				byte keyNew;
				do {
					keyNew = (byte) (RAND.nextInt(100) + 1);
				}while(key == keyNew);
				key = keyNew;

				te.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, facing.getOpposite()).propogate(this, key, 0, 0);
			}

			if(!memberCopy.containsAll(rotaryMembers)){
				for(IAxleHandler axle : rotaryMembers){
					axle.resetAngle();
				}
			}

			memberCopy.removeAll(rotaryMembers);
			for(IAxleHandler axle : memberCopy){
				//For 0-mass gears.
				axle.getMotionData()[0] = 0;
				axle.syncAngle();
			}
		}

		@Override
		public void lock(){
			locked = true;
			for(IAxleHandler gear : rotaryMembers){
				gear.getMotionData()[0] = 0;
				gear.getMotionData()[1] = 0;
				gear.getMotionData()[2] = 0;
				gear.getMotionData()[3] = 0;
			}
		}

		@Override
		public boolean isLocked(){
			return locked;
		}

		@Override
		public boolean addToList(IAxleHandler handler){
			if(!locked){
				rotaryMembers.add(handler);
				return false;
			}else{
				return true;
			}
		}

		@Override
		public void addAxisToList(ISlaveAxisHandler handler, EnumFacing side){
			slaves.add(Pair.of(handler, side));
		}

		@Override
		public double getTotalEnergy(){
			return sumEnergy;
		}
	}

	@Override
	public void update() {
	}
}
