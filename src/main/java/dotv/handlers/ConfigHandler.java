package dotv.handlers;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import dotv.core.DOTV;

public class ConfigHandler
{
	public static Configuration config;
	
	public static void initConfigs()
	{
		if(config == null)
		{
			DOTV.logger.log(Level.ERROR, "Config attempted to be loaded before it was initialised!");
			return;
		}
		
		config.load();
		
		config.save();
		
		DOTV.logger.log(Level.INFO, "Loaded configs...");
	}
}
