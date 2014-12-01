package zmaster587.mechanicalutilities.container;

import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import zmaster587.libVulpes.util.INetworkMachine;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Network.ChannelHandler;
import zmaster587.mechanicalutilities.Network.PacketMachine;
import zmaster587.mechanicalutilities.Tiles.TileAdvWatermill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.common.util.ForgeDirection;



public class ContainerAdvWaterMill extends Container {

	protected TileAdvWatermill tile;
	private int waterAmt;

	public ContainerAdvWaterMill(InventoryPlayer playerInv,TileAdvWatermill te) {
		tile = te;

		if(!te.getWorldObj().isRemote && tile.hasLiquid()) {
			ChannelHandler.sendToPlayer(new PacketMachine(tile, (byte)0), playerInv.player);
			
			//PacketDispatcher.sendPacketToPlayer(new PacketMachine(tile, (byte)0).makePacket(), (Player) playerInv.player);
		}
	}
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return null;
	}

	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		ChannelHandler.sendToPlayer(new PacketMachine(tile, (byte)0), (EntityPlayer)iCrafting);
		
		//PacketDispatcher.sendPacketToPlayer(new PacketMachine(tile,(byte)0).makePacket(), (Player) iCrafting);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if(!tile.getWorldObj().isRemote && waterAmt != tile.getFill())
			for(int i = 0; i < this.crafters.size(); i++) {
				ICrafting iCrafting = (ICrafting)this.crafters.get(i);
				//iCrafting.sendProgressBarUpdate(this, 0, tile.getCurrentPower()*10000);
				
				ChannelHandler.sendToPlayer(new PacketMachine(tile, (byte)0), (EntityPlayer)iCrafting);
				
				//PacketDispatcher.sendPacketToPlayer(new PacketMachine(tile,(byte)0).makePacket(), (Player) iCrafting);
			}
		waterAmt = tile.getFill();
	}


	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {

		return true;
	}
}
