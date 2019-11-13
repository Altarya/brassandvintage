package alty.brassandvintagecore.world.gen;

import java.util.Random;

import alty.brassandvintagecore.blocks.BaVBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BaVOreGen implements IWorldGenerator {
	private WorldGenerator ore_vanadinite;
	private WorldGenerator bit_vanadinite_grass;
	private WorldGenerator bit_vanadinite_sand;
	
	public BaVOreGen() {
		ore_vanadinite = new WorldGenMinable(BaVBlocks.VANADINITE_ORE.getDefaultState(), 30, BlockMatcher.forBlock(Blocks.STONE));
		bit_vanadinite_grass = new WorldGenMinable(BaVBlocks.VANADINITE_BIT.getDefaultState(), 1, BlockMatcher.forBlock(Blocks.GRASS));
		bit_vanadinite_sand = new WorldGenMinable(BaVBlocks.VANADINITE_BIT.getDefaultState(), 1, BlockMatcher.forBlock(Blocks.SAND));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		
		int genx = chunkX * 16;
		int geny = 64;
		int genz = chunkZ * 16;
		Biome testBiome = world.provider.getBiomeForCoords(new BlockPos(genx, geny, genz));
		Biome swamp = Biomes.SWAMPLAND;
		if(testBiome == swamp) {
			runGenerator(bit_vanadinite_grass, world, random, chunkX, chunkZ, 5, 64, 256);
			runGenerator(ore_vanadinite, world, random, chunkX, chunkZ, 5, 2, 15);
		}
		
		
	}
	
	private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight) {
		if(minHeight > maxHeight || maxHeight > 256) throw new IllegalArgumentException("A BaV Ore was generated out of bounds!");
		int heightDiff = maxHeight - minHeight + 1;
		for(int i = 0; i < chance; i++) {
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(16);
			
			gen.generate(world, rand, new BlockPos(x, y, z));
		}
	}
}
