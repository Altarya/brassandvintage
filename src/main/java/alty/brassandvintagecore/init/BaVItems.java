package alty.brassandvintagecore.init;

import java.util.ArrayList;
import java.util.List;

import alty.brassandvintagecore.objects.BaVItemBase;
import net.minecraft.item.Item;

public class BaVItems {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static BaVItemBase addItem(String itemName) {
	    return new BaVItemBase(itemName);
	}
}
