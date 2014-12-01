package zmaster587.mechanicalutilities;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class NBTCraftingRecipe extends ShapedOreRecipe {

	NBTTagCompound outputNBT;
	
	public NBTCraftingRecipe(NBTTagCompound nbt, ItemStack result, Object... recipe) {
		super(result, recipe);
		
		outputNBT = nbt;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		ItemStack stack = super.getRecipeOutput();
		stack.setTagCompound(outputNBT);
		return stack;
	}

}
