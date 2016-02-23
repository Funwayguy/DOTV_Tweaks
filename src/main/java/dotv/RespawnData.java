package dotv;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class RespawnData implements IExtendedEntityProperties
{
	public static final String ID = "DOTV_RES_DATA";
	public boolean started = false;
	public int lastDim = -1; // Start in the End
	
	@Override
	public void saveNBTData(NBTTagCompound tagBase)
	{
		NBTTagCompound tags = new NBTTagCompound();
		
		tags.setBoolean("started", started);
		tags.setInteger("lastDim", lastDim);
		
		tagBase.setTag(ID, tags);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound tagBase)
	{
		NBTTagCompound tags = tagBase.getCompoundTag(ID);
		
		started = tags.getBoolean("started");
		lastDim = tags.hasKey("lastDim")? tags.getInteger("lastDim") : -1;
	}
	
	@Override
	public void init(Entity entity, World world)
	{
	}
	
	public static void Register(EntityPlayer player)
	{
		if(getData(player, false) != null)
		{
			return;
		}
		
		player.registerExtendedProperties(ID, new RespawnData());
	}
	
	// Safe version
	public static RespawnData getData(EntityPlayer player)
	{
		return getData(player, true);
	}
	
	private static RespawnData getData(EntityPlayer player, boolean create)
	{
		if(player == null)
		{
			return null;
		}
		
		RespawnData value = (RespawnData)player.getExtendedProperties(ID);
		
		if(value != null || !create)
		{
			return value;
		} else
		{
			value = new RespawnData();
			player.registerExtendedProperties(ID, value);
			return value;
		}
	}
}
