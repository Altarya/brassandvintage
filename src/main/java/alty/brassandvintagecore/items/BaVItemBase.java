package alty.brassandvintagecore.items;


import alty.brassandvintagecore.init.BaVItems;
import alty.brassandvintagecore.init.BavInitialization;
import alty.brassandvintagecore.objects.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BaVItemBase extends Item implements IHasModel{
	

		public BaVItemBase(String name) {
			setUnlocalizedName(name);
			setRegistryName("json_" + name);
			setCreativeTab(CreativeTabs.MATERIALS);
			
			BaVItems.ITEMS.add(this);
			
		}

		@Override
		public void registerModels() {
				BavInitialization.proxy.registerItemRenderer(this, 0, "inventory");
		}
}
