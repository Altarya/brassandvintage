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
	public static final Block ZINC_ORE = new BaVBlockBase("ore_zinc", Material.ROCK);
	//Decoration
	public static final Block VANADIUM_BLOCK = new BaVBlockBase("block_vanadium", Material.IRON);
	public static final Block VANADIUM_STEEL_BLOCK = new BaVBlockBase("block_vanadium_steel", Material.IRON);
	//Machine Building
	public static final Block BAV_MULTIBLOCK = new BaVMultiblockBlock();
	public static final Block SIDED_GEAR_HOLDER = new SidedGearHolder();
	public static final Block BIG_GEAR = new BigGear();
	public static final Block BIG_GEAR_SLAVE = new BigGearSlave();
	public static final Block AXLE = new BaVAxle();
	//Machines
	public static final Block ELECTRIC_MOTOR = new ElectricMotor();
	public static final Block ELECTRIC_DYNAMO = new ElectricDynamo();
	//Fluid Blocks
	public static final Block TAR = new BaVBlockFluid("tar", BaVFluids.TAR, BaVCustomMaterials.THICK_OIL, 1, 50, 20, 200, true, 5000, true);
	//Misc
	public static final Block RUBBER_TREE_LOG = new BaVBlockLog("log_rubbertree", Material.WOOD);
	public static final Block RUBBER_TREE_PLANKS = new BaVBlockBase("plank_rubbertree", Material.WOOD);
	public static final Block RUBBER_TREE_LEAVES = new BaVBlockLeaves("leaves_rubbertree", Material.LEAVES);
	public static final Block RUBBER_TREE_SAPLING = new BaVBlockSapling("sapling_rubbertree", Material.PLANTS);
}
