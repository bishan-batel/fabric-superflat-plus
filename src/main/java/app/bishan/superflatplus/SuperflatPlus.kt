package app.bishan.superflatplus

import app.bishan.superflatplus.resource.CustomRecipeManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.util.Identifier
import net.minecraft.world.WorldAccess
import net.minecraft.world.gen.WorldPreset
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object SuperflatPlus : ModInitializer {
	@JvmField
	var WORLD_PRESET_ENABLED = false

	const val ID: String = "superflat-plus"
	const val STRUCTURE_MIN_CORNER_HEIGHT: Int = 3;

	@JvmField
	val WORLD_PRESET: RegistryKey<WorldPreset> =
		RegistryKey.of(RegistryKeys.WORLD_PRESET, id("superflat_plus"))

	@JvmStatic
	fun id(key: String) = Identifier(ID, key)

	@JvmField
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
