package alty.brassandvintagecore.blocks;

import java.util.ArrayList;
import java.util.List;

import alty.brassandvintagecore.fluids.BaVFluids;
import alty.brassandvintagecore.materials.BaVCustomMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BaVBlocks {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	//Ores
	public static final Block VANADINITE_ORE = new BaVBlockBase("ore_vanadinite", Material.ROCK);
	public static final Block VANADINITE_BIT = new BaVBlockBase("bit_vanadinite", Material.ROCK);
	public static final Block VANADINITE_BIT_WATER = new BaVBlockBase("bit_vanadinite_water", Material.ROCK);
	public static final Block SPLOTCH_TAR = new BaVBlockBase("splotch_tar", Material.GROUND);
	public static final Block SPLOTCH_TAR_WATER = new BaVBlockBase("splotch_tar_water", Material.GROUND);
	//Decoration
	public static final Block VANADIUM_BLOCK = new BaVBlockBase("block_vanadium", Material.IRON);
	public static final Block VANADIUM_STEEL_BLOCK = new BaVBlockBase("block_vanadium_steel", Material.IRON);
	//Machine Building
	//Fluid Blocks
	public static final Block TAR = new BaVBlockFluid("tar", BaVFluids.TAR, BaVCustomMaterials.THICK_OIL, 1, 50, 20, true);
	//Misc
	public static final Block RUBBER_TREE_LOG = new BaVBlockBase("log_rubbertree", Material.WOOD);
	public static final Block RUBBER_TREE_PLANKS = new BaVBlockBase("plank_rubbertree", Material.WOOD);
	public static final Block RUBBER_TREE_LEAVES = new BaVBlockBase("leaf_rubbertree", Material.LEAVES);
	public static final Block RUBBER_TREE_SAPLING = new BaVBlockBase("sapling_rubbertree", Material.PLANTS);
}
