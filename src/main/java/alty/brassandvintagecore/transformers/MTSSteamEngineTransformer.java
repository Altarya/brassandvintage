package alty.brassandvintagecore.transformers;

import minecrafttransportsimulator.dataclasses.PackPartObject.*;
import minecrafttransportsimulator.dataclasses.PackVehicleObject.PackPart;
import minecrafttransportsimulator.vehicles.main.EntityVehicleE_Powered;
import minecrafttransportsimulator.vehicles.parts.APartEngineGeared;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MTSSteamEngineTransformer extends APartEngineGeared {
	public MTSSteamEngineTransformer(EntityVehicleE_Powered vehicle, PackPart packPart, String partName,
			NBTTagCompound dataTag) {
		super(vehicle, packPart, partName, dataTag);
		this.steamType = dataTag.getString("steamType");
	}

	public String steamType;
	
	@Override
	public boolean interactPart(EntityPlayer player){
		System.out.println("I am a steam engine");
		if(steamType == null) {
			steamType = "none";
		}
		switch(steamType) {			
			case "liquid":
				System.out.println("My type is: "+steamType);
				break;
			case "solid":
				System.out.println("My type is: "+steamType);
				break;
			case "nuclear":
				System.out.println("My type is: "+steamType);
				break;
			case "moltensalt":
				System.out.println("My type is: "+steamType);
				break;
			default:
				System.out.println("i am not a steam engine!");
				break;
		}
		return true;
	}
	
	@Override
	public double getForceOutput() {
		// TODO Auto-generated method stub
		return 0;
	}
}
