package zmaster587.mechanicalutilities.Tiles;

import java.util.ArrayList;

import zmaster587.libVulpes.util.ZUtils;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.StaticConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.objects.GT_RenderedTexture;

public class TileStoneSeperator extends GT_MetaTileEntity_BasicMachine {

	private static ArrayList<ItemStack> outputDusts;
	private static ArrayList<ItemStack> rareDusts;
	private static ArrayList<ItemStack> constantDusts;
	
	public TileStoneSeperator(int aID, String aName, String aNameRegional, int aTier) {
		   super(aID, aName, aNameRegional, aTier, 1, "Extracting tiny bits of metals from dusts", 2, 9, "Centrifuge.png", "dustSeperation", new ITexture[] {new GT_RenderedTexture(new GTTextureCustom("OVERLAY_SIDE_DUSTSEPERATOR_ACTIVE")), new GT_RenderedTexture(new GTTextureCustom("OVERLAY_SIDE_DUSTSEPERATOR")), new GT_RenderedTexture(new GTTextureCustom("OVERLAY_FRONT_DUSTSEPERATOR_ACTIVE")), new GT_RenderedTexture(new GTTextureCustom("OVERLAY_FRONT_DUSTSEPERATOR")),  new GT_RenderedTexture(new GTTextureCustom("OVERLAY_TOP_DUSTSEPERATOR_ACTIVE")),  new GT_RenderedTexture(new GTTextureCustom("OVERLAY_TOP_DUSTSEPERATOR")),  new GT_RenderedTexture(new GTTextureCustom("OVERLAY_BOTTOM_DUSTSEPERATOR_ACTIVE")), new GT_RenderedTexture(new GTTextureCustom("OVERLAY_BOTTOM_DUSTSEPERATOR")) });
	}
	
	public static void initOreStacks() {
		   //register valid dusts
		   outputDusts = new ArrayList<ItemStack>();
		   constantDusts = new ArrayList<ItemStack>();
		   rareDusts = new ArrayList<ItemStack>();
		   
		   String outputType;
		   
		   switch(StaticConfiguration.stoneSeperator_outputType) {
		   case 0:
			   outputType = "dustTiny";
			   break;
		   case 1:
			   outputType = "dustSmall";
			   break;
		   default:
			   outputType = "dust";
		   }
				   
		   //Register uncommon dusts
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Carbon").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Sodium").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Silicon").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Phosphor").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Sulfur").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Potassium").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Calcium").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Manganese").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Iron").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Cobalt").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Copper").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Nickel").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Silver").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Zinc").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Tin").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Gold").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Lead").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Ruby").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Sapphire").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Lapis").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Sodalite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Coal").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Bauxite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Cinnabar").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Cooperite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Pyrite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Magnetite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Malachite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Cinnabar").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Cooperite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Pyrite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Magnetite").subList(0, 1));
		   outputDusts.addAll(OreDictionary.getOres(outputType + "Malachite").subList(0, 1));
		   
		   //register rare dusts
		   rareDusts.addAll(OreDictionary.getOres(outputType + "Uranium").subList(0, 1));
		   rareDusts.addAll(OreDictionary.getOres(outputType + "Antimony").subList(0, 1));
		   rareDusts.addAll(OreDictionary.getOres(outputType + "Tungsten").subList(0, 1));
		   rareDusts.addAll(OreDictionary.getOres(outputType + "Platinum").subList(0, 1));
		   rareDusts.addAll(OreDictionary.getOres(outputType + "Chrome").subList(0, 1));
		   rareDusts.addAll(OreDictionary.getOres(outputType + "Aluminium").subList(0, 1));
		   rareDusts.addAll(OreDictionary.getOres(outputType + "Lithium").subList(0, 1));
		   
		   //register constant dusts
		   constantDusts.addAll(OreDictionary.getOres(outputType + "Iron").subList(0, 1));
		   constantDusts.addAll(OreDictionary.getOres(outputType + "Tin").subList(0, 1));
		   constantDusts.addAll(OreDictionary.getOres(outputType + "Copper").subList(0, 1));
	}
	
	public TileStoneSeperator(String aName, int aTier,String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 2, 6, aGUIName, aNEIName);
	}
	

	@Override
	public int checkRecipe() {
		super.checkRecipe();
		ArrayList<ItemStack> ores = OreDictionary.getOres("dustStone");
		
		byte slot = -1;
		
		for(ItemStack stack : ores) {
			ItemStack inputItem = this.getAllInputs()[0];
			if(inputItem != null && stack.isItemEqual(inputItem)) {
				slot = 0;
				break;
			}
			inputItem = this.getAllInputs()[1];
			if(inputItem != null && stack.isItemEqual(inputItem)) {
				slot = 1;
				break;
			}
		}
		
		if(getAllInputs()[0] == null && getAllInputs()[1] != null) {
				getAllInputs()[0] = getAllInputs()[1];
				getAllInputs()[1] = null;			
		}
		
		if(slot != -1 && isOutputEmpty()) {

			//100% chance of getting certain dusts
			if(StaticConfiguration.stoneSeperator_alwaysGiveDust)
				this.mOutputItems[0] = constantDusts.get(this.getBaseMetaTileEntity().getRandomNumber(constantDusts.size())).copy();
			else if (this.mTier*StaticConfiguration.stoneSeperator_teirChanceMult >= this.getBaseMetaTileEntity().getRandomNumber(StaticConfiguration.stoneSeperator_normalDustChance))
				this.mOutputItems[0] = constantDusts.get(this.getBaseMetaTileEntity().getRandomNumber(constantDusts.size())).copy();
			
			for(int i = 1; i < this.mOutputItems.length; i++) {
				if(this.mTier*StaticConfiguration.stoneSeperator_teirChanceMult >= this.getBaseMetaTileEntity().getRandomNumber(StaticConfiguration.stoneSeperator_rareDustChance)) {
					this.mOutputItems[i] = rareDusts.get(this.getBaseMetaTileEntity().getRandomNumber(outputDusts.size())).copy();
				}
				else if(this.mTier*StaticConfiguration.stoneSeperator_teirChanceMult >= this.getBaseMetaTileEntity().getRandomNumber(StaticConfiguration.stoneSeperator_otherDustChance)) {
					
					this.mOutputItems[i] = outputDusts.get(this.getBaseMetaTileEntity().getRandomNumber(outputDusts.size())).copy();
				}
			}
			
			getAllInputs()[slot].stackSize--;
			
			if(getAllInputs()[slot].stackSize == 0) {
					getAllInputs()[slot] = null;
				
			}
			
			this.mEUt = (4 * (1 << this.mTier - 1) * (1 << this.mTier - 1));
			this.mMaxProgresstime = (128 / (1 << this.mTier - 1));
			return 2;
		}
		return 0;
	}
	
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
		return new TileStoneSeperator(this.mName, this.mTier, this.mDescription, this.mTextures, this.mGUIName, this.mNEIName);
	}
}
