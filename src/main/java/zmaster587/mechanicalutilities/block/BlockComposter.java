package zmaster587.mechanicalutilities.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import zmaster587.mechanicalutilities.Tiles.TileEntityComposter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class BlockComposter extends Block {

	IIcon top, sideTex, topFull, topEmpty;
	
	public BlockComposter(Material mat) {
		super(mat);
	}

	@Override
	public void registerBlockIcons(IIconRegister icons) {
		sideTex = icons.registerIcon("mechanicalutilities:ExteriorComposter");
		top = icons.registerIcon("mechanicalutilities:ExteriorTopStart");
		topEmpty = icons.registerIcon("mechanicalutilities:ExteriorTopempty");
		topFull = icons.registerIcon("mechanicalutilities:ExteriorTopdone");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		if(ForgeDirection.getOrientation(side) == ForgeDirection.UP) {
			if(metadata == 0)
				return topEmpty;
			else if(metadata == 1)
				return top;
			else return topFull;
		}
		
		return sideTex;
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityComposter();
	}

	@Override
	public boolean onBlockActivated(World world, int x,	int y, int z, EntityPlayer player, int par1, float par2, float par3, float par4) {

		TileEntityComposter tile = (TileEntityComposter)world.getTileEntity(x,y,z);

		if(tile == null)
			return false;

		tile.canInsertItem(0, player.getHeldItem(), ForgeDirection.UP.ordinal());

		if(tile.getStackInSlot(1) != null && !world.isRemote)
		{
			world.spawnEntityInWorld(new EntityItem(world,player.posX, player.posY, player.posZ, tile.decrStackSize(1, 1)));
			return true;
		}

		if(player.getHeldItem() != null) {
			//Fill with water if applicable
			
			if(player.getHeldItem() != null && FluidContainerRegistry.isContainer(player.getHeldItem()) && FluidContainerRegistry.containsFluid(player.getHeldItem(), new FluidStack(FluidRegistry.WATER,1)) )
			{
				ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(player.getHeldItem());
				
				if(emptyContainer != null && 1000 == tile.fill(ForgeDirection.UP, FluidContainerRegistry.getFluidForFilledItem(player.getHeldItem()), false)) {
					tile.fill(ForgeDirection.UP, new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.getFluidForFilledItem(player.getHeldItem()).amount), true);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, emptyContainer); 
				}
			}
			
				
				
			/*if(player.getHeldItem().getItem() == Items.water_bucket) {
				tile.fill(ForgeDirection.UP, new FluidStack(FluidRegistry.WATER, 1000), true);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket)); 
			}*/
			else if( player.getHeldItem().stackSize >= 4 && ((TileEntityComposter)tile).isPlantMatter(player.getHeldItem())) {
				if(tile.getStackInSlot(0) == null)
					tile.setInventorySlotContents(0, player.getHeldItem().splitStack(4));
				else if(tile.getInventoryStackLimit() - tile.getStackInSlot(0).stackSize >= 4) {
					tile.getStackInSlot(0).stackSize += 4;
					player.inventory.decrStackSize(player.inventory.currentItem, 4);
				}
			}
		}
		else if(player.worldObj.isRemote)
			((TileEntityComposter)tile).startDisplay();//Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((new ChatComponentText(tile.infoToChat())));

		return true;
	}
}
