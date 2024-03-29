package alty.brassandvintagecore.items.mts;

import java.util.List;

import minecrafttransportsimulator.dataclasses.PackPartObject;
import minecrafttransportsimulator.items.parts.AItemPart;
import minecrafttransportsimulator.items.parts.AItemPartEngine;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemSteamEngine_car extends AItemPart{
	public ItemSteamEngine_car(String partName) {
		super(partName);
	}

	@SideOnly(Side.CLIENT)
	protected void addExtraInformation(ItemStack stack, PackPartObject pack, List<String> tooltipLines){
		tooltipLines.add(pack.engine.isAutomatic ? I18n.format("info.item.engine.automatic") : I18n.format("info.item.engine.manual"));
		tooltipLines.add(I18n.format("info.item.engine.gearratios"));
		for(byte i=0; i<pack.engine.gearRatios.length; i+=3){
			String gearRatios = String.valueOf(pack.engine.gearRatios[i]);
			if(i+1 < pack.engine.gearRatios.length){
				gearRatios += ",   " + String.valueOf(pack.engine.gearRatios[i+1]);
			}
			if(i+2 < pack.engine.gearRatios.length){
				gearRatios += ",   " + String.valueOf(pack.engine.gearRatios[i+2]);
			}
			tooltipLines.add(gearRatios);
		}
	}
}
