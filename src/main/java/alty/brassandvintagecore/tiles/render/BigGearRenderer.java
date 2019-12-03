package alty.brassandvintagecore.tiles.render;

import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.tiles.TileBigGear;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.rotationutils.IAxleHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class BigGearRenderer extends TileEntitySpecialRenderer<TileBigGear>{

	private static final ResourceLocation TEXTURE = new ResourceLocation(BaVInitialization.MODID, "textures/model/gear_24.png");
	private static final ResourceLocation TEXTURE_RIM = new ResourceLocation(BaVInitialization.MODID, "textures/model/gear_rim.png");
	private static final ModelBigGear MODEL = new ModelBigGear();
	
	@Override
	public void render(TileBigGear gear, double x, double y, double z, float partialTicks, int destroyStage, float alpha){

		if(gear.getWorld().getBlockState(gear.getPos()).getBlock() != BaVBlocks.BIG_GEAR || !gear.getWorld().isBlockLoaded(gear.getPos(), false)){
			return;
		}

		IAxleHandler handler;
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.disableLighting();
		
		GlStateManager.translate(x + .5D, y + .5D, z + .5D);
		if(gear.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.DOWN)){
			handler = gear.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.DOWN);
		}else if(gear.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.UP)){
			GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
			handler = gear.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.UP);
		}else if(gear.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.EAST)){
			GlStateManager.rotate(90F, 0.0F, 0.0F, 1.0F);
			handler = gear.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.EAST);
		}else if(gear.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.WEST)){
			GlStateManager.rotate(270F, 0.0F, 0.0F, 1.0F);
			handler = gear.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.WEST);
		}else if(gear.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.NORTH)){
			GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
			handler = gear.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.NORTH);
		}else{
			GlStateManager.rotate(270F, 1.0F, 0.0F, 0.0F);
			handler = gear.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.SOUTH);
		}
		if(handler != null){
			GlStateManager.rotate((float) handler.getAngle(), 0F, 1F, 0F);
			GlStateManager.scale(3, 1, 3);
			MODEL.render(TEXTURE, TEXTURE_RIM, gear.getMember().getColor());
		}
		
		GlStateManager.enableLighting();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();

	}
}
