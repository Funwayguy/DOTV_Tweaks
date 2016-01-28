package dotv.vadis365;

import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerRespawnNBTHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClonePlayer(PlayerEvent.Clone event)
	{
		World world = event.entity.worldObj;
		if(world.isRemote)
			return;
		
		event.entityPlayer.inventory.clearInventory(Items.diamond_pickaxe, -1);
		
		if(event.original.getEntityData().hasKey("START"))
		{
			boolean start = event.original.getEntityData().getBoolean("START");
			event.entityPlayer.getEntityData().setBoolean("START", start);
			if(start)
				event.entityPlayer.getEntityData().setBoolean("START", false);
		}
		if(event.wasDeath)
		{
			if(event.original.getEntityData().hasKey("DEATH_DIMENSION"))
			{
				int dimension = event.original.getEntityData().getInteger("DEATH_DIMENSION");
				boolean respawn = event.original.getEntityData().getBoolean("DO_RESPAWN");
				event.entityPlayer.getEntityData().setInteger("DEATH_DIMENSION", dimension);
				event.entityPlayer.getEntityData().setBoolean("DO_RESPAWN", respawn);
			}
		}
	}
}
