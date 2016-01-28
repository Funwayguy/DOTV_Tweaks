package dotv.vadis365;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class RespawnTestTeleporter extends Teleporter {

	public RespawnTestTeleporter(WorldServer worldServer) {
		super(worldServer);
	}

	@Override
	public void placeInPortal(Entity pEntity, double posX, double posY, double posZ, float rotationYaw) {
	}
}