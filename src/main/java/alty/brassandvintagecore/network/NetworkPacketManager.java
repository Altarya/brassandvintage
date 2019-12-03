package alty.brassandvintagecore.network;

import alty.brassandvintagecore.init.BaVInitialization;
import alty.brassandvintagecore.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkPacketManager {
	public static SimpleNetworkWrapper network;
	
	public static EntityPlayer getPlayerFromContext(MessageContext ctx){
		EntityPlayer thePlayer = (ctx.side.isClient() ? BaVInitialization.proxy.getPlayerClient() : ctx.getServerHandler().player);
		return thePlayer;
	}
	
	public static void preInit(FMLPreInitializationEvent event) {

		network = NetworkRegistry.INSTANCE.newSimpleChannel(BaVInitialization.MODID);
		int packetId = 0;
		network.registerMessage(SendStringToClient.class, SendStringToClient.class, packetId++, Side.CLIENT);
		network.registerMessage(SendIntToClient.class, SendIntToClient.class, packetId++, Side.CLIENT);
		network.registerMessage(SendSpinToClient.class, SendSpinToClient.class, packetId++, Side.CLIENT);
		network.registerMessage(PacketOpenPlayerGUI.Handler.class, PacketOpenPlayerGUI.class,  packetId++, Side.SERVER);
		
		
	}
	

}
