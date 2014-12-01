package zmaster587.mechanicalutilities.block;
import java.util.ArrayList;
import java.util.Random;

import zmaster587.mechanicalutilities.MechanicalUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSpaceLadder extends BlockLadder {

	public BlockSpaceLadder() {
		super();
	}

	@Override
	public int getRenderType()
	{
		//13,
		return 13;

	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
	{
		int tmp =  super.onBlockPlaced(world, x, y, z, side, hitx, hity, hitz, meta);

		if(tmp == meta)
		{
			if(world.getBlock(x, y-1, z) == this)
			{
				tmp = world.getBlockMetadata(x, y-1, z);
			}
			else
			{
				tmp = 2;
			}
		}

		return tmp;
	}
	

	
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3) {
		return MechanicalUtilities.itemSpaceLadder;
	}

	
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
    	float l = 0.05f;
    	int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0)
		{
			this.setBlockBounds(0.0f, 0.0f, 0.5f - l, 1.0f, 1.0f , 0.5f + l);
		}
		else if(meta == 1)
		{
			this.setBlockBounds(0.5f -l, 0.0f, 0.0f, 0.5f + l, 1.0f , 1.0f);
		}
    }


	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block){ 

		if(world.isAirBlock(x, y-1, z))// || (world.isBlockSolidOnSide(x, y-1, z, ForgeDirection.UP) && !(this.blockID == world.getBlockId(x, y-1, z)) ));
		{
			this.dropBlockAsItem(world, x, y, z,new ItemStack(MechanicalUtilities.itemSpaceLadder));
			world.setBlockToAir(x, y, z);
		}

	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return true;
	}
}
