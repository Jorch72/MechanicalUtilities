package zmaster587.mechanicalutilities.Tiles;

import zmaster587.mechanicalutilities.MechanicalUtilities;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;

public class GTTextureCustom implements IIconContainer, Runnable { 
	protected IIcon mIcon;
	protected String mIconName;

	public IIcon getIcon() { return this.mIcon; } 
	public IIcon getOverlayIcon() { return null; }

	public GTTextureCustom(String aIconName) {
		this.mIconName = aIconName;
		GregTech_API.sGTBlockIconload.add(this);
	}

	public void run()
	{
		this.mIcon = GregTech_API.sBlockIcons.registerIcon(MechanicalUtilities.MOD_ID + ":" + this.mIconName);
	}

	public ResourceLocation getTextureFile()
	{
		return TextureMap.locationBlocksTexture;
	}
}

