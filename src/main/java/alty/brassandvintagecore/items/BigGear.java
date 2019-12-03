package alty.brassandvintagecore.items;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.proxy.CommonProxy;
import alty.brassandvintagecore.tiles.TileBigGear;
import alty.brassandvintagecore.tiles.TileBigGearSlave;
import alty.brassandvintagecore.util.Properties;
import alty.brassandvintagecore.util.SuperMath;
import alty.brassandvintagecore.util.rotationutils.GearTypes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BigGear extends Item implements IHasModel{

	private final GearTypes type;
	public static final ModelResourceLocation LOCAT = new ModelResourceLocation(BaVInitialization.MODID + ":gear_base", "inventory");


	public BigGear(GearTypes typeIn){
		String name = "big_gear_" + typeIn.toString().toLowerCase();
		setUnlocalizedName(name);
		setRegistryName(name);
		type = typeIn;
		setCreativeTab(BaVInitialization.BAV_TAB);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced){
		tooltip.add("I: " + SuperMath.betterRound(4.5D * type.getDensity(), 2) * 1.125D);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		pos = pos.offset(side);

		for(BlockPos cPos : section(pos, side)){
			if(!worldIn.getBlockState(cPos).getBlock().isReplaceable(worldIn, cPos)){
				return EnumActionResult.SUCCESS;
			}
		}

		if(!playerIn.capabilities.isCreativeMode){
			playerIn.getHeldItem(hand).shrink(1);
		}

		for(BlockPos cPos : section(pos, side)){
			if(pos.equals(cPos)){
				worldIn.setBlockState(pos, BaVBlocks.BIG_GEAR.getDefaultState().withProperty(Properties.FACING, side.getOpposite()), 3);
				((TileBigGear) worldIn.getTileEntity(pos)).initSetup(type);
			}else{
				worldIn.setBlockState(cPos, BaVBlocks.BIG_GEAR_SLAVE.getDefaultState().withProperty(Properties.FACING, side.getOpposite()), 3);
				((TileBigGearSlave) worldIn.getTileEntity(cPos)).setInitial(pos);
			}
		}
		++CommonProxy.masterKey;

		return EnumActionResult.SUCCESS;
	}

	private static BlockPos[] section(BlockPos pos, EnumFacing side){
		if(side == EnumFacing.UP || side == EnumFacing.DOWN){
			return new BlockPos[] {pos.offset(EnumFacing.NORTH, -1).offset(EnumFacing.EAST, -1), pos.offset(EnumFacing.NORTH, -1), pos.offset(EnumFacing.NORTH, -1).offset(EnumFacing.EAST, 1), pos.offset(EnumFacing.EAST, -1), pos, pos.offset(EnumFacing.EAST, 1), pos.offset(EnumFacing.NORTH, 1).offset(EnumFacing.EAST, -1), pos.offset(EnumFacing.NORTH, 1), pos.offset(EnumFacing.NORTH, 1).offset(EnumFacing.EAST, 1)};
		}
		if(side == EnumFacing.EAST || side == EnumFacing.WEST){
			return new BlockPos[] {pos.offset(EnumFacing.NORTH, -1).offset(EnumFacing.UP, -1), pos.offset(EnumFacing.NORTH, -1), pos.offset(EnumFacing.NORTH, -1).offset(EnumFacing.UP, 1), pos.offset(EnumFacing.UP, -1), pos, pos.offset(EnumFacing.UP, 1), pos.offset(EnumFacing.NORTH, 1).offset(EnumFacing.UP, -1), pos.offset(EnumFacing.NORTH, 1), pos.offset(EnumFacing.NORTH, 1).offset(EnumFacing.UP, 1)};
		}
		return new BlockPos[] {pos.offset(EnumFacing.UP, -1).offset(EnumFacing.EAST, -1), pos.offset(EnumFacing.UP, -1), pos.offset(EnumFacing.UP, -1).offset(EnumFacing.EAST, 1), pos.offset(EnumFacing.EAST, -1), pos, pos.offset(EnumFacing.EAST, 1), pos.offset(EnumFacing.UP, 1).offset(EnumFacing.EAST, -1), pos.offset(EnumFacing.UP, 1), pos.offset(EnumFacing.UP, 1).offset(EnumFacing.EAST, 1)};
	}
	
	@Override
	public void registerModels() {
			BaVInitialization.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
