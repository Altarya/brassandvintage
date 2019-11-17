package alty.brassandvintagecore.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alty.brassandvintagecore.blocks.BaVBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;



public class BaVOreGen implements IWorldGenerator {
	private WorldGenerator ore_vanadinite;
	private WorldGenerator ore_zinc;
	//Define Ores
	public BaVOreGen() {
		ore_vanadinite = new WorldGenMinable(BaVBlocks.VANADINITE_ORE.getDefaultState(), 50, BlockMatcher.forBlock(Blocks.STONE));
		ore_zinc =  new WorldGenMinable(BaVBlocks.ZINC_ORE.getDefaultState(), 5, BlockMatcher.forBlock(Blocks.STONE));
	}
	//Define Sample Bits for Special Ores
	public Block bit_vanadinite = BaVBlocks.VANADINITE_BIT;
	public Block bit_vanadinite_sea = BaVBlocks.VANADINITE_BIT_WATER;
	
	/* Bit of code borrowed from Geolosys and slightly modified for BaV
	 * https://github.com/oitsjustjose/Geolosys
	 * Credits go to them for their Sample Logic
	 * PS: on the boolean bitType: false=Land Sample, true=Sea Sample
	 */
	private static BlockPos getSamplePos(World world, int PosX, int PosZ, int depositHeight, Random random, boolean bitType)
    {
        int blockPosX = (PosX << 4) + random.nextInt(16);
        int blockPosZ = (PosZ << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, 0, blockPosZ);
        if(bitType == false) {
	        while (searchPos.getY() < world.getHeight()) {
	            if (world.getBlockState(searchPos.down()).isSideSolid(world, searchPos.down(), EnumFacing.UP)
	            		&& canReplace(world, searchPos) 			// If the current block is air
	            		&& canReplace(world, searchPos.up()) 		// If the block above this state is air
	            		&& searchPos.getY() > world.getSeaLevel() 	// If it's above sea level it's fine
	            ) {   
	                return searchPos;
	            }
	            // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
	            else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12) && searchPos.getY() < depositHeight) {
	                return searchPos;
	            }
	            searchPos = searchPos.up();
	        }
    	} else {
	        while (searchPos.getY() < world.getHeight()) {
	            if (world.getBlockState(searchPos.down()).isSideSolid(world, searchPos.down(), EnumFacing.UP)
	            		&& canReplaceWater(world, searchPos)			// If the current block is air
	            		&& canReplaceWater(world, searchPos.up())		// If the block above this state is water or air
	               ) {
                        // If it's above sea level it's fine
                        if (searchPos.getY() > world.getSeaLevel()) {
                            return searchPos;
                        }
                        // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
                        else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12) && searchPos.getY() < depositHeight) {
                            return searchPos;
                        }
                    } else if (canReplace(world, searchPos.up())) {
                        // If it's above sea level it's fine
                        if (searchPos.getY() > world.getSeaLevel()) {
                            return searchPos;
                        }
                        // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
                        else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12) && searchPos.getY() < depositHeight) {
                            return searchPos;
                        }
	                }
	            searchPos = searchPos.up();
	        }
        }
        return world.getTopSolidOrLiquidBlock(searchPos);
}
	
	private static boolean isWithinRange(int posA, int posB, int range) {
		return (Math.abs(posA - posB) <= range);
	}
	private static boolean canReplace(World world, BlockPos posate) {
		IBlockState state = world.getBlockState(posate);
        Material mat = state.getMaterial();
        return mat == Material.AIR || state.getBlock().isLeaves(state, world, posate) || state.getBlock().isFoliage(world, posate) || mat.isReplaceable();
	}
	private static boolean canReplaceWater(World world, BlockPos posate) {
		IBlockState state = world.getBlockState(posate);
        Material mat = state.getMaterial();
        return mat == Material.WATER || mat.isReplaceable();
	}
	//End of the borrowed code
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		
		int genx = chunkX * 16;
		int geny = 64;
		int genz = chunkZ * 16;
		Biome testBiome = world.provider.getBiomeForCoords(new BlockPos(genx, geny, genz));
		
		List<Type> WhitelistVanadinite = new ArrayList<Type>();
		WhitelistVanadinite.addAll(BiomeDictionary.getTypes(Biomes.DESERT));
		
		List<Type> testType = new ArrayList<Type>();
		testType.addAll(BiomeDictionary.getTypes(testBiome));
		
		if(testType.containsAll(WhitelistVanadinite)) {
			runGeneratorSpecial(ore_vanadinite, world, random, chunkX, chunkZ, 1, 2, 15, bit_vanadinite, bit_vanadinite_sea);
		}
		
		runGenerator(ore_zinc, world, random, chunkX, chunkZ, 5, 5, 128);
		
		
	}
	public static int oreY;
	
	static void runGeneratorBit(Block bitBlock, Block bitBlockSea, World world, Random rand, int chunkX, int chunkZ) {
		BlockPos bitPos = getSamplePos(world, chunkX, chunkZ, oreY, rand, false);
			int bitCount = rand.nextInt(5);
			int j = 0;
			while(j <= bitCount) {
				world.setBlockState(bitPos, bitBlock.getDefaultState());
				//System.out.println("A vanadinite land bit has been spawned at "+bitPos);
				BlockPos bitPosSea = getSamplePos(world, chunkX, chunkZ, oreY, rand, true);
				world.setBlockState(bitPosSea, bitBlockSea.getDefaultState());
				//System.out.println("A vanadinite sea bit has been spawned at "+bitPos);
				j++;
			}
		}
	
	
	private void runGeneratorSpecial(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight, Block bitBlock, Block bitBlockSea) {
		if(minHeight > maxHeight || maxHeight > 256) throw new IllegalArgumentException("A BaV Ore was generated out of bounds!");
		int i = rand.nextInt(1000);
		
		if(i <= chance) {
			int x = chunkX * 16 + rand.nextInt(4);
			int z = chunkZ * 16 + rand.nextInt(4);
			oreY = minHeight + rand.nextInt(16);
			//BlockPos veinPos = new BlockPos(x, oreY, z);
			//System.out.println("A vanadinite vein has been spawned at " + veinPos);
			
			runGeneratorBit(bitBlock, bitBlockSea, world, rand, chunkX, chunkZ);
			gen.generate(world, rand, new BlockPos(x, oreY, z));
		}
	}
	
	private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight) {
		if(minHeight > maxHeight || maxHeight > 256) throw new IllegalArgumentException("A BaV Ore was generated out of bounds!");
		int i = 0;
		
		while(i <= rand.nextInt(chance)) {
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(maxHeight);
			gen.generate(world, rand, new BlockPos(x, y, z));
			i++;
		}
	}
}
