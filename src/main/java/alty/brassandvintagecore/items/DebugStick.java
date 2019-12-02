package alty.brassandvintagecore.items;

import java.util.HashSet;

import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.multiblocks.common.BaVMultiblockRegister;
import alty.brassandvintagecore.objects.IHasModel;
import alty.brassandvintagecore.tiles.TileMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugStick extends ItemTool implements IHasModel {
public static final String NAME = "item_debug_rod";
	
	public DebugStick() {
		super(2, -3.2F, ToolMaterial.IRON, new HashSet<Block>());
		this.setUnlocalizedName(BaVInitialization.MODID + ":" + NAME);
		this.setRegistryName(new ResourceLocation(BaVInitialization.MODID, NAME));
        this.setCreativeTab(BaVInitialization.BAV_TAB);
        BaVItems.ITEMS.add(this);
	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState mb = BaVBlocks.BAV_MULTIBLOCK.getDefaultState();
		if(world.getBlockState(pos) == mb) {
			player.sendMessage(new TextComponentString("MB Blockpos: "+TileMultiblock.get(world, pos).offset));
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public void registerModels() {
			BaVInitialization.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
