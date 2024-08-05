package app.bishan.superflatplus.resource.recipe

import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

class AnvilDropInventory(private val blocks: MutableList<BlockState>) : RecipeInput {

    fun matches(it: List<BlockState>) = blocks.zip(it).all { (a, b) -> a == b }

    override fun getStackInSlot(slot: Int): ItemStack = blocks[slot].block.asItem().defaultStack

    override fun getSize(): Int = blocks.size

}
