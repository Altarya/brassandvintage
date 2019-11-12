package alty.brassandvintagecore.init;

import java.util.ArrayList;
import java.util.List;

import alty.brassandvintagecore.blocks.BaVBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BaVBlocks {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block VANADIUM_ORE = new BaVBlockBase("block_vanadium_ore", Material.IRON);
}
