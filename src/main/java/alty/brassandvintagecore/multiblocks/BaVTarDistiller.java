package alty.brassandvintagecore.multiblocks;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaVTarDistiller implements IMultiblock{
	
	public static BaVTarDistiller instance = new BaVTarDistiller();
	
	static ItemStack[][][] structure = new ItemStack[5][5][5];

	static
	{
		for(int h = 0; h < 5; h++)
			for(int l = 0; l < 5; l++)
				for(int w = 0; w < 5; w++)
				{
					if(h==0)
					{
						if(l==0&&w==2)
							structure[h][l][w] = new ItemStack(Items.CAULDRON);
						else if(l==2&&(w==0||w==4))
							structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						else if(l==0&&w==0)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						else if(l==4&&w==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else if(l==4||(l > 2&&(w==0||w==4)))
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.STEEL.getMeta());
						else
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetalSlabs, 1, BlockTypes_MetalsAll.STEEL.getMeta());
					}
					else if(h==1)
					{
						if(l==2&&(w==0||w==4))
							structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						else if(l==0&&w==0)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
						else if(l==4&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if((w==0||w==4)&&l > 2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else if(l >= 2&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta());
					}
					else if(h==2)
					{
						if(l==2&&(w==0||w==4))
							structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
						else if(l==4&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if(l==4)
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.STEEL.getMeta());
						else if(w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta());
					}
					else if(h==3)
					{
						if(l==4&&w==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if(l==4&&(w==1||w==3))
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						else if(l > 0&&w > 0&&w < 4)
							structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta());
					}
					else if(h==4)
					{
						if(l > 1&&w==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
						else if(l==4&&(w==1||w==3))
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
					}

					if(structure[h][l][w]==null)
					{
						structure[h][l][w] = ItemStack.EMPTY;
					}
				}
	}


	@Override
	public String getUniqueName() {
		return "BaV:TarDistiller";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state) {
		return state.getBlock()==IEContent.blockMetalDecoration0 && (state.getBlock().getMetaFromState(state)==BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack[][][] getStructureManual() {
		// TODO Auto-generated method stub
		return structure;
	}

	@Override
	public IngredientStack[] getTotalMaterials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean overwriteBlockRender(ItemStack stack, int iterator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getManualScale() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canRenderFormedStructure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renderFormedStructure() {
		// TODO Auto-generated method stub
		
	}
	
}
