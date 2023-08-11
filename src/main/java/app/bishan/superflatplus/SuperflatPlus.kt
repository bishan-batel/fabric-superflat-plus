package app.bishan.superflatplus

import app.bishan.superflatplus.resource.CustomRecipeManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.util.Identifier
import net.minecraft.world.WorldAccess
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object SuperflatPlus : ModInitializer {
	const val ID: String = "superflat-plus"
	const val STRUCTURE_MIN_CORNER_HEIGHT: Int = 3;

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
