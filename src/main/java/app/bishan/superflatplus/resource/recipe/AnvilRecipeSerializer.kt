package app.bishan.superflatplus.resource.recipe

import app.bishan.superflatplus.SuperflatPlus
import com.google.gson.JsonObject
import net.minecraft.block.Block
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object AnvilRecipeSerializer : RecipeSerializer<AnvilDropRecipe> {
	@JvmStatic
	fun bootstrap() {
		Registry.register(Registries.RECIPE_SERIALIZER, SuperflatPlus.id("anvil_drop"), this)
	}

	override fun read(id: Identifier, json: JsonObject): AnvilDropRecipe {
		val result = Registries.ITEM.get(Identifier.tryParse(json.get("result").asString)!!).defaultStack

		val blocks = json.getAsJsonArray("blocks").map {
			Block.getBlockFromItem(
				Registries.ITEM.get(Identifier.tryParse(it.asString))
			)
		}

		return AnvilDropRecipe(id, result, blocks)
	}

	override fun read(id: Identifier, buf: PacketByteBuf): AnvilDropRecipe =
		AnvilDropRecipe(
			buf.readIdentifier(),
			buf.readItemStack(),
			buf.readCollection({ mutableListOf() })
			{ bId -> Registries.BLOCK.get(bId.readIdentifier()) }
		)

	override fun write(buf: PacketByteBuf, recipe: AnvilDropRecipe) {
		buf.writeIdentifier(recipe.id)
		buf.writeItemStack(recipe.result)
		buf.writeCollection(recipe.ingredients) { buf: PacketByteBuf, block: Block ->
			buf.writeIdentifier(Registries.BLOCK.getId(block))
		}
	}
}
