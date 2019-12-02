package alty.brassandvintagecore.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class BaVInventory implements IInventory {
	private static final String name = "TechgunsPlayerInventory";

	public static final int NUMSLOTS = 5;



	public NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(NUMSLOTS, ItemStack.EMPTY);// new
																											// ItemStack[NUMSLOTS];

	public boolean dirty = false;
	public EntityPlayer player;

	public BaVInventory(EntityPlayer player) {
		this.player = player;
	}

	
	public void saveNBTData(NBTTagCompound tags) {
		ItemStackHelper.saveAllItems(tags, this.inventory);
	}
	public void loadNBTData(NBTTagCompound tags) {
		this.inventory.clear();
		ItemStackHelper.loadAllItems(tags, this.inventory);
	}

	@Override
	public int getSizeInventory() {
		return NUMSLOTS;
	}

	@Override
	public ItemStack getStackInSlot(int slotid) {
		return slotid >= 0 && slotid < this.inventory.size() ? this.inventory.get(slotid) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		List<ItemStack> list = this.inventory;

		return list != null && !((ItemStack) list.get(index)).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int slotid, ItemStack itemstack) {
		this.inventory.set(slotid, itemstack);

		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		// System.out.println("Marked inv as dirty");
		this.dirty = true;
	}

	@Override
	public boolean isItemValidForSlot(int slotid, ItemStack itemstack) {
		return true;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("brassandvintagecore.extrainventory", new Object[0]);
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack1 : this.inventory) {
			if (!itemstack1.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		NonNullList<ItemStack> nonnulllist = this.inventory;
		if (nonnulllist != null && !((ItemStack) nonnulllist.get(index)).isEmpty()) {
			ItemStack itemstack = nonnulllist.get(index);
			nonnulllist.set(index, ItemStack.EMPTY);
			return itemstack;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.player.isDead) {
			return false;
		} else {
			return player.getDistanceSq(this.player) <= 64.0D;
		}
}

}
