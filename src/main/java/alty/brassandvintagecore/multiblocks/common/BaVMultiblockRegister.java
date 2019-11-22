package alty.brassandvintagecore.multiblocks.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alty.brassandvintagecore.util.BaVMultiblockHandler;

public class BaVMultiblockRegister {
private static final Map<String, BaVMultiblockHandler> entries = new HashMap<String, BaVMultiblockHandler>(); 
	
	private BaVMultiblockRegister() {
		
	}
	
	public static void register(String name, BaVMultiblockHandler mb) {
		entries.put(name, mb);
	}
	
	public static BaVMultiblockHandler get(String name) {
		return entries.get(name);
	}

	public static List<String> keys() {
		List<String> keys = new ArrayList<String>();
		keys.addAll(entries.keySet());
		return keys;
	}
}
