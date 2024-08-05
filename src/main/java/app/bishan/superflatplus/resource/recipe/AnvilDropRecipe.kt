package app.bishan.superflatplus.resource.recipe

import app.bishan.superflatplus.SuperflatPlus.isSuperflat
import app.bishan.superflatplus.resource.CustomRecipeManager
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.recipe.Recipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class AnvilDropRecipe(val result: BlockState, val ingredients: List<BlockState>) :
    Recipe<AnvilDropInventory> {

    companion object {
        @JvmStatic
        fun mixinOnLanding(world: World, pos: BlockPos) {
            // attempt to get first match with given block order
            world.recipeManager.getFirstMatch(
                CustomRecipeManager.ANVIL_DROP,
                AnvilDropInventory(
                    mutableListOf(
                        world.getBlockState(pos.down()), world.getBlockState(pos.down(2))
                    )
                ),
                world
            ).ifPresent { recipe ->
                val offset = recipe.value.ingredients.size

                (0 until offset).forEach { i ->
                    world.breakBlock(pos.down(i + 1), false)
                }

                world.setBlockState(
                    pos.down(offset), recipe.value.result
                )
            }
        }

    }

    override fun matches(inventory: AnvilDropInventory?, world: World?): Boolean =
        world?.isSuperflat == true && inventory?.matches(ingredients) == true

    override fun craft(input: AnvilDropInventory?, lookup: RegistryWrapper.WrapperLookup?): ItemStack {
        return result.block.asItem().defaultStack
    }

    override fun fits(width: Int, height: Int) = true
    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup?): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getSerializer() = AnvilRecipeSerializer

    override fun getType() = CustomRecipeManager.ANVIL_DROP


}
