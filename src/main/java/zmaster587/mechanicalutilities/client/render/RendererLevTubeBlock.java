package zmaster587.mechanicalutilities.client.render;

import org.lwjgl.opengl.GL11;

import zmaster587.mechanicalutilities.Tiles.TileLevTube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;

public class RendererLevTubeBlock extends TileEntitySpecialRenderer {


	private final static ResourceLocation texture = new ResourceLocation("mechanicalutilities:textures/blocks/Connector.png");

	public RendererLevTubeBlock() {
	}

	public void drawCube(double radius, Tessellator tessellator) {
		//TOP
		tessellator.addVertex(radius, -radius, radius);
		tessellator.addVertex(radius, radius, radius);
		tessellator.addVertex(-radius, radius, radius);
		tessellator.addVertex(-radius, -radius, radius);

		//BOTTOM
		tessellator.addVertex(radius, radius, -radius);
		tessellator.addVertex(radius, -radius, -radius);
		tessellator.addVertex(-radius, -radius, -radius);
		tessellator.addVertex(-radius, radius, -radius);

		//EAST
		tessellator.addVertex(radius, -radius, -radius);
		tessellator.addVertex(radius, radius, -radius);
		tessellator.addVertex(radius, radius, radius);
		tessellator.addVertex(radius, -radius, radius);

		//SOUTH
		tessellator.addVertex(radius, -radius, -radius);
		tessellator.addVertex(radius, -radius, radius);
		tessellator.addVertex(-radius, -radius, radius);
		tessellator.addVertex(-radius, -radius, -radius);

		//WEST
		tessellator.addVertex(-radius, -radius, radius);
		tessellator.addVertex(-radius, radius, radius);
		tessellator.addVertex(-radius, radius, -radius);
		tessellator.addVertex(-radius, -radius, -radius);

		//NORTH
		tessellator.addVertex(radius, radius, radius);
		tessellator.addVertex(radius, radius, -radius);
		tessellator.addVertex(-radius, radius, -radius);
		tessellator.addVertex(-radius, radius, radius);
	}
	
	public void drawCubeUV(double radius, Tessellator tessellator) {
		//TOP
		tessellator.addVertexWithUV(radius, -radius, radius,1,0);
		tessellator.addVertexWithUV(radius, radius, radius,1,1);
		tessellator.addVertexWithUV(-radius, radius, radius,0,1);
		tessellator.addVertexWithUV(-radius, -radius, radius,0,0);

		//BOTTOM
		tessellator.addVertexWithUV(radius, radius, -radius,1,1);
		tessellator.addVertexWithUV(radius, -radius, -radius,1,0);
		tessellator.addVertexWithUV(-radius, -radius, -radius,0,0);
		tessellator.addVertexWithUV(-radius, radius, -radius,0,1);

		//EAST
		tessellator.addVertexWithUV(radius, -radius, -radius,0,0);
		tessellator.addVertexWithUV(radius, radius, -radius,1,0);
		tessellator.addVertexWithUV(radius, radius, radius,1,1);
		tessellator.addVertexWithUV(radius, -radius, radius,0,1);

		//SOUTH
		tessellator.addVertexWithUV(radius, -radius, -radius,1,0);
		tessellator.addVertexWithUV(radius, -radius, radius,1,1);
		tessellator.addVertexWithUV(-radius, -radius, radius,0,1);
		tessellator.addVertexWithUV(-radius, -radius, -radius,0,0);

		//WEST
		tessellator.addVertexWithUV(-radius, -radius, radius,0,1);
		tessellator.addVertexWithUV(-radius, radius, radius,1,1);
		tessellator.addVertexWithUV(-radius, radius, -radius,1,0);
		tessellator.addVertexWithUV(-radius, -radius, -radius,0,0);

		//NORTH
		tessellator.addVertexWithUV(radius, radius, radius,1,1);
		tessellator.addVertexWithUV(radius, radius, -radius,1,0);
		tessellator.addVertexWithUV(-radius, radius, -radius,0,0);
		tessellator.addVertexWithUV(-radius, radius, radius,0,1);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		Tessellator tessellator = Tessellator.instance;

		GL11.glPushMatrix();

		GL11.glTranslated(x + 0.5F, y + 0.5F, z + 0.5F);
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);


		/*tessellator.startDrawingQuads();

		drawCube(0.35D, tessellator);

		tessellator.draw();*/



		for(int i=0; i < 6; i++) {
			if(((TileLevTube)tileentity).renderConnectionOnSide(i)) {
				GL11.glPushMatrix();

				ForgeDirection dir = ForgeDirection.getOrientation(i);

				GL11.glTranslated(0.5*dir.offsetX, 0.5*dir.offsetY, 0.5*dir.offsetZ);

				tessellator.startDrawingQuads();

				//tessellator.setColorRGBA_F(0.1F, 0.1F, 0.95F, 1.0f);
				tessellator.setBrightness(16);
				
				bindTexture(texture);
				
				//for(int g=0; g < 8; g++) {
					drawCubeUV(0.25D, tessellator);
				//}

				tessellator.draw();

				GL11.glPopMatrix();
			}
		}

		//GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();

		if(!((TileLevTube)tileentity).getComplete())
			return;

		GL11.glPushMatrix();

		//GL11.glTranslated(x + 1, y + 1, z);
		ForgeDirection majDir = ((TileLevTube)tileentity).getMajorCorner();
		ForgeDirection minDir = ((TileLevTube)tileentity).getMinorCorner();

		int xx = majDir.offsetX + minDir.offsetX, yy = majDir.offsetY + minDir.offsetY ,zz = majDir.offsetZ + minDir.offsetZ;

		GL11.glTranslated(x + (xx == 0 ? 0 : 0.5) -xx*1.5, y + (yy == 0 ? 0 : 0.5) -yy*1.5, z + (zz == 0 ? 0 : 0.5) -zz*1.5);

		if(yy == 0)
			GL11.glRotated(-90, 1, 0, 0);
		else if(xx == 0)
			GL11.glRotated(90, 0, 1, 0);
		//GL11.glTranslated(-1, -1, 0);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(0.1F, 0.1F, 0.95F, 1.0f);
		tessellator.setBrightness(16);

		for(int i=0; i < 2; i++) {
			//EAST
			tessellator.addVertex(0.9, 0.9, 0);
			tessellator.addVertex(0.9, -0.9, 0);
			tessellator.addVertex(0.9, -0.9, 1);
			tessellator.addVertex(0.9, 0.9, 1);

			//SOUTH
			tessellator.addVertex(0.9, -0.9, 1);
			tessellator.addVertex(0.9, -0.9, 0);
			tessellator.addVertex(-0.9, -0.9, 0);
			tessellator.addVertex(-0.9, -0.9, 1);

			//WEST
			tessellator.addVertex(-0.9, 0.9, 1);
			tessellator.addVertex(-0.9, -0.9, 1);
			tessellator.addVertex(-0.9, -0.9, 0);
			tessellator.addVertex(-0.9, 0.9, 0);

			//NORTH
			tessellator.addVertex(0.9, 0.9, 0);
			tessellator.addVertex(0.9, 0.9, 1);
			tessellator.addVertex(-0.9, 0.9, 1);
			tessellator.addVertex(-0.9, 0.9, 0);
		}

		tessellator.draw();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
