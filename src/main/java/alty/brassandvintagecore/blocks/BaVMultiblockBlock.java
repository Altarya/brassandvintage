package alty.brassandvintagecore.blocks;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.tiles.TileMultiblock;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Slf4j
public class BaVMultiblockBlock extends Block {
	public static final String NAME = "bav_multiblock";

	public BaVMultiblockBlock() {
		super(Material.IRON);
		setHardness(2.0F);
		
        setUnlocalizedName(BaVInitialization.MODID + ":" + NAME);
        setRegistryName(new ResourceLocation(BaVInitialization.MODID, NAME));
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileMultiblock te = TileMultiblock.get(worldIn, pos);
		if (te != null) {
			// Multiblock break
			try {
				te.breakBlock();
			} catch (Exception ex) {
				System.out.print("oh no pee peee pooo poo: "+ex);
				// Something broke
				// TODO figure out why
				worldIn.setBlockToAir(pos);
			}
			worldIn.destroyBlock(pos, true);
		} else {
			// Break during block restore
			super.breakBlock(worldIn, pos, state);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileMultiblock te = TileMultiblock.get(worldIn, pos);
		if (te != null) {
			return te.onBlockActivated(playerIn, hand);
		}
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMultiblock();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TESR Renderer
		return EnumBlockRenderType.MODEL;
	}


	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
