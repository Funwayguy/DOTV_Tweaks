package dotv.core;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import dotv.core.proxies.CommonProxy;
import dotv.handlers.ConfigHandler;

@Mod(modid = DOTV.MODID, version = DOTV.VERSION, name = DOTV.NAME, guiFactory = "dotv.handlers.ConfigGuiFactory")
public class DOTV
{
    public static final String MODID = "dotv";
    public static final String VERSION = "DOTV_KEY";
    public static final String NAME = "DOTV Tweaks";
    public static final String PROXY = "dotv.core.proxies";
    public static final String CHANNEL = "DOTV_CHAN";
	
	@Instance(MODID)
	public static DOTV instance;
	
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network ;
	public static Logger logger;
	
	public static Block decay;
	public static Block voidTNT;
	public static Block cleanseTNT;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);
    	
    	ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile(), true);
    	ConfigHandler.initConfigs();
    	
    	proxy.registerHandlers();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
