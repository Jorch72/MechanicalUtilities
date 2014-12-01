package zmaster587.mechanicalutilities.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import gregtech.api.GregTech_API;
import gregtech.common.blocks.GT_Block_Machines;

public class GregMachineBlock extends GT_Block_Machines {
	
	public GregMachineBlock() {
		super();
		
		try {
			Class.forName("gregtech.api.GregTech_API").getMethod("registerMachineBlock", Block.class, int.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    setHardness(1.0F);
	    setResistance(10.0F);
	    setStepSound(soundTypeMetal);
	    setCreativeTab(GregTech_API.TAB_GREGTECH);
	    this.isBlockContainer = true;
	}

}
