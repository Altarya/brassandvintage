package alty.brassandvintagecore.world.gen;

import java.util.Random;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.objects.IStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class BaVWorldGenStructure extends WorldGenerator implements IStructure{
	
	public static String structureName;
	
	
	
	public BaVWorldGenStructure(String name) {
		this.structureName = name;
	}



	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		
		this.generateStructure(worldIn, position);
		return true;
	}
	
	public static void generateStructure(World world, BlockPos pos) {
		MinecraftServer mcServer = world.getMinecraftServer();
		TemplateManager manager = worldServer.getStructureTemplateManager();
		ResourceLocation location = new ResourceLocation(BaVInitialization.MODID, structureName);
		Template template = manager.get(mcServer, location);
		
		if(template != null) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			template.addBlocksToWorldChunk(world, pos, settings);
		}
	}

}
