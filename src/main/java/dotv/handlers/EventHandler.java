package dotv.handlers;

import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import dotv.core.DOTV;

public class EventHandler
{
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equals(DOTV.MODID))
		{
			ConfigHandler.config.save();
			ConfigHandler.initConfigs();
		}
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityPlayer && !event.entity.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean("DOTV_START"))
		{
			NBTTagCompound pTags = event.entity.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			pTags.setBoolean("DOTV_START", true);
			event.entity.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, pTags);
			event.entity.travelToDimension(1);
			return;
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
        double d0 = event.entity.posY + (double)event.entity.getEyeHeight();
        int i = MathHelper.floor_double(event.entity.posX);
        int j = MathHelper.floor_float((float)MathHelper.floor_double(d0));
        int k = MathHelper.floor_double(event.entity.posZ);
        Block block = event.entity.worldObj.getBlock(i, j, k);
        
		if(event.entityLiving instanceof EntityPlayer && block == Blocks.portal)
		{
			event.entityLiving.timeUntilPortal = event.entityLiving.getPortalCooldown();
		} else if(event.entityLiving instanceof EntityDragon)
		{
			event.entityLiving.setDead();
			return;
		}
	}
	
	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent event)
	{
		if(event.player.getHealth() > 0 && event.toDim == 0)
		{
			ChunkCoordinates coords = event.player.worldObj.provider.getSpawnPoint();
			event.player.setPosition(coords.posX, coords.posY, coords.posZ);
			return;
		}
	}
}
