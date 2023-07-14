package app.bishan.superflatplus.worldgen;

import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.SharedConstants;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.function.ToFloatFunction;
import net.minecraft.util.math.Spline;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.ParameterRange;
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.densityfunction.DensityFunctions;

@SuppressWarnings("unchecked")
public final class FlatBiomeParameters {
    private final MultiNoiseUtil.ParameterRange defaultParameter = ParameterRange.of(-1.0F, 1.0F);
    private final MultiNoiseUtil.ParameterRange[] temperatureParameters = new MultiNoiseUtil.ParameterRange[]{ParameterRange.of(-1.0F, -0.45F), ParameterRange.of(-0.45F, -0.15F), ParameterRange.of(-0.15F, 0.2F), ParameterRange.of(0.2F, 0.55F), ParameterRange.of(0.55F, 1.0F)};
    private final MultiNoiseUtil.ParameterRange[] humidityParameters = new MultiNoiseUtil.ParameterRange[]{ParameterRange.of(-1.0F, -0.35F), ParameterRange.of(-0.35F, -0.1F), ParameterRange.of(-0.1F, 0.1F), ParameterRange.of(0.1F, 0.3F), ParameterRange.of(0.3F, 1.0F)};
    private final MultiNoiseUtil.ParameterRange[] erosionParameters = new MultiNoiseUtil.ParameterRange[]{ParameterRange.of(-1.0F, -0.78F), ParameterRange.of(-0.78F, -0.375F), ParameterRange.of(-0.375F, -0.2225F), ParameterRange.of(-0.2225F, 0.05F), ParameterRange.of(0.05F, 0.45F), ParameterRange.of(0.45F, 0.55F), ParameterRange.of(0.55F, 1.0F)};
    private final MultiNoiseUtil.ParameterRange frozenTemperature;
    private final MultiNoiseUtil.ParameterRange nonFrozenTemperatureParameters;
    private final MultiNoiseUtil.ParameterRange mushroomFieldsContinentalness;
    private final MultiNoiseUtil.ParameterRange deepOceanContinentalness;
    private final MultiNoiseUtil.ParameterRange oceanContinentalness;
    private final MultiNoiseUtil.ParameterRange coastContinentalness;
    private final MultiNoiseUtil.ParameterRange riverContinentalness;
    private final MultiNoiseUtil.ParameterRange nearInlandContinentalness;
    private final MultiNoiseUtil.ParameterRange midInlandContinentalness;
    private final MultiNoiseUtil.ParameterRange farInlandContinentalness;
    private final RegistryKey<Biome>[][] oceanBiomes;
    private final RegistryKey<Biome>[][] commonBiomes;
    private final RegistryKey<Biome>[][] uncommonBiomes;
    private final RegistryKey<Biome>[][] nearMountainBiomes;
    private final RegistryKey<Biome>[][] specialNearMountainBiomes;
    private final RegistryKey<Biome>[][] windsweptBiomes;

    public FlatBiomeParameters() {
        this.frozenTemperature = this.temperatureParameters[0];
        this.nonFrozenTemperatureParameters = ParameterRange.combine(this.temperatureParameters[1], this.temperatureParameters[4]);
        this.mushroomFieldsContinentalness = ParameterRange.of(-1.2F, -1.05F);
        this.deepOceanContinentalness = ParameterRange.of(-1.05F, -0.455F);
        this.oceanContinentalness = ParameterRange.of(-0.455F, -0.19F);
        this.coastContinentalness = ParameterRange.of(-0.19F, -0.11F);
        this.riverContinentalness = ParameterRange.of(-0.11F, 0.55F);
        this.nearInlandContinentalness = ParameterRange.of(-0.11F, 0.03F);
        this.midInlandContinentalness = ParameterRange.of(0.03F, 0.3F);
        this.farInlandContinentalness = ParameterRange.of(0.3F, 1.0F);
        this.oceanBiomes = new RegistryKey[][]{{FlatBiomeKeys.DEEP_FROZEN_OCEAN, FlatBiomeKeys.DEEP_COLD_OCEAN, FlatBiomeKeys.DEEP_OCEAN, FlatBiomeKeys.DEEP_LUKEWARM_OCEAN, FlatBiomeKeys.WARM_OCEAN}, {FlatBiomeKeys.FROZEN_OCEAN, FlatBiomeKeys.COLD_OCEAN, FlatBiomeKeys.OCEAN, FlatBiomeKeys.LUKEWARM_OCEAN, FlatBiomeKeys.WARM_OCEAN}};
        this.commonBiomes = new RegistryKey[][]{{FlatBiomeKeys.SNOWY_PLAINS, FlatBiomeKeys.SNOWY_PLAINS, FlatBiomeKeys.SNOWY_PLAINS, FlatBiomeKeys.SNOWY_TAIGA, FlatBiomeKeys.TAIGA}, {FlatBiomeKeys.PLAINS, FlatBiomeKeys.PLAINS, FlatBiomeKeys.FOREST, FlatBiomeKeys.TAIGA, FlatBiomeKeys.OLD_GROWTH_SPRUCE_TAIGA}, {FlatBiomeKeys.FLOWER_FOREST, FlatBiomeKeys.PLAINS, FlatBiomeKeys.FOREST, FlatBiomeKeys.BIRCH_FOREST, FlatBiomeKeys.DARK_FOREST}, {FlatBiomeKeys.SAVANNA, FlatBiomeKeys.SAVANNA, FlatBiomeKeys.FOREST, FlatBiomeKeys.JUNGLE, FlatBiomeKeys.JUNGLE}, {FlatBiomeKeys.DESERT, FlatBiomeKeys.DESERT, FlatBiomeKeys.DESERT, FlatBiomeKeys.DESERT, FlatBiomeKeys.DESERT}};
        this.uncommonBiomes = new RegistryKey[][]{{FlatBiomeKeys.ICE_SPIKES, null, FlatBiomeKeys.SNOWY_TAIGA, null, null}, {null, null, null, null, FlatBiomeKeys.OLD_GROWTH_PINE_TAIGA}, {FlatBiomeKeys.SUNFLOWER_PLAINS, null, null, FlatBiomeKeys.OLD_GROWTH_BIRCH_FOREST, null}, {null, null, FlatBiomeKeys.PLAINS, FlatBiomeKeys.SPARSE_JUNGLE, FlatBiomeKeys.BAMBOO_JUNGLE}, {null, null, null, null, null}};
        this.nearMountainBiomes = new RegistryKey[][]{{FlatBiomeKeys.SNOWY_PLAINS, FlatBiomeKeys.SNOWY_PLAINS, FlatBiomeKeys.SNOWY_PLAINS, FlatBiomeKeys.SNOWY_TAIGA, FlatBiomeKeys.SNOWY_TAIGA}, {FlatBiomeKeys.MEADOW, FlatBiomeKeys.MEADOW, FlatBiomeKeys.FOREST, FlatBiomeKeys.TAIGA, FlatBiomeKeys.OLD_GROWTH_SPRUCE_TAIGA}, {FlatBiomeKeys.MEADOW, FlatBiomeKeys.MEADOW, FlatBiomeKeys.MEADOW, FlatBiomeKeys.MEADOW, FlatBiomeKeys.DARK_FOREST}, {FlatBiomeKeys.SAVANNA_PLATEAU, FlatBiomeKeys.SAVANNA_PLATEAU, FlatBiomeKeys.FOREST, FlatBiomeKeys.FOREST, FlatBiomeKeys.JUNGLE}, {FlatBiomeKeys.BADLANDS, FlatBiomeKeys.BADLANDS, FlatBiomeKeys.BADLANDS, FlatBiomeKeys.WOODED_BADLANDS, FlatBiomeKeys.WOODED_BADLANDS}};
        this.specialNearMountainBiomes = new RegistryKey[][]{{FlatBiomeKeys.ICE_SPIKES, null, null, null, null}, {FlatBiomeKeys.CHERRY_GROVE, null, FlatBiomeKeys.MEADOW, FlatBiomeKeys.MEADOW, FlatBiomeKeys.OLD_GROWTH_PINE_TAIGA}, {FlatBiomeKeys.CHERRY_GROVE, FlatBiomeKeys.CHERRY_GROVE, FlatBiomeKeys.FOREST, FlatBiomeKeys.BIRCH_FOREST, null}, {null, null, null, null, null}, {FlatBiomeKeys.ERODED_BADLANDS, FlatBiomeKeys.ERODED_BADLANDS, null, null, null}};
        this.windsweptBiomes = new RegistryKey[][]{{FlatBiomeKeys.WINDSWEPT_GRAVELLY_HILLS, FlatBiomeKeys.WINDSWEPT_GRAVELLY_HILLS, FlatBiomeKeys.WINDSWEPT_HILLS, FlatBiomeKeys.WINDSWEPT_FOREST, FlatBiomeKeys.WINDSWEPT_FOREST}, {FlatBiomeKeys.WINDSWEPT_GRAVELLY_HILLS, FlatBiomeKeys.WINDSWEPT_GRAVELLY_HILLS, FlatBiomeKeys.WINDSWEPT_HILLS, FlatBiomeKeys.WINDSWEPT_FOREST, FlatBiomeKeys.WINDSWEPT_FOREST}, {FlatBiomeKeys.WINDSWEPT_HILLS, FlatBiomeKeys.WINDSWEPT_HILLS, FlatBiomeKeys.WINDSWEPT_HILLS, FlatBiomeKeys.WINDSWEPT_FOREST, FlatBiomeKeys.WINDSWEPT_FOREST}, {null, null, null, null, null}, {null, null, null, null, null}};
    }

    public void writeOverworldBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        if (SharedConstants.DEBUG_BIOME_SOURCE) {
            this.writeDebug(parameters);
        } else {
            this.writeOceanBiomes(parameters);
            this.writeLandBiomes(parameters);
            this.writeCaveBiomes(parameters);
        }
    }

    private void writeDebug(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        RegistryWrapper.WrapperLookup wrapperLookup = BuiltinRegistries.createWrapperLookup();
        RegistryEntryLookup<DensityFunction> registryEntryLookup = wrapperLookup.getWrapperOrThrow(RegistryKeys.DENSITY_FUNCTION);
        DensityFunctionTypes.Spline.DensityFunctionWrapper densityFunctionWrapper = new DensityFunctionTypes.Spline.DensityFunctionWrapper(registryEntryLookup.getOrThrow(DensityFunctions.CONTINENTS_OVERWORLD));
        DensityFunctionTypes.Spline.DensityFunctionWrapper densityFunctionWrapper2 = new DensityFunctionTypes.Spline.DensityFunctionWrapper(registryEntryLookup.getOrThrow(DensityFunctions.EROSION_OVERWORLD));
        DensityFunctionTypes.Spline.DensityFunctionWrapper densityFunctionWrapper3 = new DensityFunctionTypes.Spline.DensityFunctionWrapper(registryEntryLookup.getOrThrow(DensityFunctions.RIDGES_FOLDED_OVERWORLD));
        parameters.accept(Pair.of(MultiNoiseUtil.createNoiseHypercube(this.defaultParameter, this.defaultParameter, this.defaultParameter, this.defaultParameter, ParameterRange.of(0.0F), this.defaultParameter, 0.01F), FlatBiomeKeys.PLAINS));
        Spline<?, ?> spline = VanillaTerrainParametersCreator.createContinentalOffsetSpline(densityFunctionWrapper2, densityFunctionWrapper3, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, ToFloatFunction.IDENTITY);
        float[] var10;
        int var11;
        int var12;
        float f;
        if (spline instanceof Spline.Implementation<?, ?> implementation) {
            RegistryKey<Biome> registryKey = FlatBiomeKeys.DESERT;
            var10 = implementation.locations();
            var11 = var10.length;

            for (var12 = 0; var12 < var11; ++var12) {
                f = var10[var12];
                parameters.accept(Pair.of(MultiNoiseUtil.createNoiseHypercube(this.defaultParameter, this.defaultParameter, this.defaultParameter, ParameterRange.of(f), ParameterRange.of(0.0F), this.defaultParameter, 0.0F), registryKey));
                registryKey = registryKey == FlatBiomeKeys.DESERT ? FlatBiomeKeys.BADLANDS : FlatBiomeKeys.DESERT;
            }
        }

        Spline<?, ?> spline2 = VanillaTerrainParametersCreator.createOffsetSpline(densityFunctionWrapper, densityFunctionWrapper2, densityFunctionWrapper3, false);
        if (spline2 instanceof Spline.Implementation<?, ?> implementation2) {
            var10 = implementation2.locations();
            var11 = var10.length;

            for (var12 = 0; var12 < var11; ++var12) {
                f = var10[var12];
                parameters.accept(Pair.of(MultiNoiseUtil.createNoiseHypercube(this.defaultParameter, this.defaultParameter, ParameterRange.of(f), this.defaultParameter, ParameterRange.of(0.0F), this.defaultParameter, 0.0F), FlatBiomeKeys.SNOWY_TAIGA));
            }
        }

    }

    private void writeOceanBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        this.writeBiomeParameters(parameters, this.defaultParameter, this.defaultParameter, this.mushroomFieldsContinentalness, this.defaultParameter, this.defaultParameter, 0.0F, FlatBiomeKeys.MUSHROOM_FIELDS);

        for (int i = 0; i < this.temperatureParameters.length; ++i) {
            MultiNoiseUtil.ParameterRange parameterRange = this.temperatureParameters[i];
            this.writeBiomeParameters(parameters, parameterRange, this.defaultParameter, this.deepOceanContinentalness, this.defaultParameter, this.defaultParameter, 0.0F, this.oceanBiomes[0][i]);
            this.writeBiomeParameters(parameters, parameterRange, this.defaultParameter, this.oceanContinentalness, this.defaultParameter, this.defaultParameter, 0.0F, this.oceanBiomes[1][i]);
        }

    }

    private void writeLandBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        this.writeMidBiomes(parameters, ParameterRange.of(-1.0F, -0.93333334F));
        this.writeHighBiomes(parameters, ParameterRange.of(-0.93333334F, -0.7666667F));
        this.writePeakBiomes(parameters, ParameterRange.of(-0.7666667F, -0.56666666F));
        this.writeHighBiomes(parameters, ParameterRange.of(-0.56666666F, -0.4F));
        this.writeMidBiomes(parameters, ParameterRange.of(-0.4F, -0.26666668F));
        this.writeLowBiomes(parameters, ParameterRange.of(-0.26666668F, -0.05F));
        this.writeValleyBiomes(parameters, ParameterRange.of(-0.05F, 0.05F));
        this.writeLowBiomes(parameters, ParameterRange.of(0.05F, 0.26666668F));
        this.writeMidBiomes(parameters, ParameterRange.of(0.26666668F, 0.4F));
        this.writeHighBiomes(parameters, ParameterRange.of(0.4F, 0.56666666F));
        this.writePeakBiomes(parameters, ParameterRange.of(0.56666666F, 0.7666667F));
        this.writeHighBiomes(parameters, ParameterRange.of(0.7666667F, 0.93333334F));
        this.writeMidBiomes(parameters, ParameterRange.of(0.93333334F, 1.0F));
    }

    private void writePeakBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness) {
        for (int i = 0; i < this.temperatureParameters.length; ++i) {
            MultiNoiseUtil.ParameterRange parameterRange = this.temperatureParameters[i];

            for (int j = 0; j < this.humidityParameters.length; ++j) {
                MultiNoiseUtil.ParameterRange parameterRange2 = this.humidityParameters[j];
                RegistryKey<Biome> registryKey = this.getRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey2 = this.getBadlandsOrRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey3 = this.getMountainStartBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey4 = this.getNearMountainBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey5 = this.getWindsweptOrRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey6 = this.getBiomeOrWindsweptSavanna(i, j, weirdness, registryKey5);
                RegistryKey<Biome> registryKey7 = this.getPeakBiome(i, j, weirdness);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), this.erosionParameters[0], weirdness, 0.0F, registryKey7);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.nearInlandContinentalness), this.erosionParameters[1], weirdness, 0.0F, registryKey3);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[1], weirdness, 0.0F, registryKey7);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.nearInlandContinentalness), ParameterRange.combine(this.erosionParameters[2], this.erosionParameters[3]), weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[2], weirdness, 0.0F, registryKey4);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.midInlandContinentalness, this.erosionParameters[3], weirdness, 0.0F, registryKey2);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.farInlandContinentalness, this.erosionParameters[3], weirdness, 0.0F, registryKey4);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), this.erosionParameters[4], weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.nearInlandContinentalness), this.erosionParameters[5], weirdness, 0.0F, registryKey6);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[5], weirdness, 0.0F, registryKey5);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, registryKey);
            }
        }

    }

    private void writeHighBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness) {
        for (int i = 0; i < this.temperatureParameters.length; ++i) {
            MultiNoiseUtil.ParameterRange parameterRange = this.temperatureParameters[i];

            for (int j = 0; j < this.humidityParameters.length; ++j) {
                MultiNoiseUtil.ParameterRange parameterRange2 = this.humidityParameters[j];
                RegistryKey<Biome> registryKey = this.getRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey2 = this.getBadlandsOrRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey3 = this.getMountainStartBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey4 = this.getNearMountainBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey5 = this.getWindsweptOrRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey6 = this.getBiomeOrWindsweptSavanna(i, j, weirdness, registryKey);
                RegistryKey<Biome> registryKey7 = this.getMountainSlopeBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey8 = this.getPeakBiome(i, j, weirdness);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.nearInlandContinentalness, this.erosionParameters[0], weirdness, 0.0F, registryKey7);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[0], weirdness, 0.0F, registryKey8);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.nearInlandContinentalness, this.erosionParameters[1], weirdness, 0.0F, registryKey3);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[1], weirdness, 0.0F, registryKey7);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.nearInlandContinentalness), ParameterRange.combine(this.erosionParameters[2], this.erosionParameters[3]), weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[2], weirdness, 0.0F, registryKey4);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.midInlandContinentalness, this.erosionParameters[3], weirdness, 0.0F, registryKey2);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.farInlandContinentalness, this.erosionParameters[3], weirdness, 0.0F, registryKey4);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), this.erosionParameters[4], weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.nearInlandContinentalness), this.erosionParameters[5], weirdness, 0.0F, registryKey6);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[5], weirdness, 0.0F, registryKey5);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, registryKey);
            }
        }

    }

    private void writeMidBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness) {
        this.writeBiomeParameters(parameters, this.defaultParameter, this.defaultParameter, this.coastContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[2]), weirdness, 0.0F, FlatBiomeKeys.STONY_SHORE);
        this.writeBiomeParameters(parameters, ParameterRange.combine(this.temperatureParameters[1], this.temperatureParameters[2]), this.defaultParameter, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.SWAMP);
        this.writeBiomeParameters(parameters, ParameterRange.combine(this.temperatureParameters[3], this.temperatureParameters[4]), this.defaultParameter, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.MANGROVE_SWAMP);

        for (int i = 0; i < this.temperatureParameters.length; ++i) {
            MultiNoiseUtil.ParameterRange parameterRange = this.temperatureParameters[i];

            for (int j = 0; j < this.humidityParameters.length; ++j) {
                MultiNoiseUtil.ParameterRange parameterRange2 = this.humidityParameters[j];
                RegistryKey<Biome> registryKey = this.getRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey2 = this.getBadlandsOrRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey3 = this.getMountainStartBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey4 = this.getWindsweptOrRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey5 = this.getNearMountainBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey6 = this.getShoreBiome(i, j);
                RegistryKey<Biome> registryKey7 = this.getBiomeOrWindsweptSavanna(i, j, weirdness, registryKey);
                RegistryKey<Biome> registryKey8 = this.getErodedShoreBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey9 = this.getMountainSlopeBiome(i, j, weirdness);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[0], weirdness, 0.0F, registryKey9);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosionParameters[1], weirdness, 0.0F, registryKey3);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.farInlandContinentalness, this.erosionParameters[1], weirdness, 0.0F, i == 0 ? registryKey9 : registryKey5);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.nearInlandContinentalness, this.erosionParameters[2], weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.midInlandContinentalness, this.erosionParameters[2], weirdness, 0.0F, registryKey2);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.farInlandContinentalness, this.erosionParameters[2], weirdness, 0.0F, registryKey5);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.nearInlandContinentalness), this.erosionParameters[3], weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[3], weirdness, 0.0F, registryKey2);
                if (weirdness.max() < 0L) {
                    this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, this.erosionParameters[4], weirdness, 0.0F, registryKey6);
                    this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[4], weirdness, 0.0F, registryKey);
                } else {
                    this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), this.erosionParameters[4], weirdness, 0.0F, registryKey);
                }

                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, this.erosionParameters[5], weirdness, 0.0F, registryKey8);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.nearInlandContinentalness, this.erosionParameters[5], weirdness, 0.0F, registryKey7);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[5], weirdness, 0.0F, registryKey4);
                if (weirdness.max() < 0L) {
                    this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, this.erosionParameters[6], weirdness, 0.0F, registryKey6);
                } else {
                    this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, this.erosionParameters[6], weirdness, 0.0F, registryKey);
                }

                if (i == 0) {
                    this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, registryKey);
                }
            }
        }

    }

    private void writeLowBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness) {
        this.writeBiomeParameters(parameters, this.defaultParameter, this.defaultParameter, this.coastContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[2]), weirdness, 0.0F, FlatBiomeKeys.STONY_SHORE);
        this.writeBiomeParameters(parameters, ParameterRange.combine(this.temperatureParameters[1], this.temperatureParameters[2]), this.defaultParameter, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.SWAMP);
        this.writeBiomeParameters(parameters, ParameterRange.combine(this.temperatureParameters[3], this.temperatureParameters[4]), this.defaultParameter, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.MANGROVE_SWAMP);

        for (int i = 0; i < this.temperatureParameters.length; ++i) {
            MultiNoiseUtil.ParameterRange parameterRange = this.temperatureParameters[i];

            for (int j = 0; j < this.humidityParameters.length; ++j) {
                MultiNoiseUtil.ParameterRange parameterRange2 = this.humidityParameters[j];
                RegistryKey<Biome> registryKey = this.getRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey2 = this.getBadlandsOrRegularBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey3 = this.getMountainStartBiome(i, j, weirdness);
                RegistryKey<Biome> registryKey4 = this.getShoreBiome(i, j);
                RegistryKey<Biome> registryKey5 = this.getBiomeOrWindsweptSavanna(i, j, weirdness, registryKey);
                RegistryKey<Biome> registryKey6 = this.getErodedShoreBiome(i, j, weirdness);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.nearInlandContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, registryKey2);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, registryKey3);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.nearInlandContinentalness, ParameterRange.combine(this.erosionParameters[2], this.erosionParameters[3]), weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), ParameterRange.combine(this.erosionParameters[2], this.erosionParameters[3]), weirdness, 0.0F, registryKey2);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, ParameterRange.combine(this.erosionParameters[3], this.erosionParameters[4]), weirdness, 0.0F, registryKey4);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[4], weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, this.erosionParameters[5], weirdness, 0.0F, registryKey6);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.nearInlandContinentalness, this.erosionParameters[5], weirdness, 0.0F, registryKey5);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[5], weirdness, 0.0F, registryKey);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, this.coastContinentalness, this.erosionParameters[6], weirdness, 0.0F, registryKey4);
                if (i == 0) {
                    this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, registryKey);
                }
            }
        }

    }

    private void writeValleyBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness) {
        this.writeBiomeParameters(parameters, this.frozenTemperature, this.defaultParameter, this.coastContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, weirdness.max() < 0L ? FlatBiomeKeys.STONY_SHORE : FlatBiomeKeys.FROZEN_RIVER);
        this.writeBiomeParameters(parameters, this.nonFrozenTemperatureParameters, this.defaultParameter, this.coastContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, weirdness.max() < 0L ? FlatBiomeKeys.STONY_SHORE : FlatBiomeKeys.RIVER);
        this.writeBiomeParameters(parameters, this.frozenTemperature, this.defaultParameter, this.nearInlandContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, FlatBiomeKeys.FROZEN_RIVER);
        this.writeBiomeParameters(parameters, this.nonFrozenTemperatureParameters, this.defaultParameter, this.nearInlandContinentalness, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, FlatBiomeKeys.RIVER);
        this.writeBiomeParameters(parameters, this.frozenTemperature, this.defaultParameter, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), ParameterRange.combine(this.erosionParameters[2], this.erosionParameters[5]), weirdness, 0.0F, FlatBiomeKeys.FROZEN_RIVER);
        this.writeBiomeParameters(parameters, this.nonFrozenTemperatureParameters, this.defaultParameter, ParameterRange.combine(this.coastContinentalness, this.farInlandContinentalness), ParameterRange.combine(this.erosionParameters[2], this.erosionParameters[5]), weirdness, 0.0F, FlatBiomeKeys.RIVER);
        this.writeBiomeParameters(parameters, this.frozenTemperature, this.defaultParameter, this.coastContinentalness, this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.FROZEN_RIVER);
        this.writeBiomeParameters(parameters, this.nonFrozenTemperatureParameters, this.defaultParameter, this.coastContinentalness, this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.RIVER);
        this.writeBiomeParameters(parameters, ParameterRange.combine(this.temperatureParameters[1], this.temperatureParameters[2]), this.defaultParameter, ParameterRange.combine(this.riverContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.SWAMP);
        this.writeBiomeParameters(parameters, ParameterRange.combine(this.temperatureParameters[3], this.temperatureParameters[4]), this.defaultParameter, ParameterRange.combine(this.riverContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.MANGROVE_SWAMP);
        this.writeBiomeParameters(parameters, this.frozenTemperature, this.defaultParameter, ParameterRange.combine(this.riverContinentalness, this.farInlandContinentalness), this.erosionParameters[6], weirdness, 0.0F, FlatBiomeKeys.FROZEN_RIVER);

        for (int i = 0; i < this.temperatureParameters.length; ++i) {
            MultiNoiseUtil.ParameterRange parameterRange = this.temperatureParameters[i];

            for (int j = 0; j < this.humidityParameters.length; ++j) {
                MultiNoiseUtil.ParameterRange parameterRange2 = this.humidityParameters[j];
                RegistryKey<Biome> registryKey = this.getBadlandsOrRegularBiome(i, j, weirdness);
                this.writeBiomeParameters(parameters, parameterRange, parameterRange2, ParameterRange.combine(this.midInlandContinentalness, this.farInlandContinentalness), ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), weirdness, 0.0F, registryKey);
            }
        }

    }

    private void writeCaveBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        this.writeCaveBiomeParameters(parameters, this.defaultParameter, this.defaultParameter, ParameterRange.of(0.8F, 1.0F), this.defaultParameter, this.defaultParameter, 0.0F, FlatBiomeKeys.DRIPSTONE_CAVES);
        this.writeCaveBiomeParameters(parameters, this.defaultParameter, ParameterRange.of(0.7F, 1.0F), this.defaultParameter, this.defaultParameter, this.defaultParameter, 0.0F, FlatBiomeKeys.LUSH_CAVES);
        this.writeDeepDarkParameters(parameters, this.defaultParameter, this.defaultParameter, this.defaultParameter, ParameterRange.combine(this.erosionParameters[0], this.erosionParameters[1]), this.defaultParameter, 0.0F, FlatBiomeKeys.DEEP_DARK);
    }

    private RegistryKey<Biome> getRegularBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        if (weirdness.max() < 0L) {
            return this.commonBiomes[temperature][humidity];
        } else {
            RegistryKey<Biome> registryKey = this.uncommonBiomes[temperature][humidity];
            return registryKey == null ? this.commonBiomes[temperature][humidity] : registryKey;
        }
    }

    private RegistryKey<Biome> getBadlandsOrRegularBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        return temperature == 4 ? this.getBadlandsBiome(humidity, weirdness) : this.getRegularBiome(temperature, humidity, weirdness);
    }

    private RegistryKey<Biome> getMountainStartBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        return temperature == 0 ? this.getMountainSlopeBiome(temperature, humidity, weirdness) : this.getBadlandsOrRegularBiome(temperature, humidity, weirdness);
    }

    private RegistryKey<Biome> getBiomeOrWindsweptSavanna(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness, RegistryKey<Biome> biomeKey) {
        return temperature > 1 && humidity < 4 && weirdness.max() >= 0L ? FlatBiomeKeys.WINDSWEPT_SAVANNA : biomeKey;
    }

    private RegistryKey<Biome> getErodedShoreBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        RegistryKey<Biome> registryKey = weirdness.max() >= 0L ? this.getRegularBiome(temperature, humidity, weirdness) : this.getShoreBiome(temperature, humidity);
        return this.getBiomeOrWindsweptSavanna(temperature, humidity, weirdness, registryKey);
    }

    private RegistryKey<Biome> getShoreBiome(int temperature, int humidity) {
        if (temperature == 0) {
            return FlatBiomeKeys.SNOWY_BEACH;
        } else {
            return temperature == 4 ? FlatBiomeKeys.DESERT : FlatBiomeKeys.BEACH;
        }
    }

    private RegistryKey<Biome> getBadlandsBiome(int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        if (humidity < 2) {
            return weirdness.max() < 0L ? FlatBiomeKeys.BADLANDS : FlatBiomeKeys.ERODED_BADLANDS;
        } else {
            return humidity < 3 ? FlatBiomeKeys.BADLANDS : FlatBiomeKeys.WOODED_BADLANDS;
        }
    }

    private RegistryKey<Biome> getNearMountainBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        if (weirdness.max() >= 0L) {
            RegistryKey<Biome> registryKey = this.specialNearMountainBiomes[temperature][humidity];
            if (registryKey != null) {
                return registryKey;
            }
        }

        return this.nearMountainBiomes[temperature][humidity];
    }

    private RegistryKey<Biome> getPeakBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        if (temperature <= 2) {
            return weirdness.max() < 0L ? FlatBiomeKeys.JAGGED_PEAKS : FlatBiomeKeys.FROZEN_PEAKS;
        } else {
            return temperature == 3 ? FlatBiomeKeys.STONY_PEAKS : this.getBadlandsBiome(humidity, weirdness);
        }
    }

    private RegistryKey<Biome> getMountainSlopeBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        if (temperature >= 3) {
            return this.getNearMountainBiome(temperature, humidity, weirdness);
        } else {
            return humidity <= 1 ? FlatBiomeKeys.SNOWY_SLOPES : FlatBiomeKeys.GROVE;
        }
    }

    private RegistryKey<Biome> getWindsweptOrRegularBiome(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness) {
        RegistryKey<Biome> registryKey = this.windsweptBiomes[temperature][humidity];
        return registryKey == null ? this.getRegularBiome(temperature, humidity, weirdness) : registryKey;
    }

    private void writeBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        parameters.accept(Pair.of(MultiNoiseUtil.createNoiseHypercube(temperature, humidity, continentalness, erosion, ParameterRange.of(0.0F), weirdness, offset), biome));
        parameters.accept(Pair.of(MultiNoiseUtil.createNoiseHypercube(temperature, humidity, continentalness, erosion, ParameterRange.of(1.0F), weirdness, offset), biome));
    }

    private void writeCaveBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        parameters.accept(Pair.of(MultiNoiseUtil.createNoiseHypercube(temperature, humidity, continentalness, erosion, ParameterRange.of(0.2F, 0.9F), weirdness, offset), biome));
    }

    private void writeDeepDarkParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        parameters.accept(Pair.of(MultiNoiseUtil.createNoiseHypercube(temperature, humidity, continentalness, erosion, ParameterRange.of(1.1F), weirdness, offset), biome));
    }

}
