package app.bishan.superflatplus.resource.recipe

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class AnvilDropInventory(private val blocks: MutableList<BlockState>) : Inventory {

    fun matches(it: List<BlockState>) = blocks.zip(it).all { (a, b) -> a == b }

    override fun clear() = blocks.clear()
    override fun size() = blocks.size
    override fun isEmpty() = blocks.isEmpty()

    override fun getStack(slot: Int): ItemStack = blocks[slot].block.asItem().defaultStack

    override fun removeStack(slot: Int, amount: Int): ItemStack = removeStack(slot)

    override fun removeStack(slot: Int): ItemStack = blocks.removeAt(slot).block.asItem().defaultStack

    override fun setStack(slot: Int, stack: ItemStack) {
        blocks[slot] = Block.getBlockFromItem(stack.item).defaultState
    }

    override fun markDirty() {}

    override fun canPlayerUse(player: PlayerEntity?) = false
}
