package zmaster587.mechanicalutilities.client.gui;

import zmaster587.mechanicalutilities.Tiles.TileAdvWatermill;
import zmaster587.mechanicalutilities.container.ContainerAdvWaterMill;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAdvWaterMill  extends GuiContainer {
	
	private ResourceLocation backdrop = new ResourceLocation("mechanicalutilities","textures/gui/advWatermill.png");
	
	TileAdvWatermill tile;
	
	public GuiAdvWaterMill(InventoryPlayer playerInv, TileAdvWatermill te) {
		super(new ContainerAdvWaterMill(playerInv,te));
		
		tile = te;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		int x = (width - xSize) / 2, y = (height - ySize) / 2;
		
		fontRendererObj.drawString("Advanced watermill", 24,  4, 4210752);
		fontRendererObj.drawString("Current Output: ", 52, 20, 0xFF121212);
		fontRendererObj.drawString(tile.getCurrentOutput() + " EU/t", 52, 28, 0xFF121212);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		
		this.mc.renderEngine.bindTexture(backdrop);
		int x = (width - xSize) / 2, y = (height - ySize) / 2;
		float maxStorage, currentStorage;
		this.drawTexturedModalRect(x, y, 0, 0, 176, 170);

		this.drawTexturedModalRect(x + 30, y + 79 - tile.getFluidPercentFilledScaled(66), 176, 0, 18, tile.getFluidPercentFilledScaled(66));
	}
}