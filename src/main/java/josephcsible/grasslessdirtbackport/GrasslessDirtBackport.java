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

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = GrasslessDirtBackport.MODID, version = GrasslessDirtBackport.VERSION)
public class GrasslessDirtBackport
{
	// XXX duplication with mcmod.info and build.gradle
	public static final String MODID = "grasslessdirtbackport";
	public static final String VERSION = "1.0.0";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// In vanilla, regular and grassless dirt have the same unlocalized name.
		// We need to change that so we can add a localized name.
		BlockDirt.field_150009_a[1] = "grassless_dirt";
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.addRecipe(new ItemStack(Blocks.dirt, 1, 1), new Object[] {"DG", "GD", 'D', new ItemStack(Blocks.dirt, 1, 0), 'G', Blocks.gravel});
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onHarvestDrops(HarvestDropsEvent event) {
		// In vanilla, grassless dirt drops as regular dirt.
		// Make it drop as itself instead.
		if(event.block == Blocks.dirt && event.blockMetadata == 1 && !event.drops.isEmpty()) {
			ItemStack drop = event.drops.get(0);
			if(drop.getItem() == Item.getItemFromBlock(Blocks.dirt) && drop.getItemDamage() == 0)
				drop.setItemDamage(1);
		}
	}

	@SubscribeEvent
	public void onUseHoe(UseHoeEvent event) {
		if(event.world.getBlock(event.x, event.y, event.z) == Blocks.dirt &&
				event.world.getBlockMetadata(event.x, event.y, event.z) == 1 &&
				event.world.isAirBlock(event.x, event.y + 1, event.z)) {
			// Vanilla also enforces that you didn't hit the bottom of the block, but that information isn't exposed to us here, and it's really not worth the trouble to try to get it.
			event.world.playSoundEffect(event.x + 0.5F, event.y + 0.5F, event.z + 0.5F, Blocks.dirt.stepSound.getStepResourcePath(), (Blocks.dirt.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.dirt.stepSound.getPitch() * 0.8F);
			event.world.setBlock(event.x, event.y, event.z, Blocks.dirt);
			event.setResult(Result.ALLOW);
		}
	}
}
