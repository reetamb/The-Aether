package com.gildedgames.aether.client;

import com.gildedgames.aether.Aether;
import com.gildedgames.aether.client.gui.screen.inventory.AltarScreen;
import com.gildedgames.aether.client.gui.screen.inventory.FreezerScreen;
import com.gildedgames.aether.client.gui.screen.inventory.IncubatorScreen;
import com.gildedgames.aether.client.gui.screen.inventory.LoreBookScreen;
import com.gildedgames.aether.client.renderer.entity.*;
import com.gildedgames.aether.client.renderer.tile.ChestMimicTileEntityRenderer;
import com.gildedgames.aether.client.renderer.tile.CustomItemStackTileEntityRenderer;
import com.gildedgames.aether.client.renderer.tile.SkyrootBedTileEntityRenderer;
import com.gildedgames.aether.client.renderer.tile.TreasureChestTileEntityRenderer;
import com.gildedgames.aether.entity.tile.ChestMimicTileEntity;
import com.gildedgames.aether.entity.tile.SkyrootBedTileEntity;
import com.gildedgames.aether.entity.tile.TreasureChestTileEntity;
import com.gildedgames.aether.registry.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Aether.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class AetherRendering
{
    public static void registerBlockRenderLayers() {
        RenderType cutout = RenderType.getCutout();
        RenderType mipped = RenderType.getCutoutMipped();
        RenderType translucent = RenderType.getTranslucent();

        render(AetherBlocks.COLD_AERCLOUD, translucent);
        render(AetherBlocks.BLUE_AERCLOUD, translucent);
        render(AetherBlocks.GOLDEN_AERCLOUD, translucent);
        render(AetherBlocks.PINK_AERCLOUD, translucent);
        render(AetherBlocks.AEROGEL, translucent);
        render(AetherBlocks.AEROGEL_SLAB, translucent);
        render(AetherBlocks.AEROGEL_STAIRS, translucent);
        render(AetherBlocks.AEROGEL_WALL, translucent);
        render(AetherBlocks.QUICKSOIL_GLASS, translucent);
        render(AetherBlocks.AETHER_PORTAL, translucent);
        render(AetherBlocks.BERRY_BUSH, cutout);
        render(AetherBlocks.BERRY_BUSH_STEM, cutout);
        render(AetherBlocks.AMBROSIUM_TORCH, cutout);
        render(AetherBlocks.AMBROSIUM_WALL_TORCH, cutout);
        render(AetherBlocks.SKYROOT_SAPLING, cutout);
        render(AetherBlocks.GOLDEN_OAK_SAPLING, cutout);
        render(AetherBlocks.PURPLE_FLOWER, cutout);
        render(AetherBlocks.WHITE_FLOWER, cutout);
        render(AetherBlocks.POTTED_PURPLE_FLOWER, cutout);
        render(AetherBlocks.POTTED_WHITE_FLOWER, cutout);
        render(AetherBlocks.POTTED_SKYROOT_SAPLING, cutout);
        render(AetherBlocks.POTTED_GOLDEN_OAK_SAPLING, cutout);
    }

    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.LIGHTNING_KNIFE.get(), LightningKnifeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.ZEPHYR_SNOWBALL.get(), m -> new SpriteRenderer<>(m, event.getMinecraftSupplier().get().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.GOLDEN_DART.get(), DartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.ENCHANTED_DART.get(), DartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.POISON_DART.get(), DartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.PHOENIX_ARROW.get(), PhoenixArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.SPECTRAL_PHOENIX_ARROW.get(), PhoenixArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.HAMMER_PROJECTILE.get(), HammerProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.FLOATING_BLOCK.get(), FloatingBlockRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.TNT_PRESENT.get(), TNTPresentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.MIMIC.get(), MimicRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.SENTRY.get(), SentryRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.ZEPHYR.get(), ZephyrRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.MOA.get(), MoaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.COCKATRICE.get(), CockatriceRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.AECHOR_PLANT.get(), AechorPlantRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.PHYG.get(), PhygRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.FLYING_COW.get(), FlyingCowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.SHEEPUFF.get(), SheepuffRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.AERWHALE.get(), AerwhaleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AetherEntityTypes.WHIRLWIND.get(), WhirlwindRenderer::new);
    }

    public static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntityRenderer(AetherTileEntityTypes.CHEST_MIMIC.get(), ChestMimicTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(AetherTileEntityTypes.TREASURE_CHEST.get(), TreasureChestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(AetherTileEntityTypes.SKYROOT_BED.get(), SkyrootBedTileEntityRenderer::new);
    }

    public static CustomItemStackTileEntityRenderer chestMimicRenderer() {
        return new CustomItemStackTileEntityRenderer(ChestMimicTileEntity::new);
    }

    public static CustomItemStackTileEntityRenderer treasureChestRenderer() {
        return new CustomItemStackTileEntityRenderer(TreasureChestTileEntity::new);
    }

    public static CustomItemStackTileEntityRenderer skyrootBedRenderer() {
        return new CustomItemStackTileEntityRenderer(SkyrootBedTileEntity::new);
    }

    public static void registerGuiFactories() {
        ScreenManager.registerFactory(AetherContainerTypes.BOOK_OF_LORE.get(), LoreBookScreen::new);
        ScreenManager.registerFactory(AetherContainerTypes.ALTAR.get(), AltarScreen::new);
        ScreenManager.registerFactory(AetherContainerTypes.FREEZER.get(), FreezerScreen::new);
        ScreenManager.registerFactory(AetherContainerTypes.INCUBATOR.get(), IncubatorScreen::new);
    }

    public static void registerItemModelProperties() {
        ItemModelsProperties.registerProperty(AetherItems.PHOENIX_BOW.get(), new ResourceLocation("pulling"), (stack, world, living)
                -> living != null && living.isHandActive() && living.getActiveItemStack() == stack ? 1.0F : 0.0F);
        ItemModelsProperties.registerProperty(AetherItems.PHOENIX_BOW.get(), new ResourceLocation("pull"), (stack, world, living) -> {
            if (living == null) {
                return 0.0F;
            } else {
                return living.getActiveItemStack() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getItemInUseCount()) / 20.0F;
            }
        });

        ItemModelsProperties.registerProperty(AetherItems.CANDY_CANE_SWORD.get(), new ResourceLocation("named"), (stack, world, living)
                -> stack.getDisplayName().getString().toLowerCase().equals("green candy cane sword") ? 1.0F : 0.0F);

        ItemModelsProperties.registerProperty(AetherItems.NOTCH_HAMMER.get(), new ResourceLocation("named"), (stack, world, living)
                -> stack.getDisplayName().getString().toLowerCase().equals("hammer of jeb") ? 1.0F : 0.0F);
    }

    private static void render(Supplier<? extends Block> block, RenderType render) {
        RenderTypeLookup.setRenderLayer(block.get(), render);
    }
}
