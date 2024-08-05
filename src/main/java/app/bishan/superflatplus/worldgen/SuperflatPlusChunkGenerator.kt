package app.bishan.superflatplus.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Blocks
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ChunkRegion
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.Blender
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.NoiseChunkGenerator
import net.minecraft.world.gen.noise.NoiseConfig
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.min

// Chunk generator extending from ChunkGenerator
class SuperflatPlusChunkGenerator(biomeSource: BiomeSource, settings: RegistryEntry<ChunkGeneratorSettings>) :
    NoiseChunkGenerator(biomeSource, settings) {

    companion object {
        @JvmStatic
        val CODEC: MapCodec<SuperflatPlusChunkGenerator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter { generator -> generator.biomeSource },
                ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter { generator -> generator.settings },
            ).apply(
                instance
            ) { biomeSource, settings ->
                SuperflatPlusChunkGenerator(
                    biomeSource, settings
                )
            }
        }
    }

    override fun buildSurface(
        region: ChunkRegion?, structures: StructureAccessor?, noiseConfig: NoiseConfig?, chunk: Chunk?
    ) {
    }

    override fun populateNoise(
        blender: Blender,
        noiseConfig: NoiseConfig,
        structureAccessor: StructureAccessor,
        chunk: Chunk
    ): CompletableFuture<Chunk>? {
        val list = listOf(Blocks.GRASS_BLOCK.defaultState)
        val mutable = BlockPos.Mutable()
        val heightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG)
        val heightmap2 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG)

        for (i in 0 until min(chunk.height, list.size)) {
            val blockState = list[i]
            if (blockState != null) {
                val j = chunk.bottomY + i

                for (k in 0 until 16) {
                    for (l in 0 until 16) {
                        chunk.setBlockState(mutable.set(k, j, l), blockState, false)
                        heightmap.trackUpdate(k, j, l, blockState)
                        heightmap2.trackUpdate(k, j, l, blockState)
                    }
                }
            }
        }

        return CompletableFuture.completedFuture(chunk)
    }

    override fun getCodec(): MapCodec<out ChunkGenerator> = CODEC
}