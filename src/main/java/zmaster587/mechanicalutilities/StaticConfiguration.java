// Designed to store needed values



package zmaster587.mechanicalutilities;

import net.minecraftforge.common.config.Configuration;

public class StaticConfiguration {
	
	float defaultStorage, diamondMult;
	public static byte stoneSeperator_outputType;
	public static int hydroGenPower, stoneSeperator_rareDustChance, stoneSeperator_otherDustChance, stoneSeperator_normalDustChance;
	public static float stoneSeperator_teirChanceMult;
	public static boolean ic2Recipies, watermills, gtStoneDustinator, stoneSeperator_alwaysGiveDust;
	
	private final String CATEGORY_STONE_SEPERATOR = "Stone Seperator";
	
	public StaticConfiguration(Configuration config) {
		defaultStorage = Float.parseFloat(config.get(Configuration.CATEGORY_GENERAL, "pnuematicBatteryEmptyMJ", 10000.0f).getString());
		diamondMult = Float.parseFloat(config.get(Configuration.CATEGORY_GENERAL, "pnuematicDiamondAddMJ", 10000.0f).getString());
		hydroGenPower = config.get(Configuration.CATEGORY_GENERAL, "HydroEUMult", 1).getInt();
		watermills = config.get(Configuration.CATEGORY_GENERAL, "Enable-Watermills", false).getBoolean(false);
		ic2Recipies = config.get(Configuration.CATEGORY_GENERAL, "IC2-Recipies", false).getBoolean(false);
		
		gtStoneDustinator = config.getBoolean("StoneDustMachine",Configuration.CATEGORY_GENERAL, true, "Enables the Stone Dust to ore Machine");
		
		config.addCustomCategoryComment(CATEGORY_STONE_SEPERATOR, "Contains options to manipulate the Stone Seperator");
		
		stoneSeperator_alwaysGiveDust = config.getBoolean("alwaysGiveDust", CATEGORY_STONE_SEPERATOR, true, "If true a dust will always be given");
		stoneSeperator_teirChanceMult = config.getFloat("tierMuliplyer", CATEGORY_STONE_SEPERATOR, 1f, 1f, Float.MAX_VALUE, "higher numbers increase chances for dusts");
		stoneSeperator_normalDustChance = config.getInt("normalDustChance", CATEGORY_STONE_SEPERATOR, 50, 1, Integer.MAX_VALUE, "1/ chances to get a normal dust (only if alwaysGiveDust is set to false");
		stoneSeperator_rareDustChance = config.getInt("rareDustChance", CATEGORY_STONE_SEPERATOR, 400, 1, Integer.MAX_VALUE, "1/ chances to get a rare dust");
		stoneSeperator_otherDustChance = config.getInt("otherDustChance", CATEGORY_STONE_SEPERATOR, 200, 1, Integer.MAX_VALUE, "1/ chances to get an uncommon dust");
		stoneSeperator_outputType = (byte)config.getInt("outputType", CATEGORY_STONE_SEPERATOR, 1, 0, 2, "Control what size dusts are created; 0: tiny piles, 1: small piles, 2: dusts");
	}
	
	public float getDefaultStorage() { return defaultStorage; }
	
	public float getDiamondMult() { return diamondMult; }
	
}
