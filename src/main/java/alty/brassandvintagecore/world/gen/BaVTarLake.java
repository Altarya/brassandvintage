package alty.brassandvintagecore.world.gen;

import java.util.Random;

import alty.brassandvintagecore.blocks.BaVBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BaVTarLake implements IWorldGenerator {
	
	public static final BaVWorldGenStructure TAR_LAKE = new BaVWorldGenStructure("tar_lake");

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.getDimension()) {
			case 0:
				generateStructure(TAR_LAKE, world, random, chunkX, chunkZ, 50, 100, 31, Blocks.STONE);
				
				break;
		}
		
	}
	
	private void generateStructure(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, int maxY , int chance, int structureHeight, Block topBlock) {
		
		int x = (chunkX * 16) + random.nextInt(4);
		int z = (chunkZ * 16) + random.nextInt(4);
		int y = random.nextInt(maxY);
		
		BlockPos bpos = new BlockPos(x,y,z);
		IBlockState testBlock = world.getBlockState(bpos);
		
		if(random.nextInt(chance) == 0) {
			while(testBlock != topBlock.getDefaultState() && y <= structureHeight && y <= world.getHeight()) {
				y++;
			}
			if(y >= world.getHeight()) {
				y = random.nextInt(maxY);
			}
			System.out.println("A Tar lake has been spawned at:" + bpos);
			generator.generate(world, random, bpos);
			BaVOreGen.runGeneratorBit(BaVBlocks.SPLOTCH_TAR, BaVBlocks.SPLOTCH_TAR_WATER, world, random, chunkX, chunkZ);
				
		}
		
	}
	
	private static int calculateGenerationHeight(World world, int x, int z, Block topBlock) {
		int y = world.getHeight();
		boolean foundGround = false;
		
		while(!foundGround && y >= 0) {
			Block block = world.getBlockState(new BlockPos(x,y,z)).getBlock(); 
			foundGround = block == topBlock;
			y--;
		}
		return y;
	}
}
