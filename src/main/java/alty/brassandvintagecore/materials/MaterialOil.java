package alty.brassandvintagecore.materials;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Blocks;

public class MaterialOil extends MaterialLiquid {
	public MaterialOil(MapColor color)
    {
        super(color);
   
        
    }
    
    public boolean getCanBurn() {
    	return true;
    }
    
}
