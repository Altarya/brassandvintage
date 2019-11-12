package alty.brassandvintagecore.init;

import java.util.ArrayList;
import java.util.List;

import alty.brassandvintagecore.blocks.BaVBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BaVBlocks {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block VANADIUM_ORE = new BaVBlockBase("block_vanadium_ore", Material.ROCK);
	
	public static final Block RUBBER_TREE_LOG = new BaVBlockBase("block_rubber_tree_log", Material.WOOD);
	public static final Block RUBBER_TREE_PLANKS = new BaVBlockBase("block_rubber_tree_planks", Material.WOOD);
	
	public static final Block RUBBER_TREE_LEAVES = new BaVBlockBase("block_rubber_tree_leaves", Material.LEAVES);
	
	public static final Block RUBBER_TREE_SAPLING = new BaVBlockBase("block_rubber_tree_sapling", Material.PLANTS);
}
