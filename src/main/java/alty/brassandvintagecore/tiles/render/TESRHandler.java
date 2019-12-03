package alty.brassandvintagecore.tiles.render;

import alty.brassandvintagecore.tiles.SidedGearHolderTileEntity;
import alty.brassandvintagecore.tiles.TileBigGear;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TESRHandler {
	public static void registerBlockRenderer(){

		ClientRegistry.bindTileEntitySpecialRenderer(SidedGearHolderTileEntity.class, new BasicGearRender());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBigGear.class, new BigGearRenderer());
		//ClientRegistry.bindTileEntitySpecialRenderer(ToggleGearTileEntity.class, new ToggleGearRenderer());
	}

	public static void reg(Block block){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName().toString(), "inventory"));
	}
}	
