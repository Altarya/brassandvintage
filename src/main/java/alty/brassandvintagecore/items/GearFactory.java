package alty.brassandvintagecore.items;

import java.util.HashMap;

import alty.brassandvintagecore.util.rotationutils.GearTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GearFactory {

	public static final HashMap<GearTypes, BasicGear> BASIC_GEARS = new HashMap<GearTypes, BasicGear>();
	public static final HashMap<GearTypes, BigGear> BIG_GEARS = new HashMap<GearTypes, BigGear>();
	//public static final HashMap<GearTypes, ToggleGear> TOGGLE_GEARS = new HashMap<GearTypes, ToggleGear>();

	public static void init(){
		BASIC_GEARS.clear();
		BIG_GEARS.clear();
		//TOGGLE_GEARS.clear();
		for(GearTypes typ : GearTypes.values()){
			BASIC_GEARS.put(typ, new BasicGear(typ));
			BIG_GEARS.put(typ, new BigGear(typ));
			//TOGGLE_GEARS.put(typ, new ToggleGear(typ));
		}
	}

	@SideOnly(Side.CLIENT)
	public static void clientInit(){
		ItemColors itemColor = Minecraft.getMinecraft().getItemColors();
		for(GearTypes typ : GearTypes.values()){
			int colorCode = typ.getColor().getRGB();
			IItemColor itemColoring = new IItemColor(){
				@Override
				public int colorMultiplier(ItemStack stack, int tintIndex){
					return tintIndex == 0 ? colorCode : -1;
				}
			};
			itemColor.registerItemColorHandler(itemColoring, BASIC_GEARS.get(typ), BIG_GEARS.get(typ));
			//itemColor.registerItemColorHandler(itemColoring, TOGGLE_GEARS.get(typ));
		}
	}
}
