package alty.brassandvintagecore.items;

import java.util.HashSet;

import alty.brassandvintagecore.init.BavInitialization;
import alty.brassandvintagecore.multiblocks.common.BaVMultiblockRegister;
import alty.brassandvintagecore.objects.IHasModel;
import net.minecraft.block.Block;
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

public class HandScrewgun extends ItemTool implements IHasModel {
public static final String NAME = "item_hand_screwgun";
	
	public HandScrewgun() {
		super(2, -3.2F, ToolMaterial.IRON, new HashSet<Block>());
		this.setUnlocalizedName(BavInitialization.MODID + ":" + NAME);
		this.setRegistryName(new ResourceLocation(BavInitialization.MODID, NAME));
        this.setCreativeTab(CreativeTabs.MISC);
        BaVItems.ITEMS.add(this);
	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			for (String key : BaVMultiblockRegister.keys()) {
				if (BaVMultiblockRegister.get(key).tryCreate(world, pos)) {
					player.sendMessage(new TextComponentString("Multiblock Formed!"));
					return EnumActionResult.SUCCESS;
				}
			}
			System.out.println(BaVMultiblockRegister.keys());
			player.sendMessage(new TextComponentString("Nothing to Screw!"));
		return EnumActionResult.PASS;
	}
	
	@Override
	public void registerModels() {
			BavInitialization.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
