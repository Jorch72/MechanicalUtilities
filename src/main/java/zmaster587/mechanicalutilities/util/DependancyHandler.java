package zmaster587.mechanicalutilities.util;

import cpw.mods.fml.common.Loader;

public class DependancyHandler {
	public static void loadTile() {
		
	}
	
	public boolean isLoaded(String name)
	{
		return Loader.isModLoaded(name);
	}
}
