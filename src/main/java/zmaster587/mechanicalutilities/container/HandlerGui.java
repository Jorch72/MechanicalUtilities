package zmaster587.mechanicalutilities.container;

import zmaster587.mechanicalutilities.Tiles.TileAdvWatermill;
import zmaster587.mechanicalutilities.Tiles.TileLadderExtender;
import zmaster587.mechanicalutilities.client.gui.GuiAdvWaterMill;
import zmaster587.mechanicalutilities.client.gui.GuiLadderExtender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class HandlerGui implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if(tile instanceof TileLadderExtender)
		{
			return new ContainerLadderExtender(player.inventory, (TileLadderExtender) tile);
		}
		else if(tile instanceof TileAdvWatermill)
		{
			return new ContainerAdvWaterMill(player.inventory, (TileAdvWatermill) tile);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if(tile instanceof TileLadderExtender)
			return new GuiLadderExtender(player.inventory, (TileLadderExtender)tile);
		else if(tile instanceof TileAdvWatermill)
			return new GuiAdvWaterMill(player.inventory, (TileAdvWatermill) tile);
		return null;
	}

}
