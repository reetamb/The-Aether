package com.gildedgames.aether.registry;

import com.gildedgames.aether.Aether;
import com.gildedgames.aether.block.state.properties.AetherBlockStateProperties;
import com.gildedgames.aether.registry.AetherBlocks;
import com.gildedgames.aether.world.gen.feature.*;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.OptionalInt;

public class AetherFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Aether.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> QUICKSOIL = FEATURES.register("quicksoil", () -> new QuicksoilFeature(NoFeatureConfig.field_236558_a_));

    public static final RegistryObject<Feature<NoFeatureConfig>> COLD_AERCLOUD = FEATURES.register("cold_aercloud", () -> new ColdAercloudFeature(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<Feature<NoFeatureConfig>> BLUE_AERCLOUD = FEATURES.register("blue_aercloud", () -> new BlueAercloudFeature(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<Feature<NoFeatureConfig>> GOLD_AERCLOUD = FEATURES.register("gold_aercloud", () -> new GoldAercloudFeature(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<Feature<NoFeatureConfig>> PINK_AERCLOUD = FEATURES.register("pink_aercloud", () -> new PinkAercloudFeature(NoFeatureConfig.field_236558_a_));

    public static final RegistryObject<Feature<NoFeatureConfig>> CRYSTAL_TREE = FEATURES.register("crystal_tree", () -> new CrystalTreeFeature(NoFeatureConfig.field_236558_a_));

    public static final RegistryObject<Feature<NoFeatureConfig>> HOLYSTONE_SPHERE = FEATURES.register("holystone_sphere", () -> new HolystoneSphereFeature(NoFeatureConfig.field_236558_a_));
    
    public static final RegistryObject<Feature<BlockStateFeatureConfig>> LAKE = FEATURES.register("lake", () -> new AetherLakeFeature(BlockStateFeatureConfig.field_236455_a_));

    public static void registerConfiguredFeatures() {
        RuleTest HOLYSTONE = new BlockMatchRuleTest(AetherBlocks.HOLYSTONE.get());

        register("quicksoil", QUICKSOIL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(256).square().func_242731_b(10));

        register("cold_aercloud", COLD_AERCLOUD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(128).square().chance(5));
        register("blue_aercloud", BLUE_AERCLOUD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(96).square().chance(5));
        register("gold_aercloud", GOLD_AERCLOUD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(160).square().chance(5));
        register("pink_aercloud", PINK_AERCLOUD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(160).square().chance(7));

        register("crystal_tree", CRYSTAL_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).chance(15));
        register("water_lake", LAKE.get().withConfiguration(new BlockStateFeatureConfig(Blocks.WATER.getDefaultState())).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(4))));

        register("tree_skyroot", Feature.TREE.withConfiguration(
                (new BaseTreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LOG.get().getDefaultState().with(AetherBlockStateProperties.DOUBLE_DROPS, true)),
                        new SimpleBlockStateProvider(AetherBlocks.SKYROOT_LEAVES.get().getDefaultState().with(AetherBlockStateProperties.DOUBLE_DROPS, true)),
                        new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
                        new StraightTrunkPlacer(4, 2, 0),
                        new TwoLayerFeature(1, 0, 1)))
                        .setIgnoreVines().build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
        register("tree_golden_oak", Feature.TREE.withConfiguration(
                (new BaseTreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LOG.get().getDefaultState().with(AetherBlockStateProperties.DOUBLE_DROPS, true)),
                        new SimpleBlockStateProvider(AetherBlocks.GOLDEN_OAK_LEAVES.get().getDefaultState().with(AetherBlockStateProperties.DOUBLE_DROPS, true)),
                        new FancyFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(4), 4),
                        new FancyTrunkPlacer(3, 11, 0),
                        new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))))
                        .setIgnoreVines().func_236702_a_(Heightmap.Type.MOTION_BLOCKING).build())
                .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.03F, 1))));

        register("ore_aether_dirt", Feature.ORE.withConfiguration(new OreFeatureConfig(HOLYSTONE, AetherBlocks.AETHER_DIRT.get().getDefaultState(), 33)).range(256).square().func_242731_b(10));
        register("ore_icestone", Feature.ORE.withConfiguration(new OreFeatureConfig(HOLYSTONE, AetherBlocks.ICESTONE.get().getDefaultState(), 16)).range(256).square().func_242731_b(10));
        register("ore_ambrosium", Feature.ORE.withConfiguration(new OreFeatureConfig(HOLYSTONE, AetherBlocks.AMBROSIUM_ORE.get().getDefaultState().with(AetherBlockStateProperties.DOUBLE_DROPS, true), 16)).range(256).square().func_242731_b(10));
        register("ore_zanite", Feature.ORE.withConfiguration(new OreFeatureConfig(HOLYSTONE, AetherBlocks.ZANITE_ORE.get().getDefaultState(), 8)).range(256).square().func_242731_b(10));
        register("ore_gravitite", Feature.ORE.withConfiguration(new OreFeatureConfig(HOLYSTONE, AetherBlocks.ICESTONE.get().getDefaultState(), 6)).range(256).square().func_242731_b(10));

        register("spring_water", Feature.SPRING_FEATURE.withConfiguration(new LiquidsConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(AetherBlocks.HOLYSTONE.get(), AetherBlocks.AETHER_DIRT.get()))).withPlacement(Placement.RANGE_BIASED.configure(new TopSolidRangeConfig(8, 8, 256))).square().func_242731_b(50));

        register("aether_skylands_flowers", Feature.FLOWER.withConfiguration(
                (new BlockClusterFeatureConfig.Builder(
                        (new WeightedBlockStateProvider())
                                .addWeightedBlockstate(AetherBlocks.PURPLE_FLOWER.get().getDefaultState(), 1)
                                .addWeightedBlockstate(AetherBlocks.WHITE_FLOWER.get().getDefaultState(), 1)
                                .addWeightedBlockstate(AetherBlocks.BERRY_BUSH.get().getDefaultState(), 1), SimpleBlockPlacer.PLACER))
                        .tries(64).whitelist(ImmutableSet.of(AetherBlocks.AETHER_GRASS_BLOCK.get())).build())
                .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(2));
    }

    private static <FC extends IFeatureConfig> void register(String name, ConfiguredFeature<FC, ?> feature) {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Aether.MODID, name), feature);
    }
}