package zmaster587.mechanicalutilities.client.gui;

import zmaster587.mechanicalutilities.Tiles.TileLadderExtender;
import zmaster587.mechanicalutilities.container.ContainerLadderExtender;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiLadderExtender extends GuiContainer {

	private ResourceLocation backdrop = new ResourceLocation("mechanicalutilities","textures/gui/ladderExtender.png");
	private TileLadderExtender tileEntity;
	private EntityPlayer player;
	
	public GuiLadderExtender(InventoryPlayer playerInv, TileLadderExtender tile) {
		super(new ContainerLadderExtender(playerInv, tile));
		tileEntity = tile;
		this.player = playerInv.player;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRendererObj.drawString("Ladder Extender", 24,  4, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		this.mc.renderEngine.bindTexture(backdrop);
		
		int x = (width - xSize) / 2, y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, 176, 170);
		
	}
}
