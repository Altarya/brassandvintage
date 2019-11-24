package alty.brassandvintagecore.blocks;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.objects.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BaVBlockBase extends Block implements IHasModel {

	public BaVBlockBase(String name, Material material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(BaVInitialization.BAV_TAB);
		
		BaVBlocks.BLOCKS.add(this);
		BaVItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() {
		BaVInitialization.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "normal");
	}
}
