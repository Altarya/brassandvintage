package alty.brassandvintagecore.gui;

import alty.brassandvintagecore.items.BaVItems;
import alty.brassandvintagecore.network.NetworkPacketManager;
import alty.brassandvintagecore.network.PacketOpenPlayerGUI;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import net.minecraft.item.ItemStack;

public class BaVTab extends AbstractTab {
	public BaVTab() {
		super(0,0,0, new ItemStack(BaVItems.VANADIUM_INGOT));
	}

	@Override
	public void onTabClicked() {
		NetworkPacketManager.network.sendToServer(new PacketOpenPlayerGUI());
	}

	@Override
	public boolean shouldAddToList() {
		return true;
	}
}
