package alty.brassandvintagecore.blocks;

import java.util.ArrayList;
import java.util.List;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.items.GearFactory;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.tiles.TileBigGear;
import alty.brassandvintagecore.tiles.TileBigGear;
import alty.brassandvintagecore.util.Properties;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BigGear extends BlockContainer implements IHasModel{
	private static final AxisAlignedBB NORTH = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, .5D);
	private static final AxisAlignedBB SOUTH = new AxisAlignedBB(0D, 0D, .5D, 1D, 1D, 1D);
	private static final AxisAlignedBB EAST = new AxisAlignedBB(.5D, 0D, 0D, 1D, 1D, 1D);
	private static final AxisAlignedBB WEST = new AxisAlignedBB(0D, 0D, 0D, .5D, 1D, 1D);
	private static final AxisAlignedBB UP = new AxisAlignedBB(0D, .5D, 0D, 1D, 1D, 1D);
	private static final AxisAlignedBB DOWN = new AxisAlignedBB(0D, 0D, 0D, 1D, .5D, 1D);
	public static final String NAME = "big_gear_master";
	
	public BigGear(){
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
		return new TileBigGear();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
		if(world.getTileEntity(pos) instanceof TileBigGear){
			return new ItemStack(GearFactory.BASIC_GEARS.get(((TileBigGear) world.getTileEntity(pos)).getMember()), 1);
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[] {Properties.FACING});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(Properties.FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(Properties.FACING).getIndex();
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		switch(state.getValue(Properties.FACING)){
			case UP:
				return UP;
			case DOWN:
				return DOWN;
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case EAST:
				return EAST;
			default:
				return WEST;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
		return false;
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
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		List<ItemStack> drops = new ArrayList<ItemStack>();
		TileBigGear te = (TileBigGear) world.getTileEntity(pos);
		if(te.getMember() != null){
			drops.add(new ItemStack(GearFactory.BASIC_GEARS.get(te.getMember())));
		}
		return drops;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, boolean canHarvest){
		if(canHarvest && worldIn.getTileEntity(pos) instanceof TileBigGear){
			((TileBigGear) worldIn.getTileEntity(pos)).breakGroup(state.getValue(Properties.FACING), true);
		}
		return super.removedByPlayer(state, worldIn, pos, player, canHarvest);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		if(worldIn.getTileEntity(pos) instanceof TileBigGear){
			((TileBigGear) worldIn.getTileEntity(pos)).breakGroup(state.getValue(Properties.FACING), false);
		}
		super.breakBlock(worldIn, pos, state);
	}
	@Override
	public void registerModels() {
		BaVInitialization.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "normal");
	}
}
