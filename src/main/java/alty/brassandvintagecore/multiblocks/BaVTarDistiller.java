package alty.brassandvintagecore.multiblocks;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import alty.brassandvintagecore.multiblocks.common.BaVMultiblockRegister;
import alty.brassandvintagecore.multiblocks.common.MultiblockComponent;
import alty.brassandvintagecore.tiles.TileMultiblock;
import alty.brassandvintagecore.util.BaVMultiblockHandler;
import alty.brassandvintagecore.util.OreDictHandler;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;



@Slf4j
public class BaVTarDistiller extends BaVMultiblockHandler implements IMultiblock {
	
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
		for (String key : BaVMultiblockRegister.keys()) {
			if (BaVMultiblockRegister.get(key).tryCreate(world, pos)) {
				player.sendMessage(new TextComponentString("Multiblock Formed!"));
				return true;
			}
		}
		ItemStack hammer = player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains(Lib.TOOL_HAMMER)?player.getHeldItemMainhand(): player.getHeldItemOffhand();
		if(MultiblockHandler.fireMultiblockFormationEventPost(player, this, pos, hammer).isCanceled())
			System.out.println("Multiblock check failed: Wrong item!");
			return false;
	}

	


	@Override
	public ItemStack[][][] getStructureManual() {
		return structure;
	}

	@Override
	public IngredientStack[] getTotalMaterials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean overwriteBlockRender(ItemStack stack, int iterator) {
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
	}
	
	
	public static final String NAME = "TAR_DISTILLER";
	private static final BlockPos render = new BlockPos(3,3,7);
	private static final BlockPos fluidRender1 = new BlockPos(3,3,3);
	private static final BlockPos fluidRender2 = new BlockPos(3,3,3);
	private static final BlockPos craft = new BlockPos(3,2,3);
	private static final BlockPos output = new BlockPos(3,2,14);
	private static final BlockPos power = new BlockPos(3,7,0);
	private static final BlockPos center = new BlockPos(4,1,0);
	public static final double max_volume = 5 * 4 * 4.5 * 9;

	private static MultiblockComponent[][][] tardistiller() {
		MultiblockComponent[][][] structure = new MultiblockComponent[4][11][7];
		for(int h = 0; h < 11; h++) {
			for(int l = 0; l < 4; l++) {
				for(int w = 0; w < 7; w++)
				{
					if(h==0)
					{
						if(l<4&&w<=7)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.STEEL_SCAFFOLDING);
						if(l==0&&w>2&&w<6) {
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.HEAVY_ENGINEERING);}
						}
						if(l==3&&w==1) {
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.FLUID_PIPE);
						}
					else if(h==1)
					{
						if(l==0&&w==6)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.RS_ENGINEERING);
						else if(l==0&&w<6&&w>2)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.HEAVY_ENGINEERING);
						else if(l==1&&(w==3||w==5))
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.FLUID_PIPE);
						else if((l==3||l==2)&&w>3&&w<7&&w!=5)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.FLUID_PIPE);
						else if(l==1&&(w==4||w==6))
							structure[l][h][w] = new MultiblockComponent(Blocks.GLASS);
						else if((l==1||l==2)&&(w==1||w==2))
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.IRON_SHEETM);
						else if(l==0&&w==2)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.STEEL_SCAFFOLDING);
					}
					else if(h>0&&h<11)
					{
						if(l==1&&(w==4||w==6)&&h<5)
							structure[l][h][w] = new MultiblockComponent(Blocks.GLASS);
						else if(l==1&&(w==4||w==6)&&h==5)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.NICKEL_SHEETM);
						else if((l==1||l==2)&&(w==1||w==2)&&h<9)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.IRON_SHEETM);
						else if((l==1||l==2)&&(w==1||w==2)&&h>8)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.NICKEL_SHEETM);
						else if(l==0&&w==2&&h<9)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.STEEL_SCAFFOLDING);
						else if (h==4&&(w<4&&l<5))
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.STEEL_SCAFFOLDING);
						else if (h==8&&(w<4&&l<5))
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.STEEL_SCAFFOLDING);
						else if(l==2&&(w==4||w==6)&&h<6)
							structure[l][h][w] = new MultiblockComponent(OreDictHandler.FLUID_PIPE);
					}
						if(structure[l][h][w]==null)
						{
							structure[l][h][w] = AIR;
						}	

				}
			}
		}
		return structure;
	}

	
	public BaVTarDistiller() {
		super(NAME, tardistiller());
	}
	
	@Override
	public BlockPos placementPos() {
		return new BlockPos(0, 0, 0);
	}

	@Override
	protected MultiblockInstance newInstance(World world, BlockPos origin, Rotation rot) {
		return new TarDistillerInstance(world, origin, rot);
	}
	public class TarDistillerInstance extends MultiblockInstance {
		
		public TarDistillerInstance(World world, BlockPos origin, Rotation rot) {
			super(world, origin, rot);
		}

		@Override
		public boolean onBlockActivated(EntityPlayer player, EnumHand hand, BlockPos offset) {
			if (isCenter(offset)) {
				if (!world.isRemote) {
					BlockPos pos = getPos(offset);
					player.sendMessage(new TextComponentString("I am here!"));
				}
				return true;
			}
			return false;
		}
		@Override
		public boolean isCenter(BlockPos offset) {
			return offset.equals(center);
		}

		@Override
		public boolean isRender(BlockPos offset) {
			return render.equals(offset);
		}

		@Override
		public int getInvSize(BlockPos offset) {
			//return output.equals(offset) ? 1 : 0;
			return 0;
		}

		@Override
		public boolean canInsertItem(BlockPos offset, int slot, ItemStack stack) {
			return false;
		}

		@Override
		public boolean isOutputSlot(BlockPos offset, int slot) {
			return false;
		}

		@Override
		public int getSlotLimit(BlockPos offset, int slot) {
			return output.equals(offset) ? 1 : 0;
		}

		@Override
		public boolean canRecievePower(BlockPos offset) {
			return offset.equals(power);
		}

		public boolean hasPower() {
			return false;
		}

		@Override
		public void tick(BlockPos offset) {
			// TODO Auto-generated method stub
			if (!isCenter(offset)) {
				return;
			}
			TileMultiblock te = getTile(offset);
			if (te == null) {
				return;
			}
			TileMultiblock powerTe = getTile(power);
			if (powerTe == null) {
				return;
			}
			
			if (!hasPower()) {
				return;
			}
			if (world.isRemote) {
				if (te.getRenderTicks() % 10 == 0 && te.getCraftProgress() != 0) {
					world.playSound(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0f, 0.2f, false);
				}
				return;
			}
		}
	}
}
	
	
