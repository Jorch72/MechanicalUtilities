package zmaster587.mechanicalutilities.items;

import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.block.BlockSpaceLadder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemSpaceLadder extends Item {

	public ItemSpaceLadder() {
		super();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
		if(movingobjectposition ==  null)
		{
			return itemStack;
		}
		else
		{
			if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
			{
				int x = movingobjectposition.blockX;
				int y = movingobjectposition.blockY;
				int z = movingobjectposition.blockZ;

				if(!world.canMineBlock(player, x, y, z))
					return itemStack;
				
				int top = y;
				
				for(int i = y; i < world.getHeight() -1 && !world.isAirBlock(x, i-1, z); i++)
				{
					top = i;
				}
						
				if(world.isAirBlock(x, top, z) && (world.isSideSolid(x, top-1, z, ForgeDirection.UP) || world.getBlock(x, top-1, z) == MechanicalUtilities.blockSpaceLadder))
				{
					
					world.setBlock(x, top, z, MechanicalUtilities.blockSpaceLadder, 0,3);
					
					
					if(!player.capabilities.isCreativeMode)
						itemStack.stackSize--;
				}
			}
		}
		return itemStack;
	}
}
