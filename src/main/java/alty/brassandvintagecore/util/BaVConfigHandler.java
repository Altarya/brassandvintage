package alty.brassandvintagecore.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import minecrafttransportsimulator.dataclasses.PackPartObject;
import minecrafttransportsimulator.systems.*;
import net.minecraftforge.common.config.Configuration;

public class BaVConfigHandler {
	public static Configuration config;	
	
	private static final String BAV_FUEL_CONFIG = "fuel";
	private static Map<String, Double> doubleConfigMap = new HashMap<String, Double>();
	
	
	private static final String COMMON = "general";
	

	public static void initFuels() {
		try
		{
			Map<String, Map<String, Double>> fuelConfigMaps = new HashMap<String, Map<String, Double>>();
			Field BaVfuelConfigMapsField = ConfigSystem.class.getDeclaredField("fuelConfigMaps");
			BaVfuelConfigMapsField.setAccessible(true);
			fuelConfigMaps = (Map<String, Map<String, Double>>) BaVfuelConfigMapsField.get(null);
			List<String> fuelNames = new ArrayList<String>();
			for(String packPartName : PackParserSystem.getAllPartPackNames()){
				PackPartObject packPart = PackParserSystem.getPartPack(packPartName);
				if(packPart.general.type.startsWith("engine")){
					if(!fuelNames.contains(packPart.engine.fuelType)){
						fuelNames.add(packPart.engine.fuelType);
					}
				}
			}
			for(String fuelName : fuelNames){
				String[] defaultValues = new String[] {};
				switch(fuelName){
				//TODO
					case "steam": 
						defaultValues = new String[]{"water:1.0", "distilled_water:2.0"}; 
						break;
					default:
						continue;
				}

				Map<String, Double> fluidPotencies = new HashMap<String, Double>();
				for(String configEntry : config.get(BAV_FUEL_CONFIG, fuelName, defaultValues).getStringList()){
					String fluidName = configEntry.substring(0, configEntry.indexOf(':'));
					double fluidPotency = Double.valueOf(configEntry.substring(configEntry.indexOf(':') + 1));
					fluidPotencies.put(fluidName, fluidPotency);
				}
				
				fuelConfigMaps.put(fuelName, fluidPotencies);
				BaVfuelConfigMapsField.set(null, fuelConfigMaps);
				SuperLogger.logger.info("Brass and Vintage has added its fuel types", 0);
				SuperLogger.logger.warn("Registered Fuels: "+BaVfuelConfigMapsField.get(null));
			}
			ConfigSystem.config.save();
			config.save();
		} catch (NoSuchFieldException | IllegalAccessException e) {
		       e.printStackTrace();
		}
	}
	
	public static void initCommon(File configFile){
		config = new Configuration(configFile);
		config.load();
		//TODO
		config.save();
	}
	
	
	
}
