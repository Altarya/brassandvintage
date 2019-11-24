package alty.brassandvintagecore.materials;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.items.BaVItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaVCreativeTab extends CreativeTabs {
 
    public BaVCreativeTab() {
        super(BaVInitialization.MODID);
    }
 
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(BaVItems.HAND_SCREWGUN);
    }
 
}
