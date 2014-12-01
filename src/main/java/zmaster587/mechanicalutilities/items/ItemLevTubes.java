package zmaster587.mechanicalutilities.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLevTubes extends ItemBlock {

	private final static String[] subNames = {
		"normal", "Glassy"
		};
	
	public ItemLevTubes(Block par1) {
		super(par1);
		setHasSubtypes(true);
		setUnlocalizedName("levTube");
	}
	
	@Override
	public int getMetadata (int damageValue) {
		return damageValue;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}
}
