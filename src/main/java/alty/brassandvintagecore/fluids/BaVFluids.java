package alty.brassandvintagecore.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BaVFluids {
	public static final Fluid TAR = new FluidLiquid("tar", new ResourceLocation("brassandvintagecore:fluids/tar_still"), new ResourceLocation("brassandvintagecore:fluids/tar_flowing"));
	
	public static void registerFluids() {
		registerFluid(TAR);
	}
	
	public static void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
	}
}
