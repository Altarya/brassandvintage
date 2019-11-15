package alty.brassandvintagecore.blocks;

import alty.brassandvintagecore.items.BaVItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BaVBlockFluid extends BlockFluidClassic {

	public BaVBlockFluid(String name, Fluid fluid, Material material) {
		super(fluid, material);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		
		BaVBlocks.BLOCKS.add(this);
		BaVItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
			return EnumBlockRenderType.MODEL;
	}
}
