package alty.brassandvintagecore.util;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictHandler {
	public static class OreAbstract {
		private ItemStack fallback;
		public final String def;
		public final String name;
		
		public OreAbstract(String name, ItemStack fallback) {
			this.name = name;
			this.def = null;
			this.fallback = fallback;
		}

		public OreAbstract(String name, String def, ItemStack fallback) {
			this.name = name;
			this.def = def;
			this.fallback = fallback;
		}
		
		public NonNullList<ItemStack> getOres() {
			NonNullList<ItemStack> ores = OreDictionary.getOres(name);
			if (ores.size() != 0) {
				return ores;
			}
			
			// Fallback/merge
			if (def != null) {
				ores = OreDictionary.getOres(def);
				if (ores.size() != 0) {
					return ores;
				}
			}
			
			ores = NonNullList.create();
			
			ores.add(fallback);
			
			return ores;
		}
		
		public void add(ItemStack stack) {
			OreDictionary.registerOre(name, stack);
		}

		public void add(Block block) {
			OreDictionary.registerOre(name, block);
		}
		
		public void add(Item item) {
			OreDictionary.registerOre(name, item);
		}
		
		public boolean matches(ItemStack stack, boolean strict) {
			return oreDictionaryContainsMatch(strict, getOres(), stack);
		}
		
		public ItemStack example() {
			return getOres().get(0);
		}
	}
	
	public static final OreAbstract CASTING_CASING = new OreAbstract("CastingCasing", new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta()));
	public static final OreAbstract LIGHT_ENGINEERING = new OreAbstract("LightEngineering", new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()));
	public static final OreAbstract HEAVY_ENGINEERING = new OreAbstract("HeavyEngineering", new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()));
	public static final OreAbstract RS_ENGINEERING = new OreAbstract("redstoneEngineering", new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta()));
	public static final OreAbstract STEEL_SCAFFOLDING = new OreAbstract("SteelScaffolding", "scaffoldingSteel", new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta()));
	public static final OreAbstract STEEL_BLOCK = new OreAbstract("blockSteel", new ItemStack(IEContent.blockStorage, 1 , BlockTypes_MetalsAll.STEEL.getMeta()));
	public static final OreAbstract FLUID_PIPE = new OreAbstract("pipeSteel", new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta()));
	public static final OreAbstract IRON_SHEETM = new OreAbstract("blockSheetmetalIron", new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.IRON.getMeta()));
	public static final OreAbstract NICKEL_SHEETM = new OreAbstract("blockSheetmetalNickel", new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.NICKEL.getMeta()));
	public static final OreAbstract RADIATOR = new OreAbstract("blockRadiator", new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RADIATOR.getMeta()));
	
	private static boolean oreDictionaryContainsMatch(boolean strict, NonNullList<ItemStack> ores, ItemStack playerStack) {
        for (ItemStack target : ores)
        {
            if (OreDictionary.itemMatches(target, playerStack, strict))
            {
                return true;
            }
        }
        return false;
	}
}
