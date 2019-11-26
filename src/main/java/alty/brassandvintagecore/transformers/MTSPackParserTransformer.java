package alty.brassandvintagecore.transformers;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import alty.brassandvintagecore.items.mts.ItemSteamEngine_car;
import alty.brassandvintagecore.mts.PartSteamEngine_Car;
import alty.brassandvintagecore.util.SuperLogger;
import minecrafttransportsimulator.dataclasses.CreativeTabPack;
import minecrafttransportsimulator.dataclasses.MTSRegistry;
import minecrafttransportsimulator.dataclasses.PackPartObject;
import minecrafttransportsimulator.items.parts.AItemPart;
import minecrafttransportsimulator.items.parts.ItemPartBarrel;
import minecrafttransportsimulator.items.parts.ItemPartBullet;
import minecrafttransportsimulator.items.parts.ItemPartCrate;
import minecrafttransportsimulator.items.parts.ItemPartCustom;
import minecrafttransportsimulator.items.parts.ItemPartEngineAircraft;
import minecrafttransportsimulator.items.parts.ItemPartEngineBoat;
import minecrafttransportsimulator.items.parts.ItemPartEngineCar;
import minecrafttransportsimulator.items.parts.ItemPartEngineJet;
import minecrafttransportsimulator.items.parts.ItemPartGeneric;
import minecrafttransportsimulator.items.parts.ItemPartGroundDevicePontoon;
import minecrafttransportsimulator.items.parts.ItemPartGroundDeviceSkid;
import minecrafttransportsimulator.items.parts.ItemPartGroundDeviceTread;
import minecrafttransportsimulator.items.parts.ItemPartGroundDeviceWheel;
import minecrafttransportsimulator.items.parts.ItemPartGun;
import minecrafttransportsimulator.items.parts.ItemPartPropeller;
import minecrafttransportsimulator.systems.PackParserSystem;
import minecrafttransportsimulator.vehicles.parts.APart;
import minecrafttransportsimulator.vehicles.parts.PartBarrel;
import minecrafttransportsimulator.vehicles.parts.PartBrewingStand;
import minecrafttransportsimulator.vehicles.parts.PartCraftingTable;
import minecrafttransportsimulator.vehicles.parts.PartCrate;
import minecrafttransportsimulator.vehicles.parts.PartCustom;
import minecrafttransportsimulator.vehicles.parts.PartEngineAircraft;
import minecrafttransportsimulator.vehicles.parts.PartEngineBoat;
import minecrafttransportsimulator.vehicles.parts.PartEngineCar;
import minecrafttransportsimulator.vehicles.parts.PartEngineJet;
import minecrafttransportsimulator.vehicles.parts.PartFurnace;
import minecrafttransportsimulator.vehicles.parts.PartGroundDevicePontoon;
import minecrafttransportsimulator.vehicles.parts.PartGroundDeviceSkid;
import minecrafttransportsimulator.vehicles.parts.PartGroundDeviceTread;
import minecrafttransportsimulator.vehicles.parts.PartGroundDeviceWheel;
import minecrafttransportsimulator.vehicles.parts.PartGunFixed;
import minecrafttransportsimulator.vehicles.parts.PartGunTripod;
import minecrafttransportsimulator.vehicles.parts.PartPropeller;
import minecrafttransportsimulator.vehicles.parts.PartSeat;

public class MTSPackParserTransformer {
	private static final Map<String, PackPartObject> partPackMap = new LinkedHashMap<String, PackPartObject>();
	private static final Map<String, String[]> craftingItemMap = new HashMap<String, String[]>();
	
	public static Class<? extends APart> getPartPartClass(String partName){
    	switch(PackParserSystem.getPartPack(partName).general.type){
			case "engine_steam_car": return PartSteamEngine_Car.class;
			default: return null;
		}
	}
	
	 public static Class<? extends AItemPart> getPartItemClass(String partName){
	    	switch(getPartPack(partName).general.type){
				case "engine_steam_car": return ItemSteamEngine_car.class;
				default: return null;
			}
	    }
	 public static PackPartObject getPartPack(String name){
	        return partPackMap.get(name);
	    }
	 
	 public static void addPartDefinition(InputStreamReader jsonReader, String jsonFileName, String modID){
	    	try{
		    	PackPartObject pack =  new Gson().fromJson(jsonReader, PackPartObject.class);
		    	String partName = modID + ":" + jsonFileName;
		    	partPackMap.put(partName, pack);
		    	if(!MTSRegistry.packTabs.containsKey(modID)){
					MTSRegistry.packTabs.put(modID, new CreativeTabPack(modID));
				}
		    	craftingItemMap.put(partName, pack.general.materials);
	    	}catch(Exception e){
	    		//logList.add("AN ERROR WAS ENCOUNTERED WHEN TRY TO PARSE: " + modID + ":" + jsonFileName);
	    		//logList.add(e.getMessage());
	    	}
	    }
}
