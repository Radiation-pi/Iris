package com.volmit.iris.gen.nms;

import org.bukkit.World;
import org.bukkit.WorldCreator;

public interface INMSCreator
{
	default World createWorld(WorldCreator creator)
	{
		return createWorld(creator, false);
	}

	public World createWorld(WorldCreator creator, boolean loadSpawn);
}
