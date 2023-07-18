package app.bishan.superflatplus

import app.bishan.superflatplus.resource.CustomRecipeManager
import app.bishan.superflatplus.worldgen.FlatBiomeParameters
import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.recipe.RecipeMatcher
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.resource.featuretoggle.FeatureManager
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList
import net.minecraft.world.biome.source.util.MultiNoiseUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Function


object SuperflatPlus : ModInitializer {
	const val ID: String = "superflat-plus"

	@JvmStatic
	fun id(key: String) = Identifier(ID, key)

	val logger: Logger = LoggerFactory.getLogger(ID)

	@JvmStatic
	var feature: FeatureFlag? = null


	override fun onInitialize() {
		logger.info("Starting $ID")

		CustomRecipeManager.bootstrap()

		FabricLoader.getInstance().getModContainer(ID).ifPresent {
			ResourceManagerHelper.registerBuiltinResourcePack(
				id("superflat-plus-data"),
				it,
				ResourcePackActivationType.NORMAL
			)
		}
	}

	@JvmStatic
	val WorldAccess.isSuperflat get() = enabledFeatures.contains(feature)
}
