package zmaster587.mechanicalutilities.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import zmaster587.mechanicalutilities.CommonProxy;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Tiles.TileEntityComposter;
import zmaster587.mechanicalutilities.Tiles.TileLevTube;
import zmaster587.mechanicalutilities.block.BlockSpaceLadder;
import zmaster587.mechanicalutilities.client.render.RenderSpaceLadder;
import zmaster587.mechanicalutilities.client.render.RendererLevTubeBlock;
import zmaster587.mechanicalutilities.client.render.TESRTagRenderer;

public class ClientProxy extends CommonProxy {

	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileLevTube.class, new RendererLevTubeBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityComposter.class, new TESRTagRenderer());
	}
}
