package com.volmit.iris.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.volmit.iris.Iris;
import com.volmit.iris.object.InventorySlotType;
import com.volmit.iris.object.IrisLootTable;
import com.volmit.iris.util.KList;
import com.volmit.iris.util.KMap;
import com.volmit.iris.util.KSet;
import com.volmit.iris.util.MortarCommand;
import com.volmit.iris.util.MortarSender;
import com.volmit.iris.util.O;
import com.volmit.iris.util.RNG;

public class CommandIrisLoot extends MortarCommand
{
	public CommandIrisLoot()
	{
		super("loot");
		setDescription("Show loot if a chest were right here");
		requiresPermission(Iris.perm.studio);
		setCategory("Loot");
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		if(sender.isPlayer())
		{
			Player p = sender.player();
			KSet<IrisLootTable> tables = Iris.proj.getCurrentProject().getGlUpdate().getLootTables(p.getLocation().getBlock());
			Inventory inv = Bukkit.createInventory(null, 27 * 2);
			Iris.proj.getCurrentProject().getGlUpdate().addItems(true, inv, RNG.r, tables, InventorySlotType.STORAGE, p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			p.openInventory(inv);

			for(IrisLootTable i : tables)
			{
				sender.sendMessage("- " + i.getName());
			}

			boolean ffast = false;
			boolean fadd = false;

			for(String i : args)
			{
				if(i.equals("--fast"))
				{
					ffast = true;
				}

				if(i.equals("--add"))
				{
					fadd = true;
				}
			}

			boolean fast = ffast;
			boolean add = fadd;
			O<Integer> ta = new O<Integer>();
			ta.set(-1);

			ta.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(Iris.instance, () ->
			{
				if(!p.getOpenInventory().getType().equals(InventoryType.CHEST))
				{
					Bukkit.getScheduler().cancelTask(ta.get());
					return;
				}

				if(!add)
				{
					inv.clear();
				}

				Iris.proj.getCurrentProject().getGlUpdate().addItems(true, inv, new RNG(RNG.r.imax()), tables, InventorySlotType.STORAGE, p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			}, 0, fast ? 5 : 35));

			return true;
		}

		else
		{
			sender.sendMessage("Players only.");
		}

		return true;
	}

	@Override
	protected String getArgsUsage()
	{
		return "[width]";
	}
}
