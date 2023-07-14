package app.bishan.superflatplus.worldgen

import app.bishan.superflatplus.SuperflatPlus
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome

internal object FlatBiomeKeys {
	val THE_VOID = register("the_void")

	@JvmField
	val PLAINS = register("plains")

	@JvmField
	val SUNFLOWER_PLAINS = register("sunflower_plains")

	@JvmField
	val SNOWY_PLAINS = register("snowy_plains")

	@JvmField
	val ICE_SPIKES = register("ice_spikes")

	@JvmField
	val DESERT = register("desert")

	@JvmField
	val SWAMP = register("swamp")

	@JvmField
	val MANGROVE_SWAMP = register("mangrove_swamp")

	@JvmField
	val FOREST = register("forest")

	@JvmField
	val FLOWER_FOREST = register("flower_forest")

	@JvmField
	val BIRCH_FOREST = register("birch_forest")

	@JvmField
	val DARK_FOREST = register("dark_forest")

	@JvmField
	val OLD_GROWTH_BIRCH_FOREST = register("old_growth_birch_forest")

	@JvmField
	val OLD_GROWTH_PINE_TAIGA = register("old_growth_pine_taiga")

	@JvmField
	val OLD_GROWTH_SPRUCE_TAIGA = register("old_growth_spruce_taiga")

	@JvmField
	val TAIGA = register("taiga")

	@JvmField
	val SNOWY_TAIGA = register("snowy_taiga")

	@JvmField
	val SAVANNA = register("savanna")

	@JvmField
	val SAVANNA_PLATEAU = register("savanna_plateau")

	@JvmField
	val WINDSWEPT_HILLS = register("windswept_hills")

	@JvmField
	val WINDSWEPT_GRAVELLY_HILLS = register("windswept_gravelly_hills")

	@JvmField
	val WINDSWEPT_FOREST = register("windswept_forest")

	@JvmField
	val WINDSWEPT_SAVANNA = register("windswept_savanna")

	@JvmField
	val JUNGLE = register("jungle")

	@JvmField
	val SPARSE_JUNGLE = register("sparse_jungle")

	@JvmField
	val BAMBOO_JUNGLE = register("bamboo_jungle")

	@JvmField
	val BADLANDS = register("badlands")

	@JvmField
	val ERODED_BADLANDS = register("eroded_badlands")

	@JvmField
	val WOODED_BADLANDS = register("wooded_badlands")

	@JvmField
	val MEADOW = register("meadow")

	@JvmField
	val CHERRY_GROVE = register("cherry_grove")

	@JvmField
	val GROVE = register("grove")

	@JvmField
	val SNOWY_SLOPES = register("snowy_slopes")

	@JvmField
	val FROZEN_PEAKS = register("frozen_peaks")

	@JvmField
	val JAGGED_PEAKS = register("jagged_peaks")

	@JvmField
	val STONY_PEAKS = register("stony_peaks")

	@JvmField
	val RIVER = register("river")

	@JvmField
	val FROZEN_RIVER = register("frozen_river")

	@JvmField
	val BEACH = register("beach")

	@JvmField
	val SNOWY_BEACH = register("snowy_beach")

	@JvmField
	val STONY_SHORE = register("stony_shore")

	@JvmField
	val WARM_OCEAN = register("warm_ocean")

	@JvmField
	val LUKEWARM_OCEAN = register("lukewarm_ocean")

	@JvmField
	val DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean")

	@JvmField
	val OCEAN = register("ocean")

	@JvmField
	val DEEP_OCEAN = register("deep_ocean")

	@JvmField
	val COLD_OCEAN = register("cold_ocean")

	@JvmField
	val DEEP_COLD_OCEAN = register("deep_cold_ocean")

	@JvmField
	val FROZEN_OCEAN = register("frozen_ocean")

	@JvmField
	val DEEP_FROZEN_OCEAN = register("deep_frozen_ocean")

	@JvmField
	val MUSHROOM_FIELDS = register("mushroom_fields")

	@JvmField
	val DRIPSTONE_CAVES = register("dripstone_caves")

	@JvmField
	val LUSH_CAVES = register("lush_caves")

	@JvmField
	val DEEP_DARK = register("deep_dark")
	val NETHER_WASTES = register("nether_wastes")
	val WARPED_FOREST = register("warped_forest")
	val CRIMSON_FOREST = register("crimson_forest")
	val SOUL_SAND_VALLEY = register("soul_sand_valley")
	val BASALT_DELTAS = register("basalt_deltas")
	val THE_END = register("the_end")
	val END_HIGHLANDS = register("end_highlands")
	val END_MIDLANDS = register("end_midlands")
	val SMALL_END_ISLANDS = register("small_end_islands")
	val END_BARRENS = register("end_barrens")
	private fun register(name: String): RegistryKey<Biome> =
		RegistryKey.of(RegistryKeys.BIOME, Identifier(SuperflatPlus.ID, name))
}
