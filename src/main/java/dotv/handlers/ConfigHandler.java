package dotv.handlers;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import dotv.core.DOTV;
import dotv.core.DOTV_Settings;

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
		
		DOTV_Settings.erebusKeyName = config.getString("Portal Key ID", Configuration.CATEGORY_GENERAL, "minecraft:nether_star", "The item ID player's are required to be holding to pass through portals");
		DOTV_Settings.erebusKeyMeta = config.getInt("Portal Key Meta", Configuration.CATEGORY_GENERAL, 0, 0, Integer.MAX_VALUE, "The item damage player's are required to be holding to pass through portals");
		DOTV_Settings.erebusDimID = config.getInt("Erebus Dimension ID", Configuration.CATEGORY_GENERAL, -100, Integer.MIN_VALUE, Integer.MAX_VALUE, "The dimension ID of the Erebus");
		
		config.save();
		
		DOTV.logger.log(Level.INFO, "Loaded configs...");
	}
}
