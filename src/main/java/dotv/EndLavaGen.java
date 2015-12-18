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
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.dimensionId != 1)
		{
			return;
		}
		
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		
		if(chunk == null)
		{
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
					ebs.func_150818_a(i, j & 15, k, Blocks.lava);
					ebs.setExtBlockMetadata(i, j & 15, k, 0);
				}
			}
		}
	}
}
