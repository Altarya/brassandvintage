package alty.brassandvintagecore.blocks;

import alty.brassandvintagecore.blocks.item.BaVItemBlockVariants;
import alty.brassandvintagecore.init.BavInitialization;
import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.objects.IMetaName;
import alty.brassandvintagecore.util.EnumHandler;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.EnumSet;

public class BaVBlockLog extends BlockLog implements IHasModel, IMetaName {

    public static final PropertyEnum<EnumHandler.EnumType> VARIANT = PropertyEnum.<EnumHandler.EnumType>create("variant", EnumHandler.EnumType.class, variant -> variant.getMeta() < 2);

    public BaVBlockLog(String name, Material material) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setSoundType(SoundType.WOOD);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.EnumType.RUBBER_LOG_VERTICAL).withProperty(LOG_AXIS, EnumAxis.Y));

        BaVBlocks.BLOCKS.add(this);
        BaVItems.ITEMS.add(new BaVItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumHandler.EnumType)state.getValue(VARIANT)).getMeta();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumHandler.EnumType)state.getValue(VARIANT)).getMeta();

        switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
            case X: i |= 2; break;
            case Y: i |= 4; break;
            case Z: i |= 6; break;
        }

        return i;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, EnumHandler.EnumType.byMetadata((meta & 1) % 2));

        switch (meta & 6) {
            case 0: state = state.withProperty(LOG_AXIS, EnumAxis.Y); break;
            case 2: state = state.withProperty(LOG_AXIS, EnumAxis.X); break;
            case 4: state = state.withProperty(LOG_AXIS, EnumAxis.Z); break;
            default: state = state.withProperty(LOG_AXIS, EnumAxis.NONE); break;
        }

        return state;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        EnumSet.allOf(EnumHandler.EnumType.class)
                .forEach(variant -> items.add(new ItemStack(this, 1, variant.getMeta())));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, LOG_AXIS});
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((EnumHandler.EnumType)state.getValue(VARIANT)).getMeta());
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return EnumHandler.EnumType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void registerModels() {
        EnumSet.allOf(EnumHandler.EnumType.class)
                .forEach(variant -> BavInitialization.proxy.registerVariantRenderer(Item.getItemFromBlock(this), variant.getMeta(), "log_" + variant.getName(), "inventory"));
    }
}
