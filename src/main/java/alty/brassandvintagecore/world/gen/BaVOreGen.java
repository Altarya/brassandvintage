package alty.brassandvintagecore.world.gen;

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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BaVOreGen implements IWorldGenerator {
	private WorldGenerator ore_vanadinite;
	//Define Ores
	public BaVOreGen() {
		ore_vanadinite = new WorldGenMinable(BaVBlocks.VANADINITE_ORE.getDefaultState(), 30, BlockMatcher.forBlock(Blocks.STONE));
	}
	//Define Sample Bits for Special Ores
	public Block bit_vanadinite = BaVBlocks.VANADINITE_BIT;
	public Block bit_vanadinite_sea = BaVBlocks.VANADINITE_BIT_WATER;
	
	/* Bit of code borrowed from Geolosys and slightly modified for BaV
	 * https://github.com/oitsjustjose/Geolosys/tree/1.12.x/src/main/java/com/oitsjustjose/geolosys/common/world
	 * Credits go to them for their Sample Logic
	 * PS: on the boolean bitType: false=Land Sample, true=Sea Sample
	 */
	private BlockPos getSamplePos(World world, int PosX, int PosZ, int depositHeight, Random random, boolean bitType)
    {
        int blockPosX = (PosX << 4) + random.nextInt(16);
        int blockPosZ = (PosZ << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, 0, blockPosZ);
        if(bitType = false) {
	        while (searchPos.getY() < world.getHeight())
	        {
	            if (world.getBlockState(searchPos.down()).isSideSolid(world, searchPos.down(), EnumFacing.UP))
	            {
	                // If the current block is air
	                if (canReplace(world, searchPos))
	                {
	                    // If the block above this state is air,
	                    if (canReplace(world, searchPos.up()))
	                    {
	                        // If it's above sea level it's fine
	                        if (searchPos.getY() > world.getSeaLevel())
	                        {
	                            return searchPos;
	                        }
	                        // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
	                        else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12) && searchPos.getY() < depositHeight)
	                        {
	                            return searchPos;
	                        }
	                    }
	                }
	            }
	            searchPos = searchPos.up();
	        }
    	}
        if(bitType==true) {
	        while (searchPos.getY() < world.getHeight())
	        {
	            if (world.getBlockState(searchPos.down()).isSideSolid(world, searchPos.down(), EnumFacing.UP))
	            {
	                // If the current block is air
	                if (canReplaceWater(world, searchPos))
	                {
	                    // If the block above this state is water or air,
	                    if (canReplaceWater(world, searchPos.up()))
	                    {
	                        // If it's above sea level it's fine
	                        if (searchPos.getY() > world.getSeaLevel())
	                        {
	                            return searchPos;
	                        }
	                        // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
	                        else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12) && searchPos.getY() < depositHeight)
	                        {
	                            return searchPos;
	                        }
	                    }
	                    else if (canReplace(world, searchPos.up()))
	                    {
	                        // If it's above sea level it's fine
	                        if (searchPos.getY() > world.getSeaLevel())
	                        {
	                            return searchPos;
	                        }
	                        // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
	                        else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12) && searchPos.getY() < depositHeight)
	                        {
	                            return searchPos;
	                        }
	                    }
	                }
	            }
	            searchPos = searchPos.up();
	        }
        }
        return world.getTopSolidOrLiquidBlock(searchPos);
}
	
	private boolean isWithinRange(int posA, int posB, int range) {
		return (Math.abs(posA - posB) <= range);
	}
	private boolean canReplace(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        return mat == Material.AIR || state.getBlock().isLeaves(state, world, pos) || state.getBlock().isFoliage(world, pos) || mat.isReplaceable();
	}
	private boolean canReplaceWater(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
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
		Biome swamp = Biomes.SWAMPLAND;
		if(testBiome == swamp) {
			runGeneratorSpecial(ore_vanadinite, world, random, chunkX, chunkZ, 1, 2, 15, bit_vanadinite, bit_vanadinite_sea);
		}
		
		
	}
	public int oreY;
	
	private void runGeneratorBit(Block bitBlock, Block bitBlockSea, World world, Random rand, int chunkX, int chunkZ) {
		BlockPos bitPos = getSamplePos(world, chunkX, chunkZ, oreY, rand, false);
			world.setBlockState(bitPos, bitBlock.getDefaultState());
			System.out.println("A vanadinite land bit has been spawned at "+bitPos);
			BlockPos bitPosSea = getSamplePos(world, chunkX, chunkZ, oreY, rand, true);
			world.setBlockState(bitPosSea, bitBlockSea.getDefaultState());
			System.out.println("A vanadinite sea bit has been spawned at "+bitPos);
		}
	
	
	private void runGeneratorSpecial(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight, Block bitBlock, Block bitBlockSea) {
		if(minHeight > maxHeight || maxHeight > 256) throw new IllegalArgumentException("A BaV Ore was generated out of bounds!");
		int i = 0;
		boolean chunkContainsVein = false;
		while(i <= chance && chunkContainsVein == false) {
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			oreY = minHeight + rand.nextInt(16);
			
			if(i < chance) {
					BlockPos veinPos = new BlockPos(x, oreY, z);
					System.out.println("A vanadinite vein has been spawned at " + veinPos);
			}
			if (i == chance-1) {
				runGeneratorBit(bitBlock, bitBlockSea, world, rand, chunkX, chunkZ);
				chunkContainsVein = true;
			}
			gen.generate(world, rand, new BlockPos(x, oreY, z));
			
		}
		
	}
}
