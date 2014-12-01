package zmaster587.mechanicalutilities.Tiles;

import zmaster587.libVulpes.util.INetworkMachine;
import zmaster587.mechanicalutilities.MechanicalUtilities;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import ic2.api.energy.prefab.BasicSource;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IWrenchable;
import io.netty.buffer.ByteBuf;

public class TileAdvWatermill extends TileEntity implements IEnergySource, INetworkMachine, IFluidHandler, IWrenchable {

	private BasicSource ic2Energy = new BasicSource(this, 1000, 1);
	private FluidTank fluidTank = new FluidTank(1000);
	private int maxOutputAmount = 5;
	private int drainAmount = 20;

	public TileAdvWatermill() {

	}

	public TileAdvWatermill(int maxAmt) {
		this();
		maxOutputAmount = maxAmt;
	}

	public TileAdvWatermill(int maxAmt, int drainAmt) {
		this(maxAmt);
		drainAmount = drainAmt;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return true;//ic2Energy.emitsEnergyTo(receiver, direction);
	}

	@Override
	public double getOfferedEnergy() {
		// TODO Auto-generated method stub
		return ic2Energy.getOfferedEnergy();
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

	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		ic2Energy.onReadFromNbt(tag);
		fluidTank.readFromNBT(tag);
	}

	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		ic2Energy.onWriteToNbt(tag);
		fluidTank.writeToNBT(tag);
	}

	public void updateEntity() {
		super.updateEntity();
		ic2Energy.onUpdateEntity();

		if(!this.worldObj.isRemote && fluidTank.getFluidAmount() > drainAmount) {
			fluidTank.drain(drainAmount, true);

			ic2Energy.addEnergy(getCurrentOutput());
		}
	}

	@Override
	public int fill(ForgeDirection from, FluidStack fluid, boolean doFill) {
		
		if(fluid.isFluidEqual(new FluidStack(FluidRegistry.WATER,1)) && from == ForgeDirection.UP)
			return fluidTank.fill(fluid, doFill);
		
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		// TODO Auto-generated method stub
		return fluidTank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return fluidTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		
		//DEBUG:
		System.out.println(from == ForgeDirection.UP);
		
		return fluid.equals(FluidRegistry.WATER) && from == ForgeDirection.UP;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// TODO Auto-generated method stub
		FluidTankInfo[] ret = {fluidTank.getInfo()};
		return ret;
	}

	public int getFill() {
		return this.fluidTank.getFluidAmount();
	}

	public int getCurrentOutput() {
		return getFluidPercentFilledScaled(maxOutputAmount);
	}
	
	public int getFluidPercentFilledScaled(int scale) {
		return (int)(scale*(fluidTank.getFluidAmount()/(float)fluidTank.getCapacity()));
	}
	
	public boolean hasLiquid() {
		return this.fluidTank.getFluid() != null;
	}

	@Override
	public int getXCoord() {
		// TODO Auto-generated method stub
		return this.xCoord;
	}

	@Override
	public int getYCoord() {
		// TODO Auto-generated method stub
		return this.yCoord;
	}

	@Override
	public int getZCoord() {
		// TODO Auto-generated method stub
		return this.zCoord;
	}

	@Override
	public int getWorldId() {
		// TODO Auto-generated method stub
		return this.worldObj.provider.dimensionId;
	}

	@Override
	public void writeDataToNetwork(ByteBuf out, byte id) {

		if(id == 0) {
			out.writeInt(this.fluidTank.getFluidAmount());

		}
	}

	@Override
	public void readDataFromNetwork(ByteBuf in, byte packetId,
			NBTTagCompound nbt) {
		if(packetId == 0) {
			nbt.setInteger("amt", in.readInt());
		}
	}

	@Override
	public void useNetworkData(EntityPlayer player, Side side, byte id,
			NBTTagCompound nbt) {
		
		if(id == 0) {
			FluidStack stack = this.fluidTank.getFluid();
			int waterAmt = nbt.getInteger("amt");
			if(waterAmt == 0)
				stack = null;
			else if(stack == null) {
				stack = new FluidStack(FluidRegistry.WATER, waterAmt);
			}
			else
				stack.amount = waterAmt;
			
			this.fluidTank.setFluid(stack);
		}
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
		return new ItemStack(MechanicalUtilities.blockAdvWaterMill, 1, this.blockMetadata);
	}

	@Override
	public int getSourceTier() {
		return 1;
	}
}
