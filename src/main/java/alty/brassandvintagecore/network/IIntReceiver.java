package alty.brassandvintagecore.network;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IIntReceiver {
	public void receiveInt(int identifier, int message, @Nullable EntityPlayerMP sendingPlayer);
}
