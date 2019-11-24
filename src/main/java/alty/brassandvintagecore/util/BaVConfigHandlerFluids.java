package alty.brassandvintagecore.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minecrafttransportsimulator.dataclasses.PackPartObject;
import minecrafttransportsimulator.systems.*;
import net.minecraftforge.common.config.Configuration;

public class BaVConfigHandlerFluids {
	public static Configuration config;	
	
	private static final String BAV_FUEL_CONFIG = "fuel";
	
	private static Map<String, Map<String, Double>> getMTSFuels() {
		try
		{
			Map<String, Map<String, Double>> BaVfuelConfigMaps = new HashMap<String, Map<String, Double>>();
			Field BaVfuelConfigMapsField = ConfigSystem.class.getDeclaredField("fuelConfigMaps");
			BaVfuelConfigMapsField.setAccessible(true);
			BaVfuelConfigMaps = (Map<String, Map<String, Double>>) BaVfuelConfigMapsField.get(BaVfuelConfigMaps);
			return BaVfuelConfigMaps;
		} catch (NoSuchFieldException | IllegalAccessException e) {
		       e.printStackTrace();
		}
		return null;
	}
	
	public static void initFuels(){
		List<String> fuelNames = new ArrayList<String>();
		
		for(String fuelName : fuelNames){
			String[] defaultValues;
			switch(fuelName){
			//TODO
				case "liquidsteam": defaultValues = new String[]{"lava:1.0", "gasoline:1.0", "ethanol:0.85"}; break;
				default: 
					defaultValues = new String[] {""};
					break;
			}

			Map<String, Double> fluidPotencies = new HashMap<String, Double>();
			for(String configEntry : config.get(BAV_FUEL_CONFIG, fuelName, defaultValues).getStringList()){
				String fluidName = configEntry.substring(0, configEntry.indexOf(':'));
				double fluidPotency = Double.valueOf(configEntry.substring(configEntry.indexOf(':') + 1));
				fluidPotencies.put(fluidName, fluidPotency);
			}
			getMTSFuels().put(fuelName, fluidPotencies);
		}
		config.save();
	}
}
