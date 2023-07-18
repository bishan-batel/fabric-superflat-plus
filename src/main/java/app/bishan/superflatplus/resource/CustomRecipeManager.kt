package app.bishan.superflatplus.resource

import app.bishan.superflatplus.SuperflatPlus
import app.bishan.superflatplus.resource.recipe.AnvilDropInventory
import app.bishan.superflatplus.resource.recipe.AnvilDropRecipe
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType

object CustomRecipeManager {
	fun bootstrap() {
		SuperflatPlus.logger.info("Loading Custom Recipe Types")
	}

	@JvmField
	val ANVIL_DROP: CustomRecipeType<AnvilDropRecipe> = create("anvil_drop")

	private fun <T : Recipe<*>> create(id: String): CustomRecipeType<T> = CustomRecipeType(id)

	class CustomRecipeType<T : Recipe<*>>(val id: String) : RecipeType<T> {
		override fun toString(): String = id
	}
}
