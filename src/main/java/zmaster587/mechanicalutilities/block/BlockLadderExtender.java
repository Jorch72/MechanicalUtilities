package zmaster587.mechanicalutilities.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Tiles.TileLadderExtender;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLadderExtender extends Block {

	//
	
	private IIcon top, topRot, side, front, bottom;

	public BlockLadderExtender() {
		super(Material.iron);
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int metadata, float arg0, float arg1, float arg2) {

		TileEntity tile = world.getTileEntity(x, y, z);
		if(player.isSneaking())
		{
			if((world.getBlockMetadata(x, y, z) & 4) == 0)
			{
				world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) ^ 1, 3);
				return false;
			}
			else
				return false;
		}
		if(tile == null || !(tile instanceof TileLadderExtender))
			return false;
		


		if(!world.isRemote)
			player.openGui(MechanicalUtilities.instance, 0, world, x, y, z);

		return true;
	}
	
	
	//meta bit 3 represents on/off state
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
			int z, Block sendingBlock) {
		
		boolean newFlag = world.isBlockIndirectlyGettingPowered(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		
		
		TileEntity tile = world.getTileEntity(x, y, z);
		
		//If power is on
		if(newFlag)
		{
			world.setBlockMetadataWithNotify(x, y, z, meta | 4, 7);
			
			if(tile instanceof TileLadderExtender)
			{
				((TileLadderExtender) tile).extend(world, x, y, z);
			}
		}
		else
		{
			world.setBlockMetadataWithNotify(x, y, z, (meta & ~4) , 7);
			if(tile instanceof TileLadderExtender)
			{
				((TileLadderExtender) tile).retract(world, x, y, z);
			}
		}
	}

	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileLadderExtender();
	}

	@Override
	public void breakBlock(World world,int x, int y, int z, Block oldBlock, int meta)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof IInventory))
			return;

		IInventory inv = (IInventory)tile;

		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack item = inv.getStackInSlot(i);

			if(item != null && item.stackSize > 0)
			{
				EntityItem entItem = new EntityItem(world, x,y + 0.4f,z, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if(item.hasTagCompound())
				{
					entItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				world.spawnEntityInWorld(entItem);
				item.stackSize = 0;
			}
		}

		super.breakBlock(world, x, y, z, oldBlock, meta);
	}

	@Override
	public boolean hasTileEntity(int meta)
	{
		return true;
	}

	
	//Meta bits 0 and 1 represent 90* Y and 180* X respectively
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		//TODO: allow it to be up-side-down
		if(ForgeDirection.getOrientation(side) == ForgeDirection.UP)
		{
			return ((1 & metadata) == 1) ? this.topRot : this.top;
		}
		else if (ForgeDirection.getOrientation(side) == ForgeDirection.DOWN)
			return this.bottom;
		else if(ForgeDirection.getOrientation(side) == ForgeDirection.NORTH || ForgeDirection.getOrientation(side) == ForgeDirection.SOUTH)
		{
			return ((1 & metadata) == 1) ? this.side : this.front;
		}
		else
		{
			return ((1 & metadata) == 1) ? this.front : this.side;
		}

	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconReg)
	{
		this.top = iconReg.registerIcon("mechanicalutilities:LadderExtenderTop0");
		this.topRot = iconReg.registerIcon("mechanicalutilities:LadderExtenderTop1");
		this.front = iconReg.registerIcon("mechanicalutilities:LadderExtenderFront");
		this.side = iconReg.registerIcon("mechanicalutilities:LadderExtenderSide");
		this.bottom = iconReg.registerIcon("mechanicalutilities:LadderExtenderBottom");
	}
}
