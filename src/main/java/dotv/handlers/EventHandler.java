package dotv.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import dotv.RespawnHandler;
import dotv.core.DOTV;
import dotv.core.DOTV_Settings;

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
	}
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event)
	{
		if(event.action == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.world.getBlock(event.x, event.y, event.z);
			
			if(block instanceof BlockBed && !event.world.isRemote)
			{
				event.entityPlayer.addChatComponentMessage(new ChatComponentText("Spawn Set"));
				event.entityPlayer.setSpawnChunk(event.entityPlayer.getPlayerCoordinates(), true, event.entityPlayer.dimension);
				event.setCanceled(true);
			}
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
        
        if(event.entityLiving instanceof EntityPlayer)
		{
        	/*if(event.entity.ticksExisted >= 1 && !event.entity.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean("DOTV_START"))
        	{
        		NBTTagCompound pTags = event.entity.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    			pTags.setBoolean("DOTV_START", true);
    			event.entity.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, pTags);
    			event.entity.travelToDimension(1);
    			return;
        	}*/
        	
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			//int respawnDim = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Death_Dimension");
			ItemStack item = player.getHeldItem();
			
			/*if(respawnDim != 0 && respawnDim != player.worldObj.provider.dimensionId)
			{
				event.entityLiving.timeUntilPortal = event.entityLiving.getPortalCooldown();
				RespawnHandler.RespawnPlayerInDimension(player, respawnDim);
				return;
			} else */if(!player.capabilities.isCreativeMode && player.posY >= 255) // Nerf living above any dimension
			{
				player.attackEntityFrom(DamageSource.outOfWorld, 2F);
			} else if(player.getBedLocation(player.dimension) != null && !player.isSpawnForced(player.dimension)) // Force player spawns even on broken beds. Prevents re-spawning on top of the world
			{
				player.setSpawnChunk(player.getBedLocation(player.dimension), true, player.dimension);
			}
			
			if(block == Blocks.portal || (player.worldObj.provider.dimensionId == DOTV_Settings.erebusDimID && (item == null || !Item.itemRegistry.getNameForObject(item.getItem()).equals(DOTV_Settings.erebusKeyName) || item.getItemDamage() != DOTV_Settings.erebusKeyMeta)))
			{
				event.entityLiving.timeUntilPortal = event.entityLiving.getPortalCooldown();
			}
		} else if(event.entityLiving instanceof EntityDragon)
		{
			event.entityLiving.setDead();
			return;
		}
	}
	
	/*@SubscribeEvent
	public void onPlayerCopy(PlayerEvent.Clone event)
	{
		if(event.original.dimension == 0 && event.original.getHealth() > 0 && event.entityPlayer.dimension == 0)
		{
			NBTTagCompound pTags = event.entityPlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			pTags.setInteger("Death_Dimension", 0);
			event.entityPlayer.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, pTags);
		}
	}*/
	
	/*@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent event)
	{
		if(event.player.getHealth() > 0 && event.toDim == 0)
		{
			ChunkCoordinates coords = event.player.worldObj.provider.getEntrancePortalLocation();
			event.player.setPosition(coords.posX, coords.posY, coords.posZ);
			
			NBTTagCompound pTags = event.player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			pTags.setInteger("Death_Dimension", event.toDim);
			event.player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, pTags);
			
			coords = event.player.getBedLocation(event.player.worldObj.provider.dimensionId);
			
			if(coords == null)
			{
				event.player.setSpawnChunk(event.player.getPlayerCoordinates(), true, event.player.worldObj.provider.dimensionId);
			}
		}
	}*/
}
