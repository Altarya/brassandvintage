package alty.brassandvintagecore.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.blocks.BaVMultiblockBlock;
import alty.brassandvintagecore.fluids.BaVFluids;
import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.multiblocks.BaVTarDistiller;
import alty.brassandvintagecore.multiblocks.common.BaVMultiblockRegister;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.tiles.TileMultiblock;
import alty.brassandvintagecore.transformers.MTSPackParserTransformer;
import alty.brassandvintagecore.world.gen.BaVOreGen;
import alty.brassandvintagecore.world.gen.BaVTarLake;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import minecrafttransportsimulator.dataclasses.MTSRegistry;
import minecrafttransportsimulator.items.core.ItemDecor;
import minecrafttransportsimulator.items.core.ItemInstrument;
import minecrafttransportsimulator.items.core.ItemItem;
import minecrafttransportsimulator.items.core.ItemVehicle;
import minecrafttransportsimulator.items.parts.AItemPart;
import minecrafttransportsimulator.systems.ConfigSystem;
import minecrafttransportsimulator.systems.PackParserSystem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = BaVInitialization.MODID)
public class RegistryHandler {
	public static Map<String, AItemPart> partItemMap = new LinkedHashMap<String, AItemPart>();
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(BaVItems.ITEMS.toArray(new Item[0]));
		
		
		//MTS Injection
		for(String partName : PackParserSystem.getAllPartPackNames()){
			try{
				if(partName.contains("engine_steam")) {
					Class<? extends AItemPart> itemClass = MTSPackParserTransformer.getPartItemClass(partName);
					Constructor<? extends AItemPart> construct = itemClass.getConstructor(String.class);
					AItemPart itemPart = construct.newInstance(partName);
					partItemMap.put(partName, itemPart);
					System.out.println("I am Here loading items");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
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
	
	public static void registerBaVMultiblocks() {
		GameRegistry.registerTileEntity(TileMultiblock.class, BaVMultiblockBlock.NAME);
		MultiblockHandler.registerMultiblock(BaVTarDistiller.instance);
		BaVMultiblockRegister.register("TAR_DISTILLER", new BaVTarDistiller());
	}
	
	public static void registerBaVCommon() {
		
		BaVFluids.registerFluids();
		
		GameRegistry.registerWorldGenerator(new BaVOreGen(), 0);
		RenderHandler.registerustomMeshesAndStates();
		
		GameRegistry.registerWorldGenerator(new BaVTarLake(), 0);
	}
	
	public static List<Item> getBaVItemsForPack(String modID){
		List<Item> packItems = new ArrayList<Item>();
		for(AItemPart item : partItemMap.values()){
			if(item.partName.startsWith(modID)){
				packItems.add(item);
			}
		}
		return packItems;
	}
}
