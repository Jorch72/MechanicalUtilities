package zmaster587.mechanicalutilities.Tiles;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import zmaster587.libVulpes.util.INetworkMachine;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileLevTube extends TileEntity implements INetworkMachine {

	private boolean complete;
	private ForgeDirection minorDir, majorDir, motionDir;


	public TileLevTube() {
		complete = false;
		minorDir = ForgeDirection.UNKNOWN;
		majorDir = ForgeDirection.UNKNOWN;
		motionDir = ForgeDirection.UNKNOWN;
	}

	@Override
	public void updateEntity() {
		if(!complete) 
			return;
		
		ArrayList<Entity> entities = (ArrayList<Entity>) worldObj.getEntitiesWithinAABB(Entity.class, getAffectedArea());

		
		for(Entity e : entities) {
			//e.addVelocity(motionDir.offsetX*10, motionDir.offsetY*10 + 1, motionDir.offsetZ*10);
			e.motionX = Math.max(Math.min(e.motionX + Math.signum(e.motionX)*0.2*motionDir.offsetX, 2),-2);
			e.motionY = Math.max(Math.min(e.motionY + Math.signum(e.motionY)*0.2*motionDir.offsetY, 1),-1);
			e.motionZ = Math.max(Math.min(e.motionZ - Math.signum(e.motionZ)*0.2*motionDir.offsetZ, 2),-2);
			e.fallDistance = 0;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		for(int i=0; i < ForgeDirection.values().length; i++) {
			if(ForgeDirection.values()[i] == majorDir)
				nbt.setInteger("majorDir", i);
			if(ForgeDirection.values()[i] == minorDir)
				nbt.setInteger("minorDir", i);
		}
		
		/*nbt.setInteger("majorDir", majorDir.ordinal());
		nbt.setInteger("majorDir", minorDir.ordinal());*/
		nbt.setBoolean("complete", complete);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		setCorner(ForgeDirection.getOrientation(nbt.getInteger("majorDir")),ForgeDirection.getOrientation(nbt.getInteger("minorDir")));
		
		complete = nbt.getBoolean("complete");
	}


	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		for(int i=0; i < ForgeDirection.values().length; i++) {
			if(ForgeDirection.values()[i] == majorDir)
				nbt.setInteger("majorDir", i);
			if(ForgeDirection.values()[i] == minorDir)
				nbt.setInteger("minorDir", i);
		}
		
		nbt.setBoolean("complete", complete);

		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0,nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound nbt = pkt.func_148857_g();
		
		setCorner(ForgeDirection.getOrientation(nbt.getInteger("majorDir")),ForgeDirection.getOrientation(nbt.getInteger("minorDir")));
		complete = nbt.getBoolean("complete");
	}

	
	@SideOnly(Side.CLIENT)
	public boolean renderConnectionOnSide(int side) {
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		
		Block block = worldObj.getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
		
		if(block == MechanicalUtilities.blockLevTubeController || block == MechanicalUtilities.blockLevTube)
			return true;
		
		return false;
	}
	
	public void setCorner(ForgeDirection major, ForgeDirection minor) {
		if(complete)
			return;
		majorDir=major;
		minorDir=minor;
		
		if(major.offsetX == 0 && minor.offsetX == 0)
			motionDir = ForgeDirection.EAST;
		else if(major.offsetY == 0 && minor.offsetY == 0)
			motionDir = ForgeDirection.UP;
		else
			motionDir = ForgeDirection.NORTH;
	}

	public ForgeDirection getMajorCorner() {
		return majorDir;
	}

	public ForgeDirection getMinorCorner() {
		return minorDir;
	}

	public boolean getComplete() { return complete; }

	public void runEnviromentCheck(World world) {
		boolean fullring = true;
		
		//Now to check if it's a loop
		ForgeDirection majorStartDir=majorDir, minorStartDir=minorDir;
		int loopStartX =xCoord, loopStartY=yCoord, loopStartZ=zCoord;

		if(minorDir.offsetX > 0 || minorDir.offsetY > 0 || minorDir.offsetZ > 0) {
			loopStartX -= 3*minorDir.offsetX;
			loopStartY -= 3*minorDir.offsetY;
			loopStartZ -= 3*minorDir.offsetZ;
		}
		else
			minorStartDir = minorDir.getOpposite();

		if(majorDir.offsetX > 0 || majorDir.offsetY > 0 || majorDir.offsetZ > 0) {
			loopStartX -= 3*majorDir.offsetX;
			loopStartY -= 3*majorDir.offsetY;
			loopStartZ -= 3*majorDir.offsetZ;
		}
		else
			majorStartDir = majorDir.getOpposite();


		//Make sure no other energy input is active on this ring
		/*if(world.getBlockId(xCoord + majorDir.offsetX*-3, yCoord + majorDir.offsetY*-3, zCoord + majorDir.offsetZ*-3) == MechanicalUtilities.blockLevTubeController.blockID ||
				world.getBlockId(xCoord + minorDir.offsetX*-3, yCoord + minorDir.offsetY*-3, zCoord + minorDir.offsetZ*-3) == MechanicalUtilities.blockLevTubeController.blockID ||
				world.getBlockId(xCoord + majorDir.offsetX*-3 + minorDir.offsetX*-3, yCoord + majorDir.offsetY*-3 + minorDir.offsetY*-3, zCoord + majorDir.offsetZ*-3 + minorDir.offsetZ*-3) == MechanicalUtilities.blockLevTubeController.blockID) {
			return;
		}*/

		for(int i=1; i <= 2; i++) {
			if(!(world.getBlock(loopStartX + i*minorStartDir.offsetX, loopStartY + i*minorStartDir.offsetY, loopStartZ + i*minorStartDir.offsetZ) == MechanicalUtilities.blockLevTube &&
					world.getBlock(loopStartX + i*majorStartDir.offsetX, loopStartY + i*majorStartDir.offsetY, loopStartZ + i*majorStartDir.offsetZ) == MechanicalUtilities.blockLevTube &&
					world.getBlock(loopStartX + i*minorStartDir.offsetX + 3*majorStartDir.offsetX, loopStartY + i*minorStartDir.offsetY + 3*majorStartDir.offsetY, loopStartZ + i*minorStartDir.offsetZ + 3*majorStartDir.offsetZ) == MechanicalUtilities.blockLevTube &&
					world.getBlock(loopStartX + i*majorStartDir.offsetX + 3*minorStartDir.offsetX, loopStartY + i*majorStartDir.offsetY + 3*minorStartDir.offsetY, loopStartZ + i*majorStartDir.offsetZ + 3*minorStartDir.offsetZ) == MechanicalUtilities.blockLevTube)) {
				fullring = false;
			}
		}

		complete = fullring;
	}


	protected AxisAlignedBB getAffectedArea() {
		int xx = majorDir.offsetX + minorDir.offsetX, yy = majorDir.offsetY + minorDir.offsetY ,zz = majorDir.offsetZ + minorDir.offsetZ;
		return AxisAlignedBB.getBoundingBox(xCoord + (xx == 1 ? -3 : 1) - (xx == 0 ? 1 : 0), yCoord + (yy == 1 ? -3 : 1) - (yy == 0 ? 1 : 0), zCoord + (zz == 1 ? -3 : 1) - (zz == 0 ? 1 : 0), xCoord + (xx == -1 ? 3 : 1), yCoord + (yy == -1 ? 3 : 1) + (yy == 0 ? 1 : 0), zCoord + (zz == -1 ? 3 : 1));
	
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return getAffectedArea();
	}

	@Override
	public int getXCoord() {
		// TODO Auto-generated method stub
		return xCoord;
	}

	@Override
	public int getYCoord() {
		// TODO Auto-generated method stub
		return yCoord;
	}

	@Override
	public int getZCoord() {
		// TODO Auto-generated method stub
		return zCoord;
	}

	@Override
	public int getWorldId() {
		// TODO Auto-generated method stub
		return worldObj.provider.dimensionId;
	}

	@Override
	public void writeDataToNetwork(ByteBuf out, byte id) {
		if(id == 100)
			out.writeBoolean(complete);
	}

	@Override
	public void readDataFromNetwork(ByteBuf in, byte id,
			NBTTagCompound nbt) {
		if(id == 100 && worldObj.isRemote)
			nbt.setBoolean("Complete", in.readBoolean());

	}

	@Override
	public void useNetworkData(EntityPlayer player, Side side, byte id,
			NBTTagCompound nbt) {
		if(id == 100)
			complete = nbt.getBoolean("Complete");
	}
}