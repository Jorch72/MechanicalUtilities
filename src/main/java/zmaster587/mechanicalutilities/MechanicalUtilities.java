package zmaster587.mechanicalutilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumMap;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import ic2.api.item.IC2Items;
import buildcraft.BuildCraftSilicon;
import buildcraft.BuildCraftTransport;
import zmaster587.mechanicalutilities.Network.BasePacket;
import zmaster587.mechanicalutilities.Network.ChannelHandler;
import zmaster587.mechanicalutilities.Tiles.TileEntityComposter;
import zmaster587.mechanicalutilities.Tiles.TileStoneSeperator;
import zmaster587.mechanicalutilities.Tiles.TileAdvWatermill;
import zmaster587.mechanicalutilities.Tiles.TileHydroElectricGen;
import zmaster587.mechanicalutilities.Tiles.TileLevTube;
import zmaster587.mechanicalutilities.block.BlockAdvWaterMill;
import zmaster587.mechanicalutilities.block.BlockComposter;
import zmaster587.mechanicalutilities.block.BlockHydroElecticGen;
import zmaster587.mechanicalutilities.block.BlockLadderExtender;
import zmaster587.mechanicalutilities.block.BlockLevTube;
import zmaster587.mechanicalutilities.block.BlockLevTubeController;
import zmaster587.mechanicalutilities.block.BlockSpaceLadder;
import zmaster587.mechanicalutilities.block.GregMachineBlock;
import zmaster587.mechanicalutilities.block.TestBCBlock;
import zmaster587.mechanicalutilities.container.HandlerGui;
import zmaster587.mechanicalutilities.items.ItemDetectionRod;
import zmaster587.mechanicalutilities.items.ItemHydroGen;
import zmaster587.mechanicalutilities.items.ItemLevTubes;
import zmaster587.mechanicalutilities.items.ItemSpaceLadder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=MechanicalUtilities.MOD_ID, name=MechanicalUtilities.MOD_NAME, version="0.0.7a",dependencies="after:IC2@[2.0.394,);after:gregtech")
public class MechanicalUtilities {

	public static final String MOD_NAME = "MechanicalUtilities";
	public static final String MOD_ID = "mechanicalUtilities";
	public static final String MOD_CHANNEL = MOD_ID;


	public static Block blockSpaceLadder;
	public static Block ladderExtender;
	public static Block blockAdvWaterMill;
	public static Block blockHydroElectric;
	public static Block blockLevTube;
	public static Block blockLevTubeController;
	public static Block blockGTMachines;
	public static Block blockComposter;

	public static CreativeTabs tabMechUtils = new CreativeTabs("mechutils") {
		@Override
		public Item getTabIconItem() {
			return itemSpaceLadder;
		}
	};

	public static Item itemSpaceLadder, itemDetectionRod;

	public static StaticConfiguration staticConfig;

	@SidedProxy(clientSide="zmaster587.mechanicalutilities.client.ClientProxy", serverSide="zmaster587.mechanicalutilities.CommonProxy")
	public static CommonProxy proxy;

	@Instance(value = "mechanicalUtilities")
	public static MechanicalUtilities instance;

	public static ChannelHandler channelHandler;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		int pneumaticBatteryId, spaceLadderId, spaceLadderItemId, ladderExtenderId, ladderExtenderItemId;
		float pnuematicBatteryEmptyMJ, pnuematicDiamondAddMJ;
		ChannelHandler.init();

		config.load();
		staticConfig = new StaticConfiguration(config);

		ladderExtender = new BlockLadderExtender().setBlockName("Ladder Extender").setHardness(0.5f)
				.setCreativeTab(tabMechUtils);
		blockSpaceLadder = new BlockSpaceLadder().setBlockTextureName("mechanicalutilities:ladder").setHardness(0.1f).setBlockName("Reinforced Ladder TechBock");
		blockLevTube = new BlockLevTube().setBlockName("levTube").setCreativeTab(tabMechUtils).setHardness(0.5f);
		blockLevTubeController = new BlockLevTubeController().setBlockName("levTubeController").setCreativeTab(tabMechUtils).setHardness(0.5f).setBlockTextureName("mechanicalutilities:LevController");

		blockComposter = new BlockComposter(Material.wood).setCreativeTab(tabMechUtils).setBlockName("composter").setHardness(2).setResistance(5f);
		GameRegistry.registerBlock(blockComposter, "Composter");
		
		//blockGTMachines = new GregMachineBlock().setBlockName("idk").setCreativeTab(tabMechUtils);

		itemSpaceLadder = new ItemSpaceLadder().setTextureName("mechanicalutilities:ladder").setUnlocalizedName("Reinforced Ladder").setMaxStackSize(32).setCreativeTab(MechanicalUtilities.tabMechUtils);

		itemDetectionRod = new ItemDetectionRod().setCreativeTab(tabMechUtils).setUnlocalizedName("detectionrod").setTextureName("mechanicalutilities:prospectors").setMaxDamage(100).setMaxStackSize(1).setNoRepair();

		//Things dependent on IC2
		if(Loader.isModLoaded("IC2") && staticConfig.watermills) {
			blockAdvWaterMill = new BlockAdvWaterMill(config.get(Configuration.CATEGORY_GENERAL, "Advanced_watermill(MAX_OUTPUT_eu_per_t)","5").getInt(),
					config.get(Configuration.CATEGORY_GENERAL, "Advanced_watermill(Drain_per_eu_milibuckets)", 20).getInt()).setBlockTextureName("mechanicalutilities:AdvWaterMill").setHardness(0.5f).setBlockName("advWaterMill").setCreativeTab(tabMechUtils);

			blockHydroElectric = new BlockHydroElecticGen().setBlockName("hydroGen").setCreativeTab(tabMechUtils).setHardness(0.5f);


			GameRegistry.registerBlock(blockAdvWaterMill,"advWaterMill");
			GameRegistry.registerBlock(blockHydroElectric, ItemHydroGen.class, "hydroGen");
			GameRegistry.registerTileEntity(TileAdvWatermill.class, "TileWaterMill");
			GameRegistry.registerTileEntity(TileHydroElectricGen.class, "hydroGen");
		}

		config.save();

		GameRegistry.registerBlock(blockSpaceLadder, "Space Ladder");

		GameRegistry.registerBlock(ladderExtender, "Ladder Extender");
		GameRegistry.registerBlock(blockLevTube, ItemLevTubes.class,"levTube");
		GameRegistry.registerBlock(blockLevTubeController, "levTubeController");
		GameRegistry.registerTileEntity(zmaster587.mechanicalutilities.Tiles.TileLadderExtender.class, "LadderExtender");
		GameRegistry.registerTileEntity(TileLevTube.class, "levTube");
		GameRegistry.registerTileEntity(TileEntityComposter.class, "composter");


		//Items
		GameRegistry.registerItem(itemSpaceLadder, "Space Ladder Item");
		GameRegistry.registerItem(itemDetectionRod, "Detection Rod");


	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.registerRenderers();

		LanguageRegistry.addName(ladderExtender, "Ladder Extender");
		LanguageRegistry.addName(blockSpaceLadder, "Reinforced Ladder");
		LanguageRegistry.addName(itemSpaceLadder, "Reinforced Ladder");
		LanguageRegistry.instance().addStringLocalization("itemGroup.mechutils", "en_US", "Mechanical Utilites");

		if(Loader.isModLoaded("IC2") && staticConfig.watermills) {
			//LanguageRegistry.addName(blockAdvWaterMill, "Advanced Watermill");
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockAdvWaterMill), "ixi","pwp"," b ", 'i', Blocks.iron_bars, 'x', IC2Items.getItem("pump") ,'p', IC2Items.getItem("waterMill"), 'w', "plateIron", 'b', IC2Items.getItem("reBattery")));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHydroElectric, 1,0), " a ","aba"," a ", 'a', "plateDenseBronze", 'b', IC2Items.getItem("waterMill")));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHydroElectric, 1,1), "cac","cbc","cac", 'a', "plateDenseBronze",'b', IC2Items.getItem("lvTransformer"), 'c', new ItemStack(blockHydroElectric, 1,0)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHydroElectric, 1,2), "cac","cbc","cac", 'a', "plateDenseBronze",'b', IC2Items.getItem("mvTransformer"), 'c', new ItemStack(blockHydroElectric, 1,1)));
		}

		//IC2 recipies
		if(StaticConfiguration.ic2Recipies && Loader.isModLoaded("IC2")) {
			GameRegistry.addShapedRecipe(new ItemStack(blockLevTube,8), "xxx", "yzy", "xxx", 'x', IC2Items.getItem("constructionFoamPowder"), 'y', IC2Items.getItem("insulatedTinCableItem"), 'z', IC2Items.getItem("coil"));
			GameRegistry.addShapedRecipe(new ItemStack(blockLevTube,8,1), "xxx", "yzy", "xxx", 'x', IC2Items.getItem("reinforcedGlass"), 'y', IC2Items.getItem("insulatedTinCableItem"), 'z', IC2Items.getItem("coil"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockLevTubeController,4), "xxx", "yzy", "xxx", 'x', IC2Items.getItem("electrolyzedWaterCell"), 'y', IC2Items.getItem("coil"), 'z', "circuitBasic"));
			
		} 
		else { //vanilla
			GameRegistry.addShapedRecipe(new ItemStack(blockLevTube,4), "xyx", "ywy","xyx", 'x', Blocks.stone, 'y', Blocks.iron_bars, 'w', Items.gold_ingot);
			GameRegistry.addShapedRecipe(new ItemStack(blockLevTube,4,1), "xyx", "ywy","xyx", 'x', Blocks.glass, 'y', Blocks.iron_bars, 'w', Items.gold_ingot);
			GameRegistry.addShapedRecipe(new ItemStack(blockLevTubeController,4), "xzx", "ywy","xzx", 'x', new ItemStack(blockLevTube), 'y', Blocks.redstone_torch, 'w', Items.diamond, 'z', Items.gold_ingot);
		}

		GameRegistry.addRecipe(new ItemStack(ladderExtender), "x x","yyy","zzz", 'x', new ItemStack(Items.redstone), 'y', new ItemStack(Blocks.dispenser), 'z', new ItemStack(Items.iron_ingot));

		GameRegistry.addRecipe(new ItemStack(itemSpaceLadder), "xyx", 'x', new ItemStack(Items.iron_ingot), 'y', new ItemStack(Blocks.ladder));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockComposter), "L L", "L L", "LSL", 'L', "logWood", 'S', "slabWood"));

		//Adding Sticks

		((ItemDetectionRod)itemDetectionRod).registerRecipies();

		if(Loader.isModLoaded("gregtech") && staticConfig.gtStoneDustinator) {
			GT_ModHandler.addCraftingRecipe(new TileStoneSeperator(10000, "mechutils.tier.01", "Stone Seperator",1).getStackForm(1L), new Object[] { "TCT", "WMW", "TRT", Character.valueOf('M'), ItemList.Hull_LV, Character.valueOf('T'), ItemList.Rotor_LV, Character.valueOf('R'), ItemList.Robot_Arm_LV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Tin) });
			GT_ModHandler.addCraftingRecipe(new TileStoneSeperator(10001, "mechutils.tier.02", "Stone Seperator II",2).getStackForm(1L), new Object[] { "TCT", "WMW", "TRT", Character.valueOf('M'), ItemList.Hull_MV, Character.valueOf('T'), ItemList.Rotor_MV, Character.valueOf('R'), ItemList.Robot_Arm_MV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Copper) });
			GT_ModHandler.addCraftingRecipe(new TileStoneSeperator(10002, "mechutils.tier.03", "Stone Seperator III",3).getStackForm(1L), new Object[] { "TCT", "WMW", "TRT", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('T'), ItemList.Rotor_HV, Character.valueOf('R'), ItemList.Robot_Arm_HV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Data), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Gold) });
			GT_ModHandler.addCraftingRecipe(new TileStoneSeperator(10003, "mechutils.tier.04", "Stone Seperator (Fast, ain't it)",4).getStackForm(1L), new Object[] { "TCT", "WMW", "TRT", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('T'), ItemList.Rotor_EV, Character.valueOf('R'), ItemList.Robot_Arm_EV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Aluminium) });
			GT_ModHandler.addCraftingRecipe(new TileStoneSeperator(10004, "mechutils.tier.05", "Stone Seperator (Gone to Plaid)",5).getStackForm(1L), new Object[] { "TCT", "WMW", "TRT", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('T'), ItemList.Rotor_IV, Character.valueOf('R'), ItemList.Robot_Arm_IV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Ultimate), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Tungsten) });
		}


		NetworkRegistry.INSTANCE.registerGuiHandler(this, new HandlerGui());
	}

	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{	
		TileEntityComposter.init();
		if(Loader.isModLoaded("gregtech"))
			TileStoneSeperator.initOreStacks();
	}
}