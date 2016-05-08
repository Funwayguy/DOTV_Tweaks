package dotv.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import dotv.RespawnData;
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
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			
        	if(event.entity.ticksExisted >= 1 && !RespawnData.getData(player).started)
        	{
        		RespawnData.getData(player).started = true;
    			event.entity.travelToDimension(1);
    			return;
        	}
        	
			ItemStack item = player.getHeldItem();
			int respawnDim = RespawnData.getData(player).lastDim;
			
			if(respawnDim != player.worldObj.provider.dimensionId)
			{
				event.entityLiving.timeUntilPortal = event.entityLiving.getPortalCooldown();
				RespawnHandler.RespawnPlayerInDimension(player, respawnDim);
				return;
			} else if(!player.capabilities.isCreativeMode && player.posY >= 255 && !(player.worldObj.provider.dimensionId == 0 || player.worldObj.provider.dimensionId == 1))
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
	
	@SubscribeEvent
	public void onEntityConstruct(EntityConstructing event) // Register respawn data
	{
		if(event.entity instanceof EntityPlayer)
		{
			RespawnData.Register((EntityPlayer)event.entity);
		}
	}
	
	@SubscribeEvent
	public void onPlayerCopy(PlayerEvent.Clone event) // Makes respawn data persistent
	{
		RespawnData.Register(event.entityPlayer);
		RespawnData.Register(event.original); // Just in case
		
		NBTTagCompound data = new NBTTagCompound();
		RespawnData.getData(event.original).saveNBTData(data);
		RespawnData.getData(event.entityPlayer).loadNBTData(data);
	}
	
	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent event)
	{
		if(event.player.getHealth() > 0)
		{
			ChunkCoordinates coords = event.player.getBedLocation(event.player.worldObj.provider.dimensionId);
			
			if(event.toDim == 0) // Fixes overworld spawning
			{
				coords = event.player.worldObj.provider.getEntrancePortalLocation();
				event.player.setPosition(coords.posX, coords.posY, coords.posZ);
			} else if(event.toDim == DOTV_Settings.erebusDimID)
			{
				event.player.inventory.clearInventory(Items.diamond_pickaxe, -1);
			}
			
			RespawnData.getData(event.player).lastDim = event.toDim;
			
			if(coords == null)
			{
				event.player.setSpawnChunk(event.player.getPlayerCoordinates(), true, event.player.worldObj.provider.dimensionId);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(event.world.isRemote || !DOTV.proxy.isClient())
		{
			return;
		}
		
		ObfuscationReflectionHelper.setPrivateValue(WorldInfo.class, event.world.getWorldInfo(), true, "field_76110_t", "allowCommands");
	}
}
