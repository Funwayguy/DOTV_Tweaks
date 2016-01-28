package dotv.vadis365;

import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class EndPlayerLoggedInEvent {

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

		World world = event.player.worldObj;
		if (world.isRemote)
			return;

		if (event.player.dimension != 1) {
			if (!event.player.getEntityData().hasKey("START")) {
				event.player.getEntityData().setBoolean("START", true);
				event.player.travelToDimension(1);
			}
		}
	}
}
