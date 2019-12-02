package alty.brassandvintagecore.proxy;

import alty.brassandvintagecore.gui.BaVTab;
import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.init.manual.BaVIntroduction;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;

public class ClientProxy extends CommonProxy{
	@Override
	public void preInitStart(FMLPreInitializationEvent event){
		OBJLoader.INSTANCE.addDomain(BaVInitialization.MODID);
	}
	
	@Override
	public void Init(FMLPreInitializationEvent event){
		if(TabRegistry.getTabList().isEmpty()) {
			MinecraftForge.EVENT_BUS.register(new TabRegistry());
			TabRegistry.registerTab(new InventoryTabVanilla());
		}
		TabRegistry.registerTab(new BaVTab());
	}
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

	@Override
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(BaVInitialization.MODID, filename), id));
	}
	
	@Override
	public void postInitEnd(FMLPostInitializationEvent event){
		ManualInstance man=ManualHelper.getManual();
		BaVIntroduction.page(man);
		System.out.print("Brass and Vintage wrapped its pages into the Engineer's manual successfuly");
	}
	
	@Override
	public EntityPlayer getPlayerClient() {
		return Minecraft.getMinecraft().player;
	}
}
