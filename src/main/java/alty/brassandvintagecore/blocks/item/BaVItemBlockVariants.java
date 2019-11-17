package alty.brassandvintagecore.blocks.item;

import alty.brassandvintagecore.objects.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BaVItemBlockVariants extends ItemBlock {

    public BaVItemBlockVariants(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "_" + ((IMetaName)this.block).getSpecialName(stack);
    }
}
