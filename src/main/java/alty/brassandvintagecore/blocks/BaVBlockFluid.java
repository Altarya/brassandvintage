package alty.brassandvintagecore.blocks;

import alty.brassandvintagecore.items.BaVItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BaVBlockFluid extends BlockFluidClassic {

	private boolean isHeavy = false;
	
	public BaVBlockFluid(String name, Fluid fluid, Material material, float slipperiness, int tickRate, int temperature, boolean isHeavy) {
		super(fluid, material);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setDefaultSlipperiness(slipperiness);
		this.setTickRate(tickRate);
		this.setTemperature(temperature);
		this.isHeavy = isHeavy;
		
		boolean toDisplaceWater = fluid.getDensity() > 1000;
		displacements.put(Blocks.WATER, toDisplaceWater);
		displacements.put(Blocks.FLOWING_WATER, toDisplaceWater);
		
		BaVBlocks.BLOCKS.add(this);
		BaVItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (isHeavy) {
            entityIn.setInWeb();
        }
}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
			return EnumBlockRenderType.MODEL;
	}
}
