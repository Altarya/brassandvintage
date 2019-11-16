package alty.brassandvintagecore.util;

import alty.brassandvintagecore.blocks.BaVBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class RenderHandler {
	
	
	public static void registerustomMeshesAndStates() {
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(BaVBlocks.TAR),new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				//tar
				return new ModelResourceLocation("brassandvintagecore:tar", "fluid");
				
			}
		});
		
		//tar
		ModelLoader.setCustomStateMapper(BaVBlocks.TAR, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation("brassandvintagecore:tar", "fluid");
			}

		});
	}
}
