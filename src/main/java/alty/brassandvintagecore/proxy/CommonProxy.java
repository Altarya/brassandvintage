package alty.brassandvintagecore.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id ){}
	public void registerVariantRenderer(Item item, int meta, String filename, String id ){}
	public void postInitEnd(FMLPostInitializationEvent event){}
	public void preInitStart(FMLPreInitializationEvent event){}
	
}
