package zmaster587.mechanicalutilities.Tiles;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;
import buildcraft.core.TileBuildCraft;

public class TileBaseBuildcraftExport extends TileEntity implements IPowerEmitter, IPipeConnection {

	@Override
	public boolean canEmitPowerFrom(ForgeDirection arg0) {
		return true;
	}

	@Override
	public ConnectOverride overridePipeConnection(PipeType pipe,
			ForgeDirection side) {
		if(pipe.POWER != null)
			return ConnectOverride.DEFAULT;
		else 
			return 	ConnectOverride.DISCONNECT;
	}

	public void sendPower(float amount, ForgeDirection[] dir, PowerHandler power) {
		ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();
		ArrayList<ForgeDirection> dirArr = new ArrayList<ForgeDirection>();
		for(ForgeDirection d : dir)
		{

			TileEntity tile = this.getWorldObj().getTileEntity(this.xCoord + d.offsetX,
					this.yCoord + d.offsetY,
					this.zCoord + d.offsetZ);

			if(tile instanceof IPowerReceptor 
					&& ((IPowerReceptor)tile).getPowerReceiver(d.getOpposite()) != null && (
							((IPowerReceptor)tile).getPowerReceiver(d.getOpposite()).getType() == PowerHandler.Type.PIPE
							|| ((IPowerReceptor)tile).getPowerReceiver(d.getOpposite()).getType() == PowerHandler.Type.MACHINE)
							&& ((IPowerReceptor)tile).getPowerReceiver(d.getOpposite()).powerRequest() > 0) {
				tiles.add(tile);
				dirArr.add(d);
			}
		}

		float totalSent = 0.0f;


		for(int i = 0; i < tiles.size(); i++)
		{
			if(tiles.get(i) instanceof IPowerReceptor 
					&& ((IPowerReceptor)tiles.get(i)).getPowerReceiver(dirArr.get(i).getOpposite()) != null)
			{
				double powerRequested = ((IPowerReceptor)tiles.get(i)).getPowerReceiver(dirArr.get(i).getOpposite()).powerRequest();
				if(powerRequested >= power.getActivationEnergy())
				{

					double amountSent = (amount/(float)tiles.size() > powerRequested) ? powerRequested : amount/(float)tiles.size();




					totalSent += amountSent;



					PowerReceiver receptor = (((IPowerReceptor) tiles.get(i)).getPowerReceiver(dirArr.get(i).getOpposite()));
					receptor.receiveEnergy(PowerHandler.Type.ENGINE, amountSent, dirArr.get(i).getOpposite());
					//receptor.update();
					//receptor.

					power.useEnergy(receptor.getMinEnergyReceived(), amountSent, true);
				}
			}
		}
	}
}
