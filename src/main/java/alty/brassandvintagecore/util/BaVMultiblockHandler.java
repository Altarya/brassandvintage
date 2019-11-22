package alty.brassandvintagecore.util;

import alty.brassandvintagecore.multiblocks.common.MultiblockComponent;
import alty.brassandvintagecore.util.MiscUtils.Chatter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This is a modified version of 1.12 Immersive Railroading code
 * 
 * A special thanks to malte0811 and SkySom, who were patient enough to explain things to me(Alty), and also to Cam72Cam for letting me use his logic
 * 
 * https://github.com/TeamOpenIndustry/ImmersiveRailroading/blob/422afddda54244022c704c83c9fb2072ac8d87a9/src/main/java/cam72cam/immersiverailroading/multiblock/Multiblock.java
 * 
 * */

@Slf4j
public abstract class BaVMultiblockHandler {
	//VERY IMPORTANT: THIS ARRAY IS **Z Y X**, UNLIKE IE'S WHICH IS **Y Z X**!!!!!!!
	private final MultiblockComponent[][][] components;
	private final String name;
	protected final List<BlockPos> componentPositions;
	
	protected static final MultiblockComponent AIR = new MultiblockComponent();
	protected static final MultiblockComponent STEEL() {
		return new MultiblockComponent(OreDictHandler.STEEL_BLOCK);
	}
	protected static final MultiblockComponent CASIN() {
		return new MultiblockComponent(OreDictHandler.CASTING_CASING);
	}
	protected static final MultiblockComponent L_ENG() {
		return new MultiblockComponent(OreDictHandler.LIGHT_ENGINEERING);
	}
	protected static final MultiblockComponent H_ENG() {
		return new MultiblockComponent(OreDictHandler.HEAVY_ENGINEERING);
	}
	protected static final MultiblockComponent R_ENG() {
		return new MultiblockComponent(OreDictHandler.RS_ENGINEERING);
	}
	protected static final MultiblockComponent S_SCA() {
		return new MultiblockComponent(OreDictHandler.STEEL_SCAFFOLDING);
	}
	protected static final MultiblockComponent PIPE() {
		return new MultiblockComponent(OreDictHandler.FLUID_PIPE);
	}
	protected static final MultiblockComponent I_SHE() {
		return new MultiblockComponent(OreDictHandler.IRON_SHEETM);
	}
	protected static final MultiblockComponent N_SHE() {
		return new MultiblockComponent(OreDictHandler.NICKEL_SHEETM);
	}

	protected BaVMultiblockHandler(String name, MultiblockComponent[][][] components) {
		this.name = name;
		this.components = components;
		componentPositions = new ArrayList<BlockPos>();
		for (int z = 0; z < components.length; z++) {
			MultiblockComponent[][] zcomp = components[z];
			for (int y = 0; y < components[z].length; y++) {
				MultiblockComponent[] ycomp = zcomp[y];
				for (int x = 0; x < ycomp.length; x++) {
					componentPositions.add(new BlockPos(x, y, z));
				}
			}
		}
}
	protected MultiblockComponent lookup(BlockPos offset) {
		System.out.println("X: "+offset.getX()+" Y: "+offset.getY()+" Z: "+offset.getZ());
		System.out.println(components[offset.getZ()][offset.getY()][offset.getX()]);
		return components[offset.getZ()][offset.getY()][offset.getX()];
	}
	
	private boolean checkValid(IBlockAccess world, BlockPos origin, BlockPos offset, Rotation rot) {
		BlockPos pos = origin.add(offset.rotate(rot));
		MultiblockComponent component = lookup(offset);
		System.out.println(component.valid(world, pos));
		return component.valid(world, pos);
	}
	
	public boolean tryCreate(World world, BlockPos pos) {
		for (BlockPos activationLocation : this.componentPositions) {
			for (Rotation rot : Rotation.values()) {
				BlockPos origin = pos.subtract(activationLocation.rotate(rot));
				boolean valid = true;
				for (BlockPos offset : this.componentPositions) {
					valid = valid && checkValid(world, origin, offset, rot);
				}
				if (valid) {
					if (!world.isRemote) {
						instance(world, origin, rot).onCreate();
					}
					System.out.println("tried to create, created");
					return true;
				}
			}
		}
		System.out.println(components);
		System.out.println("tried to create, but failed");
		return false;
	}
	
	public abstract BlockPos placementPos();
	public void place(World world, EntityPlayer player, BlockPos pos, Rotation rot) {
		Map<String, Integer> missing = new HashMap<String, Integer>();
		BlockPos origin = pos.subtract(this.placementPos().rotate(rot));
		for (BlockPos offset : this.componentPositions) {
			MultiblockComponent component = lookup(offset);
			BlockPos compPos = origin.add(offset.rotate(rot));
			if (!component.valid(world, compPos)) {
				if (!world.isAirBlock(compPos)) {
					if (MiscUtils.canBeReplaced(world, compPos, false)) {
						world.destroyBlock(compPos, true);
					} else {
						player.sendMessage(Chatter.INVALID_BLOCK.getMessage(compPos.getX(), compPos.getY(), compPos.getZ()));
						return;
					}
				}
			}
		}
		
		for (BlockPos offset : this.componentPositions) {
			MultiblockComponent component = lookup(offset);
			BlockPos compPos = origin.add(offset.rotate(rot));
			if (!component.valid(world, compPos)) {
				if (!component.place(world, player, compPos)) {
					if (!missing.containsKey(component.name)) {
						missing.put(component.name, 0);
					}
					missing.put(component.name, missing.get(component.name)+1);
				}
			}
		}
		
		if (missing.size() != 0) {
			player.sendMessage(Chatter.STOCK_MISSING.getMessage());
			for (String name : missing.keySet()) {
				player.sendMessage(new TextComponentString(String.format("  - %d x %s", missing.get(name), name)));
			}
		}
	}
	
	public Map<BlockPos, IBlockState> blueprint() {
		Map<BlockPos, IBlockState> result = new HashMap<BlockPos, IBlockState>();
		for (BlockPos offset : this.componentPositions) {
			MultiblockComponent component = lookup(offset);
			result.put(offset, component.def);
		}
		return result;
	}
	
	public MultiblockInstance instance(World world, BlockPos origin, Rotation rot) {
		return newInstance(world, origin, rot);
	}
	
	protected abstract MultiblockInstance newInstance(World world, BlockPos origin, Rotation rot);
	public abstract class MultiblockInstance {
		protected final World world;
		protected final BlockPos origin;
		protected final Rotation rot;
		
		public MultiblockInstance(World world, BlockPos origin, Rotation rot) {
			this.world = world;
			this.origin = origin;
			this.rot = rot;
		}
		
		public void onCreate() {
			for (BlockPos offset : componentPositions) {
				MultiblockComponent comp = lookup(offset);
				if (comp == AIR) {
					continue;
				}
				
				BlockPos pos = getPos(offset);
				IBlockState origState = world.getBlockState(pos);
				/*
				world.setBlockState(pos, IRBlocks.BLOCK_MULTIBLOCK.getDefaultState());
				TileMultiblock te = TileMultiblock.get(world, pos);
				
				te.configure(name, rot, offset, origState);*/
			}
		}
		public abstract boolean onBlockActivated(EntityPlayer player, EnumHand hand, BlockPos offset);
		public abstract int getInvSize(BlockPos offset);
		public abstract boolean isRender(BlockPos offset);
		public abstract void tick(BlockPos offset);
		public abstract boolean canInsertItem(BlockPos offset, int slot, ItemStack stack);
		public abstract boolean isOutputSlot(BlockPos offset, int slot);
		public abstract int getSlotLimit(BlockPos offset, int slot);
		public abstract boolean canRecievePower(BlockPos offset);
		public void onBreak() {
			for (BlockPos offset : componentPositions) {
				MultiblockComponent comp = lookup(offset);
				if (comp == AIR) {
					continue;
				}
				BlockPos pos = getPos(offset);
				/*TileMultiblock te = TileMultiblock.get(world, pos);
				if (te == null) {
					world.destroyBlock(pos, true);
					continue;
				}
				te.onBreak();*/
			}
		}
		
		/*
		 * Helpers
		 */
		protected BlockPos getPos(BlockPos offset) {
			return origin.add(offset.rotate(rot));
		}
		/*
		protected TileMultiblock getTile(BlockPos offset) {
			TileMultiblock te = TileMultiblock.get(world, getPos(offset));
			if (te == null) {
				if (!world.isRemote) {
					ImmersiveRailroading.warn("Multiblock TE is null: %s %s %s %s", getPos(offset), offset, world.isRemote, this.getClass());
				}
				return null;
			}
			if (!te.isLoaded()) {
				if (!world.isRemote) {
					ImmersiveRailroading.info("Multiblock is still loading: %s %s %s %s", getPos(offset), offset, world.isRemote, this.getClass());
				}
				return null;
			}
			return te;
		}*/
	}
}