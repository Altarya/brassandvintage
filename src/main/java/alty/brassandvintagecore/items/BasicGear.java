package alty.brassandvintagecore.items;

import java.util.List;

import javax.annotation.Nullable;

import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.proxy.CommonProxy;
import alty.brassandvintagecore.tiles.SidedGearHolderTileEntity;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.SuperMath;
import alty.brassandvintagecore.util.rotationutils.GearTypes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BasicGear extends Item implements IHasModel {
	
	private final GearTypes type;
	public static final ModelResourceLocation LOCAT = new ModelResourceLocation(BaVInitialization.MODID + ":gear_base", "inventory");

	public BasicGear(GearTypes typeIn){
		String name = "gear_" + typeIn.toString().toLowerCase();
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(BaVInitialization.BAV_TAB);
		type = typeIn;
		BaVItems.ITEMS.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced){
		tooltip.add("I: " + SuperMath.betterRound(type.getDensity() / 8, 2) * .125);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(worldIn.isRemote){
			return EnumActionResult.SUCCESS;
		}

		TileEntity te = worldIn.getTileEntity(pos.offset(side));
		if(te instanceof SidedGearHolderTileEntity && !te.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, side.getOpposite()) && worldIn.isSideSolid(pos, side)){
			if(!playerIn.capabilities.isCreativeMode){
				playerIn.getHeldItem(hand).shrink(1);
			}

			((SidedGearHolderTileEntity) te).setMembers(type, side.getOpposite().getIndex(), false);
			CommonProxy.masterKey++;
		}else if(worldIn.getBlockState(pos.offset(side)).getBlock().isReplaceable(worldIn, pos.offset(side)) && worldIn.isSideSolid(pos, side)){
			if(!playerIn.capabilities.isCreativeMode){
				playerIn.getHeldItem(hand).shrink(1);
			}

			worldIn.setBlockState(pos.offset(side), BaVBlocks.SIDED_GEAR_HOLDER.getDefaultState(), 3);
			te = worldIn.getTileEntity(pos.offset(side));
			((SidedGearHolderTileEntity) te).setMembers(type, side.getOpposite().getIndex(), true);
			CommonProxy.masterKey++;
		}

		return EnumActionResult.SUCCESS;
	}
	@Override
	public void registerModels() {
			BaVInitialization.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
