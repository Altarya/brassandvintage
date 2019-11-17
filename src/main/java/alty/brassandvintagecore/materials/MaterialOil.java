package alty.brassandvintagecore.materials;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Blocks;

public class MaterialOil extends Material {
		public MaterialOil(MapColor color)
	    {
	        super(color);
	        this.setReplaceable();
	        this.setNoPushMobility();
	    }

	    /**
	     * Returns if blocks of these materials are liquids.
	     */
	    public boolean isLiquid()
	    {
	        return true;
	    }

	    /**
	     * Returns if this material is considered solid or not
	     */
	    public boolean blocksMovement()
	    {
	        return false;
	    }

	    /**
	     * Returns true if the block is a considered solid. This is true by default.
	     */
	    public boolean isSolid()
	    {
	        return false;
	    }
    
}
