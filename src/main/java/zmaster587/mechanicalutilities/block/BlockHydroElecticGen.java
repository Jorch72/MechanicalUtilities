package zmaster587.mechanicalutilities.block;

import java.util.ArrayList;
import java.util.List;

import ic2.api.item.IC2Items;
import ic2.api.tile.IWrenchable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Tiles.TileAdvWatermill;
import zmaster587.mechanicalutilities.Tiles.TileHydroElectricGen;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHydroElecticGen extends Block {

	
	private IIcon top, sideBasic, sideLV, sideMV, north, south, bottom;
	
	public BlockHydroElecticGen() {
		super(Material.iron);
	}
	
	/*@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,int arg1, float arg2, float arg3, float arg4) {
		//TileEntity tile = world.getBlockTileEntity(x, y, z);
		//if(tile == null || player.isSneaking())
			return false;

		//player.openGui(MechanicalUtilities.instance, 1, world, x, y, z);
		
		//return true;
	}*/
	
	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}
	
	//Require wench to break
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(IC2Items.getItem("machine"));
        return ret;
    }
    
    @Override 
    public int getDamageValue(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z);
    }
    
	//Meta bits 0 and 1 represent 90* Y and 180* X respectively
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		if(ForgeDirection.getOrientation(side).equals(ForgeDirection.NORTH))
			return this.north;
		else if(ForgeDirection.getOrientation(side).equals(ForgeDirection.EAST) || ForgeDirection.getOrientation(side).equals(ForgeDirection.WEST)) {
			switch(metadata) {
			case 0:
				return this.sideBasic;
			case 1:
				return this.sideLV;
			case 2:
				return this.sideMV;
			}
		}
			
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
        this.sideBasic = iconReg.registerIcon("mechanicalutilities:AdvWaterMillSideBasic");
        this.sideLV = iconReg.registerIcon("mechanicalutilities:AdvWaterMillSideLV");
        this.sideMV = iconReg.registerIcon("mechanicalutilities:AdvWaterMillSideMV");
        this.north = iconReg.registerIcon("mechanicalutilities:AdvWaterMillNorth");
        this.south = iconReg.registerIcon("mechanicalutilities:AdvWaterMillSouth");
        this.bottom = iconReg.registerIcon("mechanicalutilities:AdvWaterMillBottom");
    }
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TileHydroElectricGen(meta);
	}
	
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockid) {
    	((TileHydroElectricGen)world.getTileEntity(x, y, z)).checkConditions(world);
    }

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < 3; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}
}
