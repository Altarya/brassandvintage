package alty.brassandvintagecore.tiles.render;

import java.awt.Color;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.tiles.SidedGearHolderTileEntity;
import alty.brassandvintagecore.util.RotaryCapabilities;
import alty.brassandvintagecore.util.rotationutils.IAxleHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class BasicGearRender extends TileEntitySpecialRenderer<SidedGearHolderTileEntity>{

	private final ModelBasicGear modelOct = new ModelBasicGear();
	private final ResourceLocation res = new ResourceLocation(BaVInitialization.MODID, "textures/model/gear_oct.png");

	@Override
	public void render(SidedGearHolderTileEntity gearHolder, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		if(!gearHolder.getWorld().isBlockLoaded(gearHolder.getPos(), false)){
			return;
		}

		Color color;
		
		for(EnumFacing side : EnumFacing.values()){
			if(gearHolder.hasCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, side)){
				IAxleHandler handler = gearHolder.getCapability(RotaryCapabilities.AXLE_HANDLER_CAPABILITY, side);
				color = gearHolder.getMembers()[side.getIndex()].getColor();
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				GlStateManager.disableLighting();
				GlStateManager.translate(x + .5D, y + .5D, z + .5D);
				GlStateManager.rotate(side == EnumFacing.DOWN ? 0 : side == EnumFacing.UP ? 180F : side == EnumFacing.NORTH || side == EnumFacing.EAST ? 90F : -90F, side.getAxis() == EnumFacing.Axis.Z ? 1 : 0, 0, side.getAxis() == EnumFacing.Axis.Z ? 0 : 1);
				float angle = (float) (handler.getNextAngle() - handler.getAngle());
				angle *= partialTicks;
				angle += handler.getAngle();
				GlStateManager.rotate(angle, 0F, 1F, 0F);
				modelOct.render(res, color);
				GlStateManager.enableLighting();
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
			}
		}
	}
}
