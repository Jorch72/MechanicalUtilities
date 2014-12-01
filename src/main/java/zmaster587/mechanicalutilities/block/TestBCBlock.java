package zmaster587.mechanicalutilities.block;

import zmaster587.mechanicalutilities.Tiles.TileBaseBuildcraftExport;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TestBCBlock extends Block {

	public TestBCBlock(Material mat)
	{
		super(mat);
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
	
	
	public TileEntity createTileEntity(World world, int metadata)
	{
		System.out.print("it runs2");
		return new TileBaseBuildcraftExport();
	}
}
