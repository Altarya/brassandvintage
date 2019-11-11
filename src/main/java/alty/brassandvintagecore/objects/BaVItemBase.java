package alty.brassandvintagecore.objects;

import alty.brassandvintagecore.BaVMain;
import alty.brassandvintagecore.init.BaVItems;
import alty.brassandvintagecore.proxy.ClientProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BaVItemBase extends Item implements IHasModel{
		public BaVItemBase(String name) {
			setUnlocalizedName(name);
			setRegistryName(name);
			setCreativeTab(CreativeTabs.MATERIALS);
			
			BaVItems.ITEMS.add(this);
		}

		@Override
		public void registerModels() {
			ClientProxy.registerModels() {
				BaVMain.proxy.registerItemRenderer(this, 0, "inventory");
			};
		}
}
