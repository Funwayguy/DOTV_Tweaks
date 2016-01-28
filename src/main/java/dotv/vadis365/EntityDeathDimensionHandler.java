package dotv.vadis365;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityDeathDimensionHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void playerDeath(LivingDeathEvent event)
	{
		World world = event.entityLiving.worldObj;
		if(world.isRemote)
			return;
		
		if(event.entityLiving instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer)event.entityLiving;
			player.getEntityData().setInteger("DEATH_DIMENSION", player.dimension);
			player.getEntityData().setBoolean("DO_RESPAWN", true);
		}
	}
}