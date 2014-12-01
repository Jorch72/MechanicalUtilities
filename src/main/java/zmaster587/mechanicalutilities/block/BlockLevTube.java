package zmaster587.mechanicalutilities.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import zmaster587.libVulpes.util.INetworkMachine;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Network.ChannelHandler;
import zmaster587.mechanicalutilities.Network.PacketMachine;
import zmaster587.mechanicalutilities.Tiles.TileLevTube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLevTube extends Block {

	private IIcon transpIcon;
	
	public BlockLevTube() {
		super(Material.iron);
		setLightLevel(0.3F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int damageDropped(int meta) {
		// TODO Auto-generated method stub
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		if(metadata == 0)
			return this.blockIcon;
		else
			return transpIcon;
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg)
    {
    	this.blockIcon = reg.registerIcon("mechanicalutilities:LevTubeWall");
    	this.transpIcon = reg.registerIcon("mechanicalutilities:LevTubeTrans");
    }
    
    @Override 
    public int getDamageValue(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z);
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < 2; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}
    
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		super.breakBlock(world, x, y, z, block, par6);
		//if(id != MechanicalUtilities.blockLevTube.blockID)
		//	return;
		
		if(world.isRemote)
			return;

		ForgeDirection majorDir = ForgeDirection.UNKNOWN, minorDir = ForgeDirection.UNKNOWN;

		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
			majorDir = ForgeDirection.VALID_DIRECTIONS[i];
			for(int g = 0; g < ForgeDirection.VALID_DIRECTIONS.length; g++) {

				if(i == g || ForgeDirection.OPPOSITES[i] == g)
					continue;

				minorDir = ForgeDirection.VALID_DIRECTIONS[g];
				if(world.getBlock(x + minorDir.offsetX - majorDir.offsetX,y + minorDir.offsetY - majorDir.offsetY, z + minorDir.offsetZ - majorDir.offsetZ) == MechanicalUtilities.blockLevTube) {
					for(int j = 0; j < 4; j++) {
						TileEntity tile;

						tile = world.getTileEntity(x + minorDir.offsetX - (j/2)*minorDir.offsetX*3 - (j%2)*majorDir.offsetX*3, y + minorDir.offsetY - (j/2)*minorDir.offsetY*3 - (j%2)*majorDir.offsetY*3, z + minorDir.offsetZ - (j/2)*minorDir.offsetZ*3 - (j%2)*majorDir.offsetZ*3);
						if(tile instanceof TileLevTube) {
							((TileLevTube)tile).runEnviromentCheck(world);
							
							ChannelHandler.sentToNearby(new PacketMachine((INetworkMachine) tile, (byte)100),world.provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord,64d);
							//PacketDispatcher.sendPacketToAllAround(x, y, z, 64, world.provider.dimensionId, new PacketMachine((INetworkMachine) tile, (byte)100).makePacket());
						}
					}
				}
			}
		}
	}

	/**
	 * Returns Returns true if the given side of this block type should be rendered (if it's solid or not), if the
	 * adjacent block is at the given coordinates. Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
	{
		ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
		return access.getBlock(x, y, z) != MechanicalUtilities.blockLevTube || access.getBlockMetadata(x + dir.offsetX , y + dir.offsetY, z +  + dir.offsetZ) == 0;
	}

	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int par5) {
		// majorDir: farthest offset
		// minorDir: nearer offset
		/*  
		 *  Y+
		 *   
		 *  00
		 * 0  0   X+
		 * 0  0
		 *  00
		 *  ^
		 *  majorDir: down | minorDir: West
		 */
		ForgeDirection majorDir = ForgeDirection.UNKNOWN, minorDir = ForgeDirection.UNKNOWN;

		outofloop:
			for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
				majorDir = ForgeDirection.VALID_DIRECTIONS[i];
				for(int g = 0; g < ForgeDirection.VALID_DIRECTIONS.length; g++) {

					if(i == g || ForgeDirection.OPPOSITES[i] == g)
						continue;

					minorDir = ForgeDirection.VALID_DIRECTIONS[g];

					if(world.getBlock(x - minorDir.offsetX, y - minorDir.offsetY, z - minorDir.offsetZ) == MechanicalUtilities.blockLevTube) {
						if(world.getBlock(x + minorDir.offsetX - majorDir.offsetX,y + minorDir.offsetY - majorDir.offsetY, z + minorDir.offsetZ - majorDir.offsetZ) == MechanicalUtilities.blockLevTube && 
								world.isAirBlock(x - majorDir.offsetX, y - majorDir.offsetY, z - majorDir.offsetZ)) {
							break outofloop;
						}
					}
				}
			}

		//Set these to min value
		int loopStartX = x, loopStartY = y,loopStartZ = z;
		if(majorDir.offsetX > 0 || majorDir.offsetY > 0 || majorDir.offsetZ > 0) {
			loopStartX -= 3*majorDir.offsetX;
			loopStartY -= 3*majorDir.offsetY;
			loopStartZ -= 3*majorDir.offsetZ;
		}
		else
			majorDir = majorDir.getOpposite();

		if(minorDir.offsetX < 0 || minorDir.offsetY < 0 || minorDir.offsetZ < 0) {
			loopStartX += minorDir.offsetX;
			loopStartY += minorDir.offsetY;
			loopStartZ += minorDir.offsetZ;
			minorDir = minorDir.getOpposite();
		}
		else {
			loopStartX -= 2*minorDir.offsetX;
			loopStartY -= 2*minorDir.offsetY;
			loopStartZ -= 2*minorDir.offsetZ;
		}

		for(int i=0; i <= 1; i++) {
			for(int g=0; g<=1; g++) {
				//System.out.println((loopStartX + (3*i*majorDir.offsetX) + (3*g*minorDir.offsetX)) + " " + (loopStartY + (3*i*majorDir.offsetY) + (3*g*minorDir.offsetY)) + " " + (loopStartZ + (3*i*majorDir.offsetZ) + (3*g*minorDir.offsetZ)));
				TileEntity tile = world.getTileEntity(loopStartX + (3*i*majorDir.offsetX) + (3*g*minorDir.offsetX), loopStartY + (3*i*majorDir.offsetY) + (3*g*minorDir.offsetY), loopStartZ + (3*i*majorDir.offsetZ) + (3*g*minorDir.offsetZ));
				if(tile instanceof TileLevTube) {
					((TileLevTube)tile).setCorner(i == 0 ? majorDir.getOpposite() : majorDir, g == 0 ? minorDir.getOpposite() : minorDir);
					((TileLevTube)tile).runEnviromentCheck(world);
				}
			}
		}
	}
}
