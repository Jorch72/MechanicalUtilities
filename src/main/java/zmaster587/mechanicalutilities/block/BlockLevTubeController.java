package zmaster587.mechanicalutilities.block;

import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Tiles.TileLevTube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLevTubeController extends Block {

	public BlockLevTubeController() {
		super(Material.circuits);
		this.maxX=0.85;
		this.maxY=0.85;
		this.maxZ=0.85;
		this.minX=0.15;
		this.minY=0.15;
		this.minZ=0.15;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TileLevTube();
	}


	/*public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
	{
		return false;
	}*/

	@Override
	public boolean isOpaqueCube() {
		return false;
	};
	
	@Override
	public boolean hasTileEntity(int meta) { return true; }


	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int par5) {
		//Start at 2 b/c 0 and 1 are either equal or opposite
		for(int i = 2; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
			for(int g = 0; g < ForgeDirection.VALID_DIRECTIONS.length; g++) {
				ForgeDirection majorDir = ForgeDirection.VALID_DIRECTIONS[i],
						minorDir = ForgeDirection.VALID_DIRECTIONS[g];
				if(majorDir == minorDir || majorDir.getOpposite() == minorDir)
					continue;

				if(world.getBlock(x + majorDir.offsetX, y + majorDir.offsetY, z + majorDir.offsetZ) == MechanicalUtilities.blockLevTube &&
						world.getBlock(x + minorDir.offsetX, y + minorDir.offsetY, z + minorDir.offsetZ) == MechanicalUtilities.blockLevTube) {
					((TileLevTube)world.getTileEntity(x, y, z)).setCorner(majorDir.getOpposite(), minorDir.getOpposite());
					((TileLevTube)world.getTileEntity(x, y, z)).runEnviromentCheck(world);
					if(!((TileLevTube)world.getTileEntity(x, y, z)).getComplete())
						continue;
					return;
				}
			}
		}
	}
}
