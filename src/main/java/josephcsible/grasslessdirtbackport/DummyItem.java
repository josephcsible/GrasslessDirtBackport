/*
GrasslessDirtBackport Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package josephcsible.grasslessdirtbackport;

import java.util.List;
import java.util.ListIterator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DummyItem extends Item {
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		// This method gets called on all items that are set to appear in the creative
		// inventory, so they can add all variants of themselves. We use it to insert
		// grassless dirt after regular dirt (if possible, otherwise adding it at the end.)
		final ItemStack dirtStack = new ItemStack(Blocks.dirt, 1, 0);
		final ItemStack grasslessDirtStack = new ItemStack(Blocks.dirt, 1, 1);
		ListIterator<ItemStack> li = list.listIterator();
		while(li.hasNext()) {
			if(li.next().isItemEqual(dirtStack)) {
				li.add(grasslessDirtStack);
				return;
			}
		}
		list.add(grasslessDirtStack);
	}
}
