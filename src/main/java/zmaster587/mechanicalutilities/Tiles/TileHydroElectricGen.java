package zmaster587.mechanicalutilities.Tiles;

import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.StaticConfiguration;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import ic2.api.energy.prefab.BasicSource;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IWrenchable;

public class TileHydroElectricGen extends TileEntity implements IEnergySource, IWrenchable {

	private BasicSource ic2Energy;
	private boolean valid;
	private int outputAmt;
	private int storedMeta;
	public TileHydroElectricGen() {
		
		ic2Energy = new BasicSource(this, 1000,1);
		valid = false;
	}

	public TileHydroElectricGen(int meta) {
		
		outputAmt=16*((4*meta)+1)*StaticConfiguration.hydroGenPower;
		
		ic2Energy = new BasicSource(this, outputAmt, Math.max(1, meta));
		storedMeta = meta;
	}

	public void updateContainingBlockInfo() {
		super.updateContainingBlockInfo();
		checkConditions(worldObj);
	}
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return direction.equals(ForgeDirection.UP);
	}

	@Override
	public double getOfferedEnergy() {
		// TODO Auto-generated method stub
		return Math.min(ic2Energy.getEnergyStored(), outputAmt);
	}

	@Override
	public void drawEnergy(double amount) {
		ic2Energy.drawEnergy(amount);
	}
	
	@Override
	public void invalidate() {
		ic2Energy.onInvalidate();
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		ic2Energy.onOnChunkUnload();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		valid = tag.getBoolean("valid");
		
		int meta = tag.getInteger("storedMeta");
		ic2Energy.setTier(Math.max(1, meta));
		ic2Energy.setCapacity(16*((4*meta)+1)*StaticConfiguration.hydroGenPower);
		outputAmt=16*((4*meta)+1)*StaticConfiguration.hydroGenPower;
		
		ic2Energy.onReadFromNbt(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setBoolean("valid", valid);
		tag.setInteger("storedMeta", storedMeta);
		ic2Energy.onWriteToNbt(tag);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(valid)
			ic2Energy.addEnergy(outputAmt);
		ic2Energy.onUpdateEntity();
	}
	
	public void checkConditions(World world) {
		if(world.getBiomeGenForCoords(this.xCoord, this.zCoord).equals(BiomeGenBase.river) && (
				(world.getBlock(xCoord, yCoord, zCoord -1) == Blocks.water && world.getBlock(xCoord, yCoord, zCoord +1) == Blocks.water) || 
				(world.getBlock(xCoord - 1, yCoord, zCoord) == Blocks.water && world.getBlock(xCoord + 1, yCoord, zCoord) == Blocks.water))) {
			valid = true;
		}
		else valid = false;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public short getFacing() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFacing(short facing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		// TODO Auto-generated method stub
		return 1F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		// TODO Auto-generated method stub
		return new ItemStack(MechanicalUtilities.blockHydroElectric, 1, this.blockMetadata);
	}

	@Override
	public int getSourceTier() {
		return 1;
	}
}
