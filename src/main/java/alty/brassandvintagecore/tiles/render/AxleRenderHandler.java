package alty.brassandvintagecore.tiles.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import alty.brassandvintagecore.blocks.BaVBlocks;
import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.tiles.TileAxle;
import alty.brassandvintagecore.util.Properties;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.rotationutils.GearTypes;

public class AxleRenderHandler extends TileEntitySpecialRenderer<TileAxle>{

	private final ResourceLocation textureAx = new ResourceLocation(BaVInitialization.MODID, "textures/model/axle.png");
	private final ModelAxle modelAx = new ModelAxle();

	@Override
	public void render(TileAxle axle, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		if(!axle.getWorld().isBlockLoaded(axle.getPos(), false)){
			return;
		}

		EnumFacing.Axis axis = axle.getWorld().getBlockState(axle.getPos()).getValue(Properties.AXIS);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translate(x + .5F, y + .5F, z + .5F);
		GlStateManager.rotate(axis == EnumFacing.Axis.Y ? 0 : 90F, axis == EnumFacing.Axis.Z ? 1 : 0, 0, axis == EnumFacing.Axis.X ? 1 : 0);
		GlStateManager.rotate((axis == EnumFacing.Axis.X ? -1 : 1) * (float) axle.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis)).getAngle(), 0F, 1F, 0F);
		GlStateManager.color(1, 1, 1, 1);
		modelAx.render(textureAx, textureAx, Color.YELLOW);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}