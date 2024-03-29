package alty.brassandvintagecore.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.items.HandScrewgun;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.proxy.CommonProxy;
import alty.brassandvintagecore.tiles.TileAxle;
import alty.brassandvintagecore.util.Properties;

import javax.annotation.Nullable;
import java.util.List;

public class BaVAxle extends BlockContainer implements IHasModel{

	public static String NAME = "axle";
	public BaVAxle(){
		super(Material.IRON);
		setUnlocalizedName(NAME);
		setRegistryName(NAME);
		setCreativeTab(BaVInitialization.BAV_TAB);
		setHardness(3);
		setSoundType(SoundType.METAL);
		BaVBlocks.BLOCKS.add(this);
		BaVItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileAxle();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced){
		tooltip.add("I: .25");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(HandScrewgun.isWrench(playerIn.getHeldItem(hand), worldIn.isRemote)){
			if(!worldIn.isRemote){
				worldIn.setBlockState(pos, state.cycleProperty(Properties.AXIS));
			}
			return true;
		}
		return false;
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		CommonProxy.masterKey++;
		return getDefaultState().withProperty(Properties.AXIS, EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis());
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[] {Properties.AXIS});
	}

	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(Properties.AXIS, EnumFacing.getFront(2 * meta).getAxis());
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
		if(worldIn.isRemote){
			return;
		}
		CommonProxy.masterKey++;
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(Properties.AXIS) == EnumFacing.Axis.Y ? 0 : (state.getValue(Properties.AXIS) == EnumFacing.Axis.Z ? 1 : 2);
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return side.getAxis() == world.getBlockState(pos).getValue(Properties.AXIS);
	}

	private static final AxisAlignedBB XBOX = new AxisAlignedBB(0, .4375D, .4375D, 1, .5625D, .5625D);
	private static final AxisAlignedBB YBOX = new AxisAlignedBB(.4375D, 0, .4375D, .5625D, 1, .5625D);
	private static final AxisAlignedBB ZBOX = new AxisAlignedBB(.4375D, .4375D, 0, .5625D, .5625D, 1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		switch(state.getValue(Properties.AXIS)){
			case X:
				return XBOX;
			case Y:
				return YBOX;
			default:
				return ZBOX;
		}
	}
	
	@Override
	public void registerModels() {
		BaVInitialization.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "normal");
	}
}
