package zmaster587.mechanicalutilities.client.render;

import org.lwjgl.opengl.GL11;

import zmaster587.mechanicalutilities.Tiles.TileEntityComposter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TESRTagRenderer extends TileEntitySpecialRenderer {

	private static final int maxDistSq = 1024;

	@Override
	public void renderTileEntityAt(TileEntity tile, double x,
			double y, double z, float delay) {

		if(!((TileEntityComposter)tile).isDisplaying())
			return;
		
		EntityLivingBase player = Minecraft.getMinecraft().thePlayer;

		double dist = tile.getDistanceFrom(player.posX, player.posY, player.posZ);//this.renderManager.livingPlayer);

		String displays[] = ((TileEntityComposter)tile).infoToChat();

		if (dist <= (double)(maxDistSq))
		{
			FontRenderer fontrenderer = this.func_147498_b();
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);

			float rotateX = (float)Math.atan2(Minecraft.getMinecraft().renderViewEntity.posY - tile.yCoord, tile.getDistanceFrom(player.posX, tile.yCoord, tile.zCoord));
			float rotateY = (360/(float)Math.PI) * (float)Math.atan2(Minecraft.getMinecraft().renderViewEntity.posY - tile.yCoord, tile.getDistanceFrom(player.posX, tile.yCoord, tile.zCoord));

			GL11.glRotatef(-this.field_147501_a.field_147562_h, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.field_147501_a.field_147563_i, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(-f1, -f1, f1);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);

			Tessellator tessellator = Tessellator.instance;
			byte b0 = 0;



			//GL11.glDisable(GL11.GL_TEXTURE_2D);

			for(int i = 0; i < displays.length; i++) {
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);

				tessellator.startDrawingQuads();
				GL11.glDepthMask(false);
				b0 = (byte)(10*(i - (displays.length/2)));

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_DEPTH_TEST);

				int j = fontrenderer.getStringWidth(displays[i]) / 2;
				tessellator.setColorRGBA_F(0.1F, 0.1F, 0.1F, 0.5F);
				tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
				tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
				tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
				tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
				tessellator.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);

				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				for(int g1 = 0; g1 < 10; g1++) 
					fontrenderer.drawString(displays[i], -fontrenderer.getStringWidth(displays[i]) / 2, b0, 0xd0d0d0FF);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(true);
				fontrenderer.drawString(displays[i], -fontrenderer.getStringWidth(displays[i]) / 2, b0, 0xffffffff);
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			GL11.glPopMatrix();
		}

	}

}
