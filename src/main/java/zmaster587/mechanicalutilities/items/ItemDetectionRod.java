package zmaster587.mechanicalutilities.items;

import java.util.ArrayList;
import java.util.List;

import sun.security.action.GetLongAction;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.NBTCraftingRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemDetectionRod extends Item {



	public ItemDetectionRod() {
		super();
	}

	//Range of detection
	private final int RANGE = 20;

	private enum detectedOres {
		COPPER,
		TIN,
		IRON,
		GOLD,
		SILVER,
		LEAD,
		NICKEL
	}

	public void registerRecipies() {
		NBTTagCompound level = new NBTTagCompound();

		for(int i = 0; i < detectedOres.values().length; i++) {
			level.setByte("level", (byte)i);

			char[] oreType = detectedOres.values()[i].name().toLowerCase().toCharArray();

			oreType[0] = Character.toUpperCase(oreType[0]);


			GameRegistry.addRecipe(new NBTCraftingRecipe((NBTTagCompound)level.copy(), new ItemStack(this), " xy", " y ", "yx ", 'y', Items.stick, 'x' ,"dust" + String.valueOf(oreType)));
		}

		level.setByte("level", (byte)0x80);
		GameRegistry.addRecipe(new NBTCraftingRecipe((NBTTagCompound)level.copy(), new ItemStack(this), " xy", " y ", "yx ", 'y', Items.gold_ingot, 'x' , Items.redstone));

	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int damage) {
		// TODO Auto-generated method stub
		if(stack.getTagCompound() == null || stack.getTagCompound().getByte("level") >> 7 == -1)
			return 0xFFF475;
		return super.getColorFromItemStack(stack, damage);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player,
			List list, boolean p_77624_4_) {
		super.addInformation(stack, player, list, p_77624_4_);

		if(stack.getTagCompound() != null) {
			byte level = stack.getTagCompound().getByte("level");
			list.add("Detects: " +  detectedOres.values()[level & 0x7F].name().toLowerCase());
			if(level >> 7 == -1)
				list.add("Configurable");
		}
		else
			list.add("Configurable");
	}


	@Override
	public ItemStack onItemRightClick(ItemStack stack,World world, EntityPlayer player) {
		super.onItemRightClick(stack, world,player);

		
		
		if(player.isSneaking()) {
			byte level = (stack.getTagCompound() == null) ? (byte)0x80 : stack.getTagCompound().getByte("level");
			if(level >> 7 == -1) {
				level = (byte) ((level & 0x7F) + 1);

				if(level >= detectedOres.values().length)
					level = 0;
				if(world.isRemote)
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((new ChatComponentText("Now detects " + detectedOres.values()[level].name().toLowerCase())));

				level = (byte)(level  | 0x80);

				if(stack.getTagCompound() == null)
					stack.setTagCompound(new NBTTagCompound());

				stack.getTagCompound().setByte("level", level);
			}
		}
		else 
		{
			player.swingItem();
			stack.damageItem(1, player);
			if(player.worldObj.isRemote) {

				final byte level = (stack.getTagCompound() == null) ? -1 : (byte)(stack.getTagCompound().getByte("level") & 0x7F);

				if(level != -1) {



					/*
					 * Level 1: can detect only if copper is nearby
					 * Level 2: can only detect if tin is nearby
					 * Level 3: can only detect if iron is nearby
					 */
					//Make world pointer final to run on separate thread
					final World worldfin = world;
					final int x1 = (int)player.posX ,y1=(int)player.posY,z1=(int)player.posZ;
					Runnable scanner = new Runnable() {
						@Override
						public void run() {
							for(int i = -RANGE; i < RANGE; i++) {
								for(int j = -RANGE; j < RANGE; j++) {
									for(int k = -RANGE; k < RANGE; k++) {
										ArrayList<ItemStack> items = worldfin.getBlock(x1+i, y1+j, z1+k).getDrops(worldfin, x1+i, y1+j, z1+k, worldfin.getBlockMetadata(x1+i, y1+j, z1+k), 0);

										for(ItemStack stack : items) {
											int id = OreDictionary.getOreID(stack);
											if(id != -1 && OreDictionary.getOreName(id).toUpperCase().contains(detectedOres.values()[level].name())) {
												Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((new ChatComponentText("The Prospector's Pick hints there is " + detectedOres.values()[level].name().toLowerCase() + " within 20 blocks of you!")));
												Thread.currentThread().interrupt();
												return;
											}
										}
									}
								}
							}
							Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((new ChatComponentText("The Prospector's Pick indicates there is nothing nearby it can find.")));
						}
					};

					new Thread(scanner).start();
				}
			}
		}
		return stack;
	}

}
