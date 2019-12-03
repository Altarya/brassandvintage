package alty.brassandvintagecore.proxy;

import java.io.File;

import alty.brassandvintagecore.util.BaVConfigHandler;
import alty.brassandvintagecore.util.RotaryCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id ){}
	public void registerVariantRenderer(Item item, int meta, String filename, String id ){}
	public void postInitEnd(FMLPostInitializationEvent event){}
	public void preInitStart(FMLPreInitializationEvent event){}
	
	public static int masterKey = 0;
	
	public void initConfig(File configFile){
		BaVConfigHandler.initCommon(configFile);
	}
	public void Init(FMLInitializationEvent event) {	}
	
	public EntityPlayer getPlayerClient() {
		return null;
	}
}
