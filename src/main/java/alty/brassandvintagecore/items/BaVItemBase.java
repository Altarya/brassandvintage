package alty.brassandvintagecore.items;


import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.objects.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BaVItemBase extends Item implements IHasModel{
	

		public BaVItemBase(String name) {
			setUnlocalizedName(name);
			setRegistryName(name);
			setCreativeTab(BaVInitialization.BAV_TAB);
			
			BaVItems.ITEMS.add(this);
			
		}

		@Override
		public void registerModels() {
				BaVInitialization.proxy.registerItemRenderer(this, 0, "inventory");
		}
}
