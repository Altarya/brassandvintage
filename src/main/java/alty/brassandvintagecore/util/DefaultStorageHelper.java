package alty.brassandvintagecore.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * All credits for this class file go to aidancbrady. This code came from his
 * mod Mekanism, and copying it is allowed under Mekanism's license at the time
 * of writing this.
 */
public class DefaultStorageHelper{

	public static class DefaultStorage<T> implements IStorage<T>{
		@SuppressWarnings("rawtypes")
		@Override
		public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side){
			if(instance instanceof INBTSerializable)
				return ((INBTSerializable) instance).serializeNBT();
			return new NBTTagCompound();
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt){
			if(instance instanceof INBTSerializable){
				Class<? extends NBTBase> nbtClass = ((INBTSerializable) instance).serializeNBT().getClass();

				if(nbtClass.isInstance(nbt)){
					((INBTSerializable) instance).deserializeNBT(nbtClass.cast(nbt));
				}
			}
		}
	}

	public static class NullStorage<T> implements IStorage<T>{
		@Override
		public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side){
			return new NBTTagCompound();
		}

		@Override
		public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt){
		}
	}
}