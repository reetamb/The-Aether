package com.gildedgames.aether.item.tools;

import com.gildedgames.aether.item.tools.abilities.IGravititeToolItem;
import com.gildedgames.aether.registry.AetherItemGroups;
import com.gildedgames.aether.registry.AetherItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResultType;

public class GravititePickaxeItem extends PickaxeItem implements IGravititeToolItem
{
	public GravititePickaxeItem() {
		super(AetherItemTier.GRAVITITE, 1, -2.8F, new Item.Properties().group(AetherItemGroups.AETHER_TOOLS));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		return IGravititeToolItem.super.onItemUse(context);
	}
	
	@Override
	public ActionResultType defaultItemUse(ItemUseContext context) {
		return super.onItemUse(context);
	}
}
