package app.bishan.superflatplus.resource.recipe

import app.bishan.superflatplus.SuperflatPlus.isSuperflat
import app.bishan.superflatplus.resource.CustomRecipeManager
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.world.World

class AnvilDropRecipe(@JvmField val id: Identifier, val result: ItemStack, val ingredients: List<Block>) :
	Recipe<AnvilDropInventory> {
	override fun matches(inventory: AnvilDropInventory?, world: World?): Boolean =
		world?.isSuperflat == true && inventory?.matches(ingredients) == true

	override fun craft(inventory: AnvilDropInventory?, registryManager: DynamicRegistryManager?): ItemStack {
		return getOutput(registryManager)
	}

	override fun fits(width: Int, height: Int) = true

	override fun getOutput(registryManager: DynamicRegistryManager?) = result

	override fun getId() = id

	override fun getSerializer() = AnvilRecipeSerializer

	override fun getType() = CustomRecipeManager.ANVIL_DROP
}
