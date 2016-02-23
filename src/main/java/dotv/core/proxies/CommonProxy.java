package dotv.core.proxies;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import dotv.EndLavaGen;
import dotv.handlers.EventHandler;

public class CommonProxy
{
	public boolean isClient()
	{
		return false;
	}
	
	public void registerHandlers()
	{
		// Vadis respawn stuffs
		//FMLCommonHandler.instance().bus().register(new EndPlayerLoggedInEvent());
		//MinecraftForge.EVENT_BUS.register(new PlayerRespawnNBTHandler());
		//MinecraftForge.EVENT_BUS.register(new EntityDeathDimensionHandler());
		//MinecraftForge.EVENT_BUS.register(new PlayerRelocationHandler());
		
		// Funwayguy pack tweaks
		EventHandler handler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);
		GameRegistry.registerWorldGenerator(new EndLavaGen(), 0);
	}
}
