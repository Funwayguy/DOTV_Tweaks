package dotv;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import cpw.mods.fml.common.IWorldGenerator;

public class EndLavaGen implements IWorldGenerator
{
	boolean recursive = false;
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.isRemote || world.provider.dimensionId != 1 || recursive)
		{
			return;
		}
		
		recursive = true;
		
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		
		if(chunk == null)
		{
			recursive = false;
			return;
		}
		
		for(int i = 0; i < 16; i++)
		{
			for(int j = 0; j < 48; j++)
			{
				ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[j >> 4];
				
				if(ebs == null)
				{
					ebs = new ExtendedBlockStorage(j, !chunk.worldObj.provider.hasNoSky);
                    chunk.getBlockStorageArray()[j >> 4] = ebs;
				}
				
				for(int k = 0; k < 16; k++)
				{
					if(j == 0)
					{
						ebs.func_150818_a(i, j & 15, k, Blocks.bedrock);
					} else
					{
						ebs.func_150818_a(i, j & 15, k, Blocks.lava);
					}
					ebs.setExtBlockMetadata(i, j & 15, k, 0);
				}
			}
		}
		
		recursive = false;
	}
}
