package alty.brassandvintagecore.util;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

@SuppressWarnings("deprecation")
public class MiscUtils {
	public static boolean canBeReplaced(World world, BlockPos pos, boolean allowFlex) {
			
			if (world.isAirBlock(pos)) {
				return true;
			}
			
			Block block = world.getBlockState(pos).getBlock();
			
			if (block == null) {
				return true;
			}
			if (block.isReplaceable(world, pos)) {
				return true;
			}
			if (block instanceof IGrowable && !(block instanceof BlockGrass)) {
				return true;
			}
			if (block instanceof IPlantable) {
				return true;
			}
			if (block instanceof BlockLiquid) {
				return true;
			}
			if (block instanceof BlockSnow) {
				return true;
			}
			if (block instanceof BlockLeaves) {
				return true;
			}
			return false;
		}
		
		public static IBlockState itemToBlockState(ItemStack stack) {
			Block block = Block.getBlockFromItem(stack.getItem());
			IBlockState gravelState = block.getStateFromMeta(stack.getMetadata());
			if (block instanceof BlockLog ) {
				gravelState = gravelState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
			}
			return gravelState;
		}
		
		public static Rotation rotFromFacing(EnumFacing facing) {
			switch (facing) {
			case NORTH:
				return Rotation.NONE;
			case EAST:
				return Rotation.CLOCKWISE_90;
			case SOUTH:
				return Rotation.CLOCKWISE_180;
			case WEST:
				return Rotation.COUNTERCLOCKWISE_90;
			default:
				return Rotation.NONE;
			}
		}

		//Chat Stuff
		public enum Chatter {
			STOCK_BUILT("stock.built"), 
			STOCK_DISSASEMBLED("stock.dissasembled"), 
			STOCK_MISSING("stock.missing"),
			STOCK_INVALID("stock.invalid"),
			STOCK_WRONG_GAUGE("stock.wrong_gauge"),
			DEADMANS_SWITCH_ENABLED("stock.deadmans_switch_enabled"),
			DEADMANS_SWITCH_DISABLED("stock.deadmans_switch_disabled"),
			COUPLER_FRONT("coupler.front"),
			COUPLER_BACK("coupler.back"),
			COUPLER_ENGAGED("coupler.engaged"),
			COUPLER_DISENGAGED("coupler.disengaged"),
			COUPLER_STATUS_COUPLED("coupler.status.coupled"),
			COUPLER_STATUS_DECOUPLED_ENGAGED("coupler.status.decoupled.engaged"),
			COUPLER_STATUS_DECOUPLED_DISENGAGED("coupler.status.decoupled.disengaged"),
			BUILD_MISSING_TIES("build.missing.ties"),
			BUILD_MISSING_RAILS("build.missing.rails"),
			BUILD_MISSING_RAIL_BED("build.missing.rail_bed"),
			BUILD_MISSING_RAIL_BED_FILL("build.missing.rail_bed_fill"),
			SET_AUGMENT_FILTER("augment.set"),
			RESET_AUGMENT_FILTER("augment.reset"),
			WOOD_PLANKS("misc.wood_planks"),
			INVALID_BLOCK("build.invalid_block"),
			;
			
			private String value;
			Chatter(String value) {
				this.value = value;
			}
			
			public String getRaw() {
				return "chat.immersiverailroading:" + value;
			}
			
			public TextComponentTranslation getMessage(Object... objects) {
				return new TextComponentTranslation(getRaw(), objects);
			}
	
			@Override
			public String toString() {
				return translateText(getRaw());
			}
		}
		
		
		public static String translateText(String name) {
			return I18n.translateToLocal(name);
		}

		public static String translateText(String name, Object[] objects) {
			return I18n.translateToLocalFormatted(name, objects);
	}
}

