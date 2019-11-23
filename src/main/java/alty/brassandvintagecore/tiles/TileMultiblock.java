package alty.brassandvintagecore.tiles;

import javax.annotation.Nonnull;

import alty.brassandvintagecore.multiblocks.common.BaVMultiblockRegister;
import alty.brassandvintagecore.util.BaVMultiblockHandler.MultiblockInstance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class TileMultiblock extends SyncdTileEntity implements ITickable {
	public static TileMultiblock get(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		return te instanceof TileMultiblock ? (TileMultiblock) te : null;
	}
	
	private IBlockState replaced;
	private BlockPos offset;
	private Rotation rotation;
	private String name;
	private CraftingMachineMode craftMode = CraftingMachineMode.STOPPED;
	private long ticks;
	private MultiblockInstance mb;
	
	//Crafting
	private int craftProgress = 0;
	private ItemStack craftItem = ItemStack.EMPTY;
	private ItemStackHandler container = new ItemStackHandler(0) {
        @Override
        protected void onContentsChanged(int slot) {
        	markDirty();
        }

		@Override
		public int getSlotLimit(int slot) {
			if (isLoaded()) {
				return Math.min(super.getSlotLimit(slot), getMultiblock().getSlotLimit(offset, slot));
			}
			return 0;
		}
    };
    
    private EnergyStorage energy = new EnergyStorage(1000) {
    	@Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
    		int val = super.receiveEnergy(maxReceive, simulate);
    		if (!simulate && val != 0 && isLoaded()) {
    			markDirty();
    		}
    		return val;
    	}
    	
    	@Override
        public int extractEnergy(int maxExtract, boolean simulate) {
    		int val = super.extractEnergy(maxExtract, simulate);
    		if (!simulate && val != 0 && isLoaded()) {
    			markDirty();
    		}
    		return val;
    	}
    };
    
    @Override
	public boolean isLoaded() {
    	return super.isLoaded() && this.name != null;
    }
	
	public void configure(String name, Rotation rot, BlockPos offset, IBlockState replaced) {
		this.name = name;
		this.rotation = rot;
		this.offset = offset;
		this.replaced = replaced;
		
		container.setSize(this.getMultiblock().getInvSize(offset));
		
		markDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		
		if (name == null) {
			// Probably in some weird block break path
			return nbt;
		}

		nbt.setString("name", name);
		nbt.setInteger("rotation", rotation.ordinal());
		nbt.setTag("offset", NBTUtil.createPosTag(offset));
		nbt.setTag("replaced", NBTUtil.writeBlockState(new NBTTagCompound(), replaced));
		
		nbt.setTag("inventory", container.serializeNBT());
		nbt.setTag("craftItem", craftItem.serializeNBT());
		nbt.setInteger("craftProgress", craftProgress);
		nbt.setInteger("craftMode", craftMode.ordinal());
		
		nbt.setInteger("energy", energy.getEnergyStored());
		
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		name = nbt.getString("name");
		rotation = Rotation.values()[nbt.getInteger("rotation")];
		offset = NBTUtil.getPosFromTag(nbt.getCompoundTag("offset"));
		replaced = NBTUtil.readBlockState(nbt.getCompoundTag("replaced"));
		
		container.deserializeNBT(nbt.getCompoundTag("inventory"));
		craftItem = new ItemStack(nbt.getCompoundTag("craftItem"));
		craftProgress = nbt.getInteger("craftProgress");
		
		craftMode = CraftingMachineMode.STOPPED;
		if (nbt.hasKey("craftMode")) {
			craftMode = CraftingMachineMode.values()[nbt.getInteger("craftMode")];
		}
		
		// Empty and then refill energy storage
		energy.extractEnergy(energy.getEnergyStored(), false);
		energy.receiveEnergy(nbt.getInteger("energy"), false);
	}
	/*
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return Math.pow(ImmersiveRailroading.proxy.getRenderDistance()*16, 2);
	}*/

	@Override
	public void update() {
		if (offset == null) {
			// Not formed yet.  World may tick before configure is called 
			return;
		}
		this.ticks += 1;
		this.getMultiblock().tick(offset);
	}

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
    	return INFINITE_EXTENT_AABB;
    }
	
	public BlockPos getOrigin() {
		return pos.subtract(offset.rotate(rotation));
	}
	
	public MultiblockInstance getMultiblock() {
		if (this.mb == null && this.isLoaded()) {
			this.mb = BaVMultiblockRegister.get(name).instance(world, getOrigin(), rotation);
		}
		return this.mb;
	}
	
	public String getName() {
		return name;
	}
	
	public long getRenderTicks() {
		return this.ticks;
	}
	
	public ItemStackHandler getContainer() {
		return this.container;
	}

	/*
	 * Block Functions to pass on to the multiblock
	 */
	public void breakBlock() {
		if (getMultiblock() != null) {
			getMultiblock().onBreak();
		}
	}

	public boolean onBlockActivated(EntityPlayer player, EnumHand hand) {
		return getMultiblock().onBlockActivated(player, hand, offset);
	}
	
	/*
	 * Event Handlers
	 */
	
	public void onBreak() {
		for (int slot = 0; slot < container.getSlots(); slot ++) {
			ItemStack item = container.extractItem(slot, Integer.MAX_VALUE, false);
			if (!item.isEmpty()) {
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), item));
			}
		}
		world.removeTileEntity(pos);
		world.setBlockState(pos, replaced, 3);
	}

	public boolean isRender() {
		return getMultiblock().isRender(offset);
	}

	public double getRotation() {
		return 180 - rotation.rotate(EnumFacing.EAST).getHorizontalAngle();
	}
	
	/*
	 * Crafting
	 */
	public int getCraftProgress() {
		return craftProgress;
	}
	
	public void setCraftProgress(int progress) {
		if (craftProgress != progress) {
			craftProgress = progress;
			this.markDirty();
		}
	}
	
	public CraftingMachineMode getCraftMode() {
		return craftMode;
	}
	/*
	public void setCraftMode(CraftingMachineMode mode) {
		if (!world.isRemote) {
			if (craftMode != mode) {
				craftMode = mode;
				this.markDirty();
			}
		} else {
			net.sendToServer(new MultiblockSelectCraftPacket(getPos(), craftItem, mode));
		}
	}
	
	public ItemStack getCraftItem() {
		return craftItem;
	}

	public void setCraftItem(ItemStack selected) {
		if (!world.isRemote) {
			if (craftItem == null || selected == null || !ItemStack.areItemStacksEqualUsingNBTShareTag(selected, craftItem)) {
				this.craftItem = selected.copy();
				this.craftProgress = 0;
				this.markDirty();
			}
		} else {
			net.sendToServer(new MultiblockSelectCraftPacket(getPos(), selected, craftMode));
		}
	}*/
	
	/*
	 * Capabilities
	 */
	
	@Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (this.isLoaded()) {
	        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
	            return this.getMultiblock().getInvSize(offset) != 0;
	        }
	        if (capability == CapabilityEnergy.ENERGY) {
	        	return this.getMultiblock().canRecievePower(offset);
	        }
		}
        return super.hasCapability(capability, facing);
    }

	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (this.isLoaded()) {
	        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
	        	if (this.getMultiblock().getInvSize(offset) != 0) {
	        		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new IItemHandlerModifiable()  {
						@Override
						public int getSlots() {
							return container.getSlots();
						}
						@Override
						public ItemStack getStackInSlot(int slot) {
							return container.getStackInSlot(slot);
						}
						@Override
	        	        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
	        	        	if (getMultiblock().canInsertItem(offset, slot, stack)) {
	        	        		return container.insertItem(slot, stack, simulate);
	        	        	}
	        	        	return stack;
	        	        }
	        	        @Override
	        	        public ItemStack extractItem(int slot, int amount, boolean simulate) {
	        	        	if (getMultiblock().isOutputSlot(offset, slot)) {
	        	        		return container.extractItem(slot, amount, simulate);
	        	        	}
	        	        	return ItemStack.EMPTY;
	        	        }
						@Override
						public int getSlotLimit(int slot) {
							return container.getSlotLimit(slot);
						}
						
						@Override
						public void setStackInSlot(int slot, ItemStack stack) {
							container.setStackInSlot(slot, stack);
						}
	        		});
	        	}
	        }
	        if (capability == CapabilityEnergy.ENERGY) {
	        	if (this.getMultiblock().canRecievePower(offset)) {
	        		return CapabilityEnergy.ENERGY.cast(this.energy);
	        	}
	        }
		}
        return super.getCapability(capability, facing);
}
}
