package zmaster587.mechanicalutilities.Tiles;

import org.lwjgl.Sys;

import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.items.ItemSpaceLadder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileLadderExtender extends TileEntity implements IInventory {

	private ItemStack[] inv;

	public TileLadderExtender()
	{
		inv = new ItemStack[1];
	}
	
	public void retract(World world, int x,int y,int z)
	{
		int finalAmt = 0;
		int tallestPt = 0;
		int invAmt = 0;
		if(inv[0] != null)
		{
			if(inv[0].getItem() != MechanicalUtilities.itemSpaceLadder)
				inv[0] = null;
			else
				invAmt = inv[0].stackSize;
		}
		//Hack to allow reinforced ladder to retain breaking behavior and let the extender function
		for(int i = 1; y+i < world.getActualHeight() && world.getBlock(x, i+ y, z) == MechanicalUtilities.blockSpaceLadder; i++)
		{
			tallestPt = i;
		}
		
		if(tallestPt > getInventoryStackLimit())
			return;
		
		for(int i = tallestPt; i > 0; i--)
		{
			world.setBlockToAir(x, y + i, z);
			
		}
		
		if(tallestPt + invAmt == 0)
			inv[0] = null;
		else
			inv[0] = new ItemStack(MechanicalUtilities.itemSpaceLadder, tallestPt + invAmt);
	}
	
	public void extend(World world, int x, int y, int z)
	{
		int finalAmt = 0;
		int invAmt = 0;
		if(inv[0] != null)
		{
			if(inv[0].getItem() != MechanicalUtilities.itemSpaceLadder)
				inv[0] = null;
			else
				invAmt = inv[0].stackSize;
		}
		for(int i = 1; world.isAirBlock(x, y + i, z) && i <= invAmt; i++)
		{
			world.setBlock(x, y + i, z, MechanicalUtilities.blockSpaceLadder, world.getBlockMetadata(x, y, z) & 1 ,3);
			finalAmt = i;
		}
		
		if(invAmt - finalAmt == 0)
		{
			inv[0] = null;
		}
		else
			inv[0].stackSize = invAmt - finalAmt;
	}
	
	@Override
	public void writeToNBT(net.minecraft.nbt.NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		NBTTagList list = new NBTTagList();

		NBTTagList itemList = new NBTTagList();
		for(int i = 0; i < inv.length; i++)
		{
			ItemStack stack = inv[i];
			if(stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}

		compound.setTag("Inventory", itemList);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compd)
	{
		super.readFromNBT(compd);

		NBTTagList tagList = compd.getTagList("Inventory",8);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(i >= inv.length)
			return null;
		else
			return inv[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack stack = getStackInSlot(i);
		if (stack != null) {
			if (stack.stackSize <= j) {
				setInventorySlotContents(i, null);
			} else {
				stack = stack.splitStack(j);
				if (stack.stackSize == 0) {
					setInventorySlotContents(i, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack stack = getStackInSlot(i);
		if (stack != null) {
			setInventorySlotContents(i, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inv[i] = itemstack;	
	}

	@Override
	public String getInventoryName() {
		return "Ladder Extender";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getDistanceFrom(player.posX, player.posY, player.posZ) <= 30 ? 
				true : false;
	}
	

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if(itemstack.getItem().equals(MechanicalUtilities.itemSpaceLadder))
			return true;
		else
			return false;
	}
	
	public boolean canUpdate() {return false;}

}
