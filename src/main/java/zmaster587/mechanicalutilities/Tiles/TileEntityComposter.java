package zmaster587.mechanicalutilities.Tiles;

import java.util.HashSet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityComposter extends TileEntity implements IFluidHandler, ISidedInventory {

	private ItemStack output;
	private ItemStack inputStack;
	private FluidTank waterTank;
	private static HashSet<String> names = new HashSet<String>();

	private final int totalTime = 200;
	private final int amtFluid = 100;
	private final int minAmt = 4;
	private final short displayTime = 60;
	private short currentDisplayTime = 0;
	private byte input;
	private int processingTime;

	public static void init() {
		names.add("listAllveggie");
		names.add("listAllseed");
		names.add("listAllgrain");
		names.add("listAllfruit");
		names.add("listAllmeatraw");
		names.add("listAllmeatcooked");
		names.add("listAllherb");
		names.add("listAllmushroom");
		names.add("treeSapling");
		names.add("treeLeaves");
		names.add("cropWheat");
		names.add("cropPotato");
		names.add("cropCarrot");
	}


	public TileEntityComposter() {
		processingTime = -1;
		waterTank = new FluidTank(1000);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();

		writeToNBT(nbt);

		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("procesingTime",processingTime);

		nbt.setByte("input", input);

		if(output != null) {
			NBTTagCompound outnbt = new NBTTagCompound();
			output.writeToNBT(outnbt);
			nbt.setTag("output", outnbt);
		}

		waterTank.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		super.readFromNBT(nbt);

		processingTime = nbt.getInteger("procesingTime");

		input = nbt.getByte("input");
		if(nbt.hasKey("output"))
			output = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("output"));

		waterTank.readFromNBT(nbt);
	}

	public boolean isProcessing() {
		return processingTime != -1;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	public String[] infoToChat() {
		return new String[] {"Contains: " + input + "/4 compostable items", "Water: " + waterTank.getFluidAmount() + "mb", (isProcessing() ? "Time: " + ((totalTime-processingTime)/20) : "Not Processing")};
	}

	@SideOnly(Side.CLIENT)
	public void startDisplay() {
		currentDisplayTime = displayTime;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isDisplaying() {
		return currentDisplayTime > 0;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();

		if(worldObj.isRemote && isDisplaying()) {
			--currentDisplayTime;
		}
		
		if(isProcessing())
			processingTime++;

		if(worldObj.getWorldTime() % 60 == 0) {
			onInventoryUpdate();
		}

		if(processingTime > totalTime) {
			output = new ItemStack(Blocks.dirt);
			processingTime = -1;
			onInventoryUpdate();
		}
	}

	public static boolean isPlantMatter(ItemStack stack) {

		if(stack == null)
			return false;
		//First check VS hardcoded;

		Item item = stack.getItem();

		if(item == Items.apple || item == Items.egg || item == Items.rotten_flesh || item == Items.string || item == Items.bread || item == Items.carrot || item == Items.poisonous_potato || item == Items.potato || item == Item.getItemFromBlock(Blocks.pumpkin)||
				item == Item.getItemFromBlock(Blocks.melon_block) || item == Item.getItemFromBlock(Blocks.cactus) || item == Item.getItemFromBlock(Blocks.hay_block) || item == Item.getItemFromBlock(Blocks.vine) || item == Item.getItemFromBlock(Blocks.lit_pumpkin) ||
				item == Items.reeds || item == Item.getItemFromBlock(Blocks.grass) || item == Items.nether_wart)
			return true;

		
		 try {
			if(Class.forName("Reika.RotaryCraft.Items.ItemCanolaSeed").isInstance(item))
				return true;
		} catch (ClassNotFoundException e) {
			//Fail silently
		}

		int ids[] = OreDictionary.getOreIDs(stack);
		for(int id : ids) {
			String name = OreDictionary.getOreName(id);
			if(names.contains(name)) return true;
		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return (slot == 0) ? inputStack : output;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		if(slot == 1) {
			ItemStack temp = output;
			output = null;
			onInventoryUpdate();
			return temp;
		}
		
		
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(slot == 1)
			return output;
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {

		if(slot == 0)
			inputStack = stack;
		else if(slot == 1)
			output = stack;

		onInventoryUpdate();

	}

	public void onInventoryUpdate() {

		if(inputStack != null) {
			input += inputStack.stackSize;
			inputStack = null;
		}

		if(input >= minAmt && !isProcessing() && output == null && waterTank.getFluidAmount() >= amtFluid) {
			
			if(!worldObj.isRemote)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
			input -= minAmt;
			waterTank.drain(amtFluid, true);
			processingTime = 0;
			markDirty();
		}
		else if (!isProcessing() && this.blockMetadata != 0 && !worldObj.isRemote) {
			if(output != null)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 2);
			else
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
		}
	}

	@Override
	public String getInventoryName() {
		return "Composter";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(xCoord, yCoord, zCoord) < 64;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		return slot == 0 && isPlantMatter(item);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(ForgeDirection.getOrientation(side) == ForgeDirection.UP)
			return new int[] {0};
		else if(ForgeDirection.getOrientation(side) == ForgeDirection.DOWN)
			return new int[] {1};
		return null;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		return isItemValidForSlot(slot, item) && input < getInventoryStackLimit();
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item,	int side) {
		return slot == 1 && output != null;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return waterTank.fill(resource, doFill);
	}

	//Don't allow drain
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return from != ForgeDirection.UP && from != ForgeDirection.DOWN && fluid == FluidRegistry.WATER;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[] {waterTank.getInfo()};
	}

}
