package alty.brassandvintagecore.util;

import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.fluids.BaVFluids;
import alty.brassandvintagecore.init.BavInitialization;
import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.world.gen.BaVOreGen;
import alty.brassandvintagecore.world.gen.BaVTarLake;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = BavInitialization.MODID)
public class RegistryHandler {
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(BaVItems.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BaVBlocks.BLOCKS.toArray(new Block[0]));
    }
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item : BaVItems.ITEMS) {
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		for(Block block : BaVBlocks.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel)block).registerModels();
            }
        }
	}
	
	
	public static void otherBaVRegistries() {
		BaVFluids.registerFluids();
		
		GameRegistry.registerWorldGenerator(new BaVOreGen(), 0);
		RenderHandler.registerustomMeshesAndStates();
		
		GameRegistry.registerWorldGenerator(new BaVTarLake(), 0);
	}
}
