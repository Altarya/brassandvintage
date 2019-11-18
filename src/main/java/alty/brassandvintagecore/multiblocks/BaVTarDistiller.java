package alty.brassandvintagecore.multiblocks;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration2;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaVTarDistiller implements IMultiblock{
	
	public static BaVTarDistiller instance = new BaVTarDistiller();
	
	static ItemStack[][][] structure = new ItemStack[11][4][7];

	static
	{
		for(int h = 0; h < 11; h++)
			for(int l = 0; l < 4; l++)
				for(int w = 0; w < 7; w++)
				{
					if(h==0)
					{
						if(l<4&&w<=7)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						if(l==0&&w>2&&w<6) {
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());}
						}
						if(l==3&&w==1) {
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta());
						}
					else if(h==1)
					{
						if(l==0&&w==6)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
						else if(l==0&&w<6&&w>2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
						else if(l==1&&(w==3||w==5))
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta());
						else if((l==3||l==2)&&w>3&&w<7&&w!=5)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta());
						else if(l==1&&(w==4||w==6))
							structure[h][l][w] = new ItemStack(Item.getItemFromBlock(Blocks.GLASS));
						else if((l==1||l==2)&&(w==1||w==2))
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.IRON.getMeta());
						else if(l==0&&w==2)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
					}
					else if(h>0&&h<11)
					{
						if(l==1&&(w==4||w==6)&&h<5)
							structure[h][l][w] = new ItemStack(Item.getItemFromBlock(Blocks.GLASS));
						else if(l==1&&(w==4||w==6)&&h==5)
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.NICKEL.getMeta());
						else if((l==1||l==2)&&(w==1||w==2)&&h<9)
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.IRON.getMeta());
						else if((l==1||l==2)&&(w==1||w==2)&&h>8)
							structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.NICKEL.getMeta());
						else if(l==0&&w==2&&h<9)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
						else if (h==4&&(w<4&&l<5))
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_1.getMeta());
						else if (h==8&&(w<4&&l<5))
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_1.getMeta());
						else if(l==2&&(w==4||w==6)&&h<6)
							structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta());
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
		side = side.getOpposite();
		if(side==EnumFacing.UP||side==EnumFacing.DOWN)
			side = EnumFacing.fromAngle(player.rotationYaw);

		boolean mirror = false;
		boolean b = this.structureCheck(world, pos, side, mirror);
		if(!b)
		{
			mirror = true;
			b = structureCheck(world, pos, side, mirror);
		}
		if(!b) {
			System.out.println("Multiblock check failed: Structure check failed!");
			return false;
		}
		ItemStack hammer = player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains(Lib.TOOL_HAMMER)?player.getHeldItemMainhand(): player.getHeldItemOffhand();
		if(MultiblockHandler.fireMultiblockFormationEventPost(player, this, pos, hammer).isCanceled())
			System.out.println("Multiblock check failed: Wrong item!");
			return false;
	}
	
	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int h = -1; h <= 1; h++)
			for(int l = 0; l <= 2; l++)
				for(int w = -2; w <= 2; w++)
				{
					if((h==0&&l==0&&w!=0&&w!=2)||(h==0&&l==0&&w==0)||(h==1&&(l==0||w==0)))
						continue;

					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					if(h==-1)
					{
						if(l==1)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDevice1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta()))
								return false;
						}
						else if(l==0&&w==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
								return false;
						}
						else if(l==2&&w==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
								return false;
						}
						else
						{
							if(!Utils.isOreBlockAt(world, pos, "scaffoldingSteel"))
								return false;
						}
					}
					else if(h==0)
					{
						if(l==0&&w==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
								return false;
						}
						if(l==2&&w==0)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()))
								return false;
						}
						else if(l==0&&w==2)
						{
							if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta()))
								return false;
						}
						else if(l > 0&&w!=0)
						{
							if(!Utils.isOreBlockAt(world, pos, "blockSheetmetalIron"))
								return false;
						}
					}
					else if(h==1)
					{
						if(l > 0&&w!=0)
						{
							if(!Utils.isOreBlockAt(world, pos, "blockSheetmetalIron"))
								return false;
						}
					}
				}
		return true;
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
		return 10;
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
