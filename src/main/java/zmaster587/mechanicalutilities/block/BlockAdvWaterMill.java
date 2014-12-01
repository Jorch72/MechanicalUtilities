package zmaster587.mechanicalutilities.block;

import ic2.api.item.IC2Items;

import java.util.ArrayList;

import buildcraft.silicon.TileAdvancedCraftingTable;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Tiles.TileAdvWatermill;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockAdvWaterMill extends Block {

	private static int tankDrainAmt;
	private static int maxTankOutput;

	private IIcon top, side, north, south, bottom;

	public BlockAdvWaterMill(int maxOutput, int tankSize) {
		super(Material.iron);
		tankDrainAmt = tankSize;
		maxTankOutput = maxOutput;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,int arg1, float arg2, float arg3, float arg4) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile == null || player.isSneaking() || !(tile instanceof TileAdvWatermill))
			return false;
		if(!world.isRemote)
			player.openGui(MechanicalUtilities.instance, 0, world, x, y, z);

		return true;
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(IC2Items.getItem("machine"));
		return ret;
	}

	//Meta bits 0 and 1 represent 90* Y and 180* X respectively
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		if(ForgeDirection.getOrientation(side).equals(ForgeDirection.NORTH))
			return this.north;
		else if(ForgeDirection.getOrientation(side).equals(ForgeDirection.EAST) || ForgeDirection.getOrientation(side).equals(ForgeDirection.WEST))
			return this.side;
		else if(ForgeDirection.getOrientation(side).equals(ForgeDirection.SOUTH))
			return this.south;
		else if(ForgeDirection.getOrientation(side).equals(ForgeDirection.UP))
			return this.top;
		else if(ForgeDirection.getOrientation(side).equals(ForgeDirection.DOWN))
			return this.bottom;
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerBlockIcons(IIconRegister iconReg)
	{
		this.top = iconReg.registerIcon("mechanicalutilities:AdvWaterMillTop");
		this.side = iconReg.registerIcon("mechanicalutilities:AdvWaterMillSide");
		this.north = iconReg.registerIcon("mechanicalutilities:AdvWaterMillNorth");
		this.south = iconReg.registerIcon("mechanicalutilities:AdvWaterMillSouth");
		this.bottom = iconReg.registerIcon("mechanicalutilities:AdvWaterMillBottom");
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TileAdvWatermill(maxTankOutput, tankDrainAmt);
	}
}
