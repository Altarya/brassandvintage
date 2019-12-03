package alty.brassandvintagecore.network;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IStringReceiver {
	public void receiveString(String context, String message, @Nullable EntityPlayerMP sender);
}
