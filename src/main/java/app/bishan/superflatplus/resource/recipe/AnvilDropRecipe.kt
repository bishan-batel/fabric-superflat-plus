package app.bishan.superflatplus.resource.recipe

import app.bishan.superflatplus.SuperflatPlus.isSuperflat
import app.bishan.superflatplus.resource.CustomRecipeManager
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class AnvilDropRecipe(@JvmField val id: Identifier, val result: ItemStack, val ingredients: List<Block>) :
    Recipe<AnvilDropInventory> {

    companion object {
        @JvmStatic
        fun mixinOnLanding(world: World, pos: BlockPos) {
            // attempt to get first match with given block order
            world.recipeManager.getFirstMatch(
                CustomRecipeManager.ANVIL_DROP,
                AnvilDropInventory(
                    mutableListOf(
                        world.getBlockState(pos.down()).block, world.getBlockState(pos.down(2)).block
                    )
                ),
                world
            ).ifPresent { recipe ->
                val offset = recipe.ingredients.size

                (0 until offset).forEach { i ->
                    world.breakBlock(pos.down(i + 1), false)
                }

                world.setBlockState(
                    pos.down(offset), Block.getBlockFromItem(recipe.result.item).defaultState
                )
            }
        }

    }

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
