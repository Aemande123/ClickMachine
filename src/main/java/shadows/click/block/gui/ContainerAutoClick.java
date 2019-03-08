package shadows.click.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import shadows.click.ClickMachine;
import shadows.click.block.TileAutoClick;
import shadows.click.net.MessageUpdateGui;
import shadows.click.util.VanillaPacketDispatcher;

public class ContainerAutoClick extends Container {

	TileAutoClick tile;
	EntityPlayer player;

	public ContainerAutoClick(TileAutoClick tile, EntityPlayer player) {
		this.tile = tile;
		this.player = player;

		this.addSlotToContainer(new SlotItemHandler(tile.getHandler(), 0, 8, 35));

		for (int i1 = 0; i1 < 3; ++i1) {
			for (int k1 = 0; k1 < 9; ++k1) {
				this.addSlotToContainer(new Slot(player.inventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));
			}
		}

		for (int j1 = 0; j1 < 9; ++j1) {
			this.addSlotToContainer(new Slot(player.inventory, j1, 8 + j1 * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	public void handleButtonClick(int button) {
		if (button < 12) {
			if (button < 9) tile.setSpeedIndex(button);
			else if (button == 9) tile.setSneaking(!tile.isSneaking());
			else tile.setRightClicking(button == 11);
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack transferred = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);

		int otherSlots = this.inventorySlots.size() - 36;

		if (slot != null && slot.getHasStack()) {
			ItemStack current = slot.getStack();
			transferred = current.copy();

			if (slotIndex < otherSlots) {
				if (!this.mergeItemStack(current, otherSlots, this.inventorySlots.size(), true)) { return ItemStack.EMPTY; }
			} else if (!this.mergeItemStack(current, 0, otherSlots, false)) { return ItemStack.EMPTY; }
			slot.onSlotChanged();
		}

		return transferred;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		if (!player.world.isRemote) VanillaPacketDispatcher.dispatchTEToPlayer(tile, player);
	}

	boolean sent = false;

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!sent && tile.hasWorld() && !tile.getWorld().isRemote) {
			sent = true;
			ClickMachine.NETWORK.sendTo(new MessageUpdateGui(tile), (EntityPlayerMP) player);
		}
	}

}
