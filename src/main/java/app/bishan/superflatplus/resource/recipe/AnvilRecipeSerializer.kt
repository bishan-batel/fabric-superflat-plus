package app.bishan.superflatplus.resource.recipe

import app.bishan.superflatplus.SuperflatPlus
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object AnvilRecipeSerializer : RecipeSerializer<AnvilDropRecipe> {
    @JvmStatic
    val CODEC: MapCodec<AnvilDropRecipe> =
        RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                BlockState.CODEC.listOf().fieldOf("blocks").forGetter { it.ingredients },
                BlockState.CODEC.fieldOf("result").forGetter { it.result },
            ).apply(builder) { blocks, result -> AnvilDropRecipe(result, blocks) }
        }

    @JvmStatic
    val PACKED_CODEC: PacketCodec<RegistryByteBuf, AnvilDropRecipe> =
        PacketCodecs.codec(CODEC.codec()).cast()


    override fun codec(): MapCodec<AnvilDropRecipe> = CODEC

    override fun packetCodec(): PacketCodec<RegistryByteBuf, AnvilDropRecipe> = PACKED_CODEC

    @JvmStatic
    fun bootstrap() {
        Registry.register(Registries.RECIPE_SERIALIZER, SuperflatPlus.id("anvil_drop"), this)
    }

//    override fun read(buf: PacketByteBuf): AnvilDropRecipe = AnvilDropRecipe(
//        NbtHelper.toBlockState(Registries.BLOCK.readOnlyWrapper, buf.readNbt()), buf.readCollection({ _ -> listOf() }, {
//            NbtHelper.toBlockState(Registries.BLOCK.readOnlyWrapper, buf.readNbt())
//        })
//    )
//
//    override fun write(buf: PacketByteBuf, recipe: AnvilDropRecipe) {
//        buf.writeNbt(NbtHelper.fromBlockState(recipe.result))
//
//        buf.writeCollection(recipe.ingredients) { bufSlice, block ->
//            bufSlice.writeNbt(NbtHelper.fromBlockState(block))
//        }
//    }
}
