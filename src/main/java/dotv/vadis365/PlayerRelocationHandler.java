package dotv.vadis365;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dotv.core.DOTV_Settings;

public class PlayerRelocationHandler
{
	@SubscribeEvent
	public void teleportCheck(LivingEvent.LivingUpdateEvent event)
	{
		World world = event.entity.worldObj;
		if(world.isRemote)
			return;
		
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayerMP player = (EntityPlayerMP)event.entity;
			if(player.getEntityData().hasKey("DEATH_DIMENSION") && player.getEntityData().getBoolean("DO_RESPAWN"))
			{
				int dimension = player.getEntityData().getInteger("DEATH_DIMENSION");
				WorldServer world2 = DimensionManager.getWorld(dimension);
				if(player.dimension != dimension && dimension != DOTV_Settings.erebusDimID && dimension != 0)
				{//may need other dimensions black listed to enable their portals
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimension, new RespawnTestTeleporter(world2));
					if(dimension == 1)
						player.playerNetServerHandler.setPlayerLocation(100D, 49.25D, 0D, 0F, 0F); // Default end spawn co-ord (will need to change)
					player.getEntityData().setBoolean("DO_RESPAWN", false);
				}
			}
		}
	}
}
