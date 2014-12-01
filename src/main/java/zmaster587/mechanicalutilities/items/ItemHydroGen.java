package zmaster587.mechanicalutilities.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemHydroGen extends ItemBlock {

	private final static String[] subNames = {
		"Basic", "LV", "MV"
	};

	public ItemHydroGen(Block id) {
		super(id);
		setHasSubtypes(true);
		setUnlocalizedName("hydroPower");
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
