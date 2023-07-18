package app.bishan.superflatplus.resource

import app.bishan.superflatplus.SuperflatPlus
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper

object ResourceReloadListener : SimpleSynchronousResourceReloadListener {
	override fun reload(manager: ResourceManager?) {
		manager!!

		manager.findResources("superflat-plus/superflat-plus-data")
		{ it.path.endsWith(".json") }
			.forEach { (id, resource) ->
				try {
					val json = JsonHelper.deserialize(resource.inputStream.reader())
				} catch (err: Exception) {
					SuperflatPlus.logger.error("Failed to load resource $id", err)
				}

			}
	}

	override fun getFabricId() = Identifier(SuperflatPlus.ID, "superflat-plus-data")
}
