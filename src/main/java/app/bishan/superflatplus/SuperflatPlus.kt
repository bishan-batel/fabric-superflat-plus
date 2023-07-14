package app.bishan.superflatplus

import app.bishan.superflatplus.worldgen.FlatBiomeParameters
import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.fabricmc.api.ModInitializer
import net.minecraft.fluid.LavaFluid
import net.minecraft.fluid.WaterFluid
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList.Preset.BiomeSourceFunction
import net.minecraft.world.biome.source.util.MultiNoiseUtil
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Function


object SuperflatPlus : ModInitializer {
	const val ID: String = "superflat-plus"

	val logger: Logger = LoggerFactory.getLogger(ID)

	val flatPreset = MultiNoiseBiomeSourceParameterList.Preset(
		Identifier(ID, "superflat_plus"),
		object : BiomeSourceFunction {
			@Suppress("UNCHECKED_CAST")
			override fun <T> apply(function: Function<RegistryKey<Biome?>?, T>?): MultiNoiseUtil.Entries<T>? {
				val builder = ImmutableList.builder<Pair<NoiseHypercube, T?>>()

				FlatBiomeParameters()
					.writeOverworldBiomeParameters { pair: Pair<NoiseHypercube, RegistryKey<Biome?>?> ->
						builder.add(
							pair.mapSecond(function)
						)
					}

				return MultiNoiseUtil.Entries<T?>(builder.build()) as MultiNoiseUtil.Entries<T>?
			}
		}
	);

	val flatBiomeSourceKey: RegistryKey<MultiNoiseBiomeSourceParameterList> = RegistryKey.of(
		RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST,
		Identifier(ID, "superflat_plus")
	);

	override fun onInitialize() {
		logger.info("Starting $ID")

	}

}
