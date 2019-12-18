package alty.brassandvintagecore.world.gen;

import alty.brassandvintagecore.world.feature.tree.BaVRubberTreeGen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaVRubberTree implements IWorldGenerator {
	
	private final WorldGenerator RUBBER_TREE = new BaVRubberTreeGen();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.getDimension()) {
			case 0:
				runGenerator(RUBBER_TREE, world, random, chunkX, chunkZ, 2.4, -1, 0, BiomeJungle.class);

				break;
		}
		
	}
	
	private void runGenerator(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, double chance, int minHeight, int maxHeight, Class<?>... biomes) {
		
		if (chance < 1) {
			if (random.nextDouble() < chance) {
				chance = 1;
			} else {
				chance = 0;
			}
		}

		List<Class<?>> biomeList = new ArrayList<Class<?>>(Arrays.asList(biomes));
		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chance; i++) {
			BlockPos pos = new BlockPos(chunkX * 16 + 10 + random.nextInt(15), minHeight + random.nextInt(heightDiff), chunkZ * 16 + 10 + random.nextInt(15));
			if (minHeight < 0) {
				pos = world.getHeight(pos);
			}
			Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();
			if (biomeList.contains(biome) || biomes.length == 0) {
				generator.generate(world, random, pos);
			}
		}
	}
}
