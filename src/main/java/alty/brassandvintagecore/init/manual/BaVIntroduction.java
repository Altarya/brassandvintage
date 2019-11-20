package alty.brassandvintagecore.init.manual;

import alty.brassandvintagecore.init.BavInitialization;
import alty.brassandvintagecore.multiblocks.BaVTarDistiller;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import minecrafttransportsimulator.items.core.ItemVehicle;
import net.minecraft.item.ItemStack;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;

public class BaVIntroduction {
	public static void page(ManualInstance man) {
	IManualPage[] PAGE = new IManualPage[]{
				new ManualPages.ItemDisplay(man, BavInitialization.MODID+".introduction.p0", new ItemStack(ItemVehicle.getByNameOrId("brassandvintage:modelt_black"), 1, 0)),
				new ManualPages.Image(man, BavInitialization.MODID+".introduction.p1", "brassandvintagecore:textures/manual/ore_zinc.png"),
				//new ManualPageMultiblock(man, BavInitialization.MODID+".introduction.p2", BaVTarDistiller.instance),
		};
	man.addEntry(BavInitialization.MODID+".introduction", BavInitialization.MODID, PAGE);
	}
}

