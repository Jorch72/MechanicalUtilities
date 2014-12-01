package zmaster587.mechanicalutilities.container;

import zmaster587.libVulpes.gui.SlotSingleItem;
import zmaster587.mechanicalutilities.MechanicalUtilities;
import zmaster587.mechanicalutilities.Tiles.TileLadderExtender;
import zmaster587.mechanicalutilities.block.BlockLadderExtender;
import net.minecraft.block.BlockLadder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerLadderExtender extends Container {

	private TileLadderExtender tile;
	
	public ContainerLadderExtender(InventoryPlayer playerInv,  TileLadderExtender te) {
		final int x,y ;
		tile = te;
		addSlotToContainer(new SlotSingleItem(tile,0,81,33, MechanicalUtilities.itemSpaceLadder));
		// Player inventory
		for (int i1 = 0; i1 < 3; i1++) {
			for (int l1 = 0; l1 < 9; l1++) {
				addSlotToContainer(new Slot(playerInv, l1 + i1 * 9 + 9, 8 + l1 * 18, 89 + i1 * 18));
			}
		}

		// Player hotbar
		for (int j1 = 0; j1 < 9; j1++) {
			addSlotToContainer(new Slot(playerInv, j1, 8 + j1 * 18, 147));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		if(tile != null)
			return tile.isUseableByPlayer(entityplayer);
		else return false;
	}

	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		System.out.println(slot);
		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {

			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			
			if(stack.getItem() != MechanicalUtilities.itemSpaceLadder)
				return null;

			//merges the item into player inventory since its in the tileEntity
			if (slot == 0) {
				if (!this.mergeItemStack(stackInSlot, 1, 35, true)) {
					return null;
				}
			}
			//places it into the tileEntity is possible since its in the player inventory
			else if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}
			
			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		
		return stack;
	}
}
